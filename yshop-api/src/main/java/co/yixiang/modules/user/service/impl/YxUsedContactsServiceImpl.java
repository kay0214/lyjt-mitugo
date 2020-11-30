package co.yixiang.modules.user.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupons.web.param.YxCouponOrderPassengParam;
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.service.impl.YxShipPassengerServiceImpl;
import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.mapper.YxUsedContactsMapper;
import co.yixiang.modules.user.service.YxUsedContactsService;
import co.yixiang.modules.user.web.dto.YxUsedContactsSaveDto;
import co.yixiang.modules.user.web.dto.YxUsedContactsUpdateDto;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 常用联系人表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUsedContactsServiceImpl extends BaseServiceImpl<YxUsedContactsMapper, YxUsedContacts> implements YxUsedContactsService {

    @Autowired
    private YxUsedContactsMapper yxUsedContactsMapper;

    @Autowired
    private IGenerator generator;
    @Autowired
    private YxShipPassengerServiceImpl yxShipPassengerService;

    @Override
    public YxUsedContactsQueryVo getYxUsedContactsById(Serializable id) throws Exception {
        return yxUsedContactsMapper.getYxUsedContactsById(id);
    }

    @Override
    public Paging<YxUsedContactsQueryVo> getYxUsedContactsPageList(YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception {
        Page page = setPageParam(yxUsedContactsQueryParam, OrderItem.desc("create_time"));
        IPage<YxUsedContactsQueryVo> iPage = yxUsedContactsMapper.getYxUsedContactsPageListByParam(page, yxUsedContactsQueryParam);
        return new Paging(iPage);
    }

    /**
     * 小程序端选择乘客
     *
     * @param yxCouponOrderQueryParam
     * @return
     */
    @Override
    public Paging<YxUsedContactsQueryVo> getUsedContactsByUserId(YxCouponOrderPassengParam yxCouponOrderQueryParam) {
        //联系人信息
        Page page = setPageParam(yxCouponOrderQueryParam, OrderItem.desc("create_time"));
        int uid = SecurityUtils.getUserId().intValue();
        YxUsedContactsQueryParam param = new YxUsedContactsQueryParam();
        param.setUserId(uid);
        BeanUtils.copyProperties(yxCouponOrderQueryParam, param);

        IPage<YxUsedContactsQueryVo> pageList = yxUsedContactsMapper.getYxUsedContactsPageListByParam(page, param);
        Paging<YxUsedContactsQueryVo> usedContactsQueryVoPaging = new Paging(pageList);
        if (usedContactsQueryVoPaging.getTotal() > 0) {
            List<YxUsedContactsQueryVo> usedContactsList = usedContactsQueryVoPaging.getRecords();
            //联系人信息
            for (YxUsedContactsQueryVo usedContacts : usedContactsList) {
                //未成年人
                int isAdult = 0;
                if (0 != usedContacts.getUserType()) {
                    if (StringUtils.isNotBlank(usedContacts.getCardId()) && IdCardUtils.isValid(usedContacts.getCardId())) {
                        Integer intAge = DateUtils.IdCardNoToAge(usedContacts.getCardId());
                        if (intAge >= 60) {
                            //岁数大于60 为老年人
                            isAdult = 2;
                        } else if (intAge < 60 && intAge >= 18) {
                            //小于60大于等于18 为成年人
                            isAdult = 1;
                        } else {
                            isAdult = 0;
                        }
                    }
                }

                usedContacts.setCardId(CardNumUtil.idEncrypt(usedContacts.getCardId()));
                usedContacts.setUserPhone(CardNumUtil.mobileEncrypt(usedContacts.getUserPhone()));
                usedContacts.setUserName(CardNumUtil.mobileEncrypt(usedContacts.getUserName()));
                usedContacts.setIsAdult(isAdult);
                //根据订单号以及联系人id，查看用户信息
                if (null != yxCouponOrderQueryParam.getOrderId()) {
                    int intUsed = getPassengerOrderByParam(yxCouponOrderQueryParam.getOrderId(), usedContacts.getId());
                    usedContacts.setUsedStatus(intUsed);
                }

            }
        }

        return usedContactsQueryVoPaging;
    }

    /**
     * 插入数据
     *
     * @param request
     * @return
     */
    @Override
    public boolean saveUsedContacts(YxUsedContactsSaveDto request) {
        YxUsedContacts save = generator.convert(request, YxUsedContacts.class);
        save.setDelFlag(0);
        save.setCreateUserId(request.getUserId());
        save.setCreateTime(DateTime.now().toTimestamp());
        this.save(save);
        return true;
    }

    /**
     * 更新数据
     *
     * @param request
     * @return
     */
    @Override
    public boolean updateUsedContacts(YxUsedContactsUpdateDto request) {
        YxUsedContacts find = this.getById(request.getId());
        if (null == find || 1 == find.getDelFlag()) {
            throw new BadRequestException("当前数据不存在");
        }
        YxUsedContacts update = generator.convert(request, YxUsedContacts.class);
        update.setUpdateUserId(request.getUserId());
        update.setUpdateTime(DateTime.now().toTimestamp());
        this.updateById(update);
        return true;
    }

    /**
     * 根据卡券订单id，以及联系人id，查找乘客信息
     * @param orderId
     * @param contastId
     * @return
     */
    private int getPassengerOrderByParam(Integer orderId,Integer contastId) {
        QueryWrapper<YxShipPassenger> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().
                isNotNull(YxShipPassenger::getBatchNo).
                eq(YxShipPassenger::getCouponOrderId, orderId).
                eq(YxShipPassenger::getContactsId,contastId);
        List<YxShipPassenger> passengerList = yxShipPassengerService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(passengerList)) {
            // 如果有批次号代表已核销，可以选择乘客
            return 0;
        }
        return 1;
    }}
