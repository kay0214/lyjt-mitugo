package co.yixiang.modules.user.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.web.param.YxCouponOrderPassengParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.mapper.YxUsedContactsMapper;
import co.yixiang.modules.user.service.YxUsedContactsService;
import co.yixiang.modules.user.web.dto.YxUsedContactsSaveDto;
import co.yixiang.modules.user.web.dto.YxUsedContactsUpdateDto;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsOrderQueryVo;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.CardNumUtil;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private YxCouponsMapper yxCouponsMapper;
    @Autowired
    private YxCouponOrderMapper yxCouponOrderMapper;
    @Autowired
    private IGenerator generator;

    @Override
    public YxUsedContactsQueryVo getYxUsedContactsById(Serializable id) throws Exception {
        return yxUsedContactsMapper.getYxUsedContactsById(id);
    }

    @Override
    public Paging<YxUsedContactsQueryVo> getYxUsedContactsPageList(YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception {
        Page page = setPageParam(yxUsedContactsQueryParam, OrderItem.desc("create_time"));
        IPage<YxUsedContactsQueryVo> iPage = yxUsedContactsMapper.getYxUsedContactsPageList(page, yxUsedContactsQueryParam);
        return new Paging(iPage);
    }

    /**
     * 小程序端选择乘客
     *
     * @param orderId
     * @param userCount
     * @return
     */
    @Override
    public YxUsedContactsOrderQueryVo getUsedContactsByUserId(YxCouponOrderPassengParam yxCouponOrderQueryParam) {
        YxUsedContactsOrderQueryVo yxUsedContactsOrderQueryVo = new YxUsedContactsOrderQueryVo();
        //订单
        YxCouponOrderQueryVo couponOrderQueryVo = yxCouponOrderMapper.getYxCouponOrderById(yxCouponOrderQueryParam.getOrderId());
        if (null == couponOrderQueryVo) {
            throw new ErrorRequestException("订单信息不存在！订单id：" + yxCouponOrderQueryParam.getOrderId());
        }
        YxCouponsQueryVo couponsQueryVo = yxCouponsMapper.getYxCouponsById(couponOrderQueryVo.getCouponId());
        if (null == couponsQueryVo) {
            throw new ErrorRequestException("卡券信息不存在！卡券id：" + couponOrderQueryVo.getCouponId());
        }
        // 乘客人数
        BigDecimal bigMaxPass = new BigDecimal(yxCouponOrderQueryParam.getUsedNum()).multiply(new BigDecimal(couponsQueryVo.getPassengersNum()));
        yxUsedContactsOrderQueryVo.setMaxPassengersNum(bigMaxPass.intValue());
        //联系人信息
        Page<YxUsedContacts> pageModel = new Page<>(yxCouponOrderQueryParam.getPage(), yxCouponOrderQueryParam.getLimit());
        int uid = SecurityUtils.getUserId().intValue();
        QueryWrapper<YxUsedContacts> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(YxUsedContacts::getUserId, uid).eq(YxUsedContacts::getDelFlag, 0);

        IPage<YxUsedContacts> pageList = yxUsedContactsMapper.selectPage(pageModel, queryWrapper);
        if (null == pageList.getRecords() || pageList.getRecords().size() <= 0) {
            return yxUsedContactsOrderQueryVo;
        }
        List<YxUsedContacts> usedContactsList = pageList.getRecords();
        //联系人信息
        List<YxUsedContactsQueryVo> contactsQueryVoList = new ArrayList<>();
        for (YxUsedContacts usedContacts : usedContactsList) {
            //未成年人
            YxUsedContactsQueryVo usedContactsQueryVo = new YxUsedContactsQueryVo();
            int isAdult = 0;
            BeanUtils.copyProperties(usedContacts, usedContactsQueryVo);
            if (0 != usedContacts.getUserType()) {
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

            usedContactsQueryVo.setCardId(CardNumUtil.idEncrypt(usedContacts.getCardId()));
            usedContactsQueryVo.setUserPhone(CardNumUtil.mobileEncrypt(usedContacts.getUserPhone()));
            usedContactsQueryVo.setIsAdult(isAdult);
            contactsQueryVoList.add(usedContactsQueryVo);
        }
        yxUsedContactsOrderQueryVo.setContactsQueryVoList(contactsQueryVoList);
        return yxUsedContactsOrderQueryVo;
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
}
