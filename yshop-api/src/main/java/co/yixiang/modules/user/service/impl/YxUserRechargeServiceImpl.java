/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.enums.PayTypeEnum;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.mapper.YxUserMapper;
import co.yixiang.modules.user.mapper.YxUserRechargeMapper;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.param.RechargeParam;
import co.yixiang.modules.user.web.param.YxUserRechargeQueryParam;
import co.yixiang.modules.user.web.vo.YxUserRechargeQueryVo;
import co.yixiang.modules.user.web.vo.YxWechatUserQueryVo;
import co.yixiang.mp.service.YxTemplateService;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 用户充值表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-03-02
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserRechargeServiceImpl extends BaseServiceImpl<YxUserRechargeMapper, YxUserRecharge> implements YxUserRechargeService {

    private final YxUserRechargeMapper yxUserRechargeMapper;
    private final YxUserBillService billService;
    private final YxUserMapper yxUserMapper;
    private final YxWechatUserService wechatUserService;
    private final YxTemplateService templateService;

    @Override
    public void updateRecharge(YxUserRecharge userRecharge) {
        YxUser user = yxUserMapper.selectById(userRecharge.getUid());

        //修改状态
        userRecharge.setPaid(OrderInfoEnum.PAY_STATUS_1.getValue());
        userRecharge.setPayTime(OrderUtil.getSecondTimestampTwo());
        yxUserRechargeMapper.updateById(userRecharge);

        //最终充值金额
        BigDecimal newPrice = NumberUtil.add(userRecharge.getPrice(),user.getNowMoney());
        newPrice = NumberUtil.add(userRecharge.getGivePrice(),newPrice);
        //增加流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(userRecharge.getUid());
        userBill.setTitle("用户余额充值");
        userBill.setLinkId(userRecharge.getId().toString());
        userBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        userBill.setType(BillDetailEnum.TYPE_1.getValue());
        userBill.setNumber(userRecharge.getPrice());
        userBill.setBalance(newPrice);
        userBill.setMark("成功充值余额"+userRecharge.getPrice());
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        userBill.setPm(BillEnum.PM_1.getValue());
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //update 余额
        user.setNowMoney(newPrice);
        yxUserMapper.updateById(user);

        //模板消息推送
        YxWechatUserQueryVo wechatUser =  wechatUserService.getYxWechatUserById(userRecharge.getUid());
        if(ObjectUtil.isNotNull(wechatUser)){
            //公众号与小程序打通统一公众号模板通知
            if(StrUtil.isNotBlank(wechatUser.getOpenid())){
                templateService.rechargeSuccessNotice(OrderUtil.stampToDate(userRecharge.getPayTime().toString()),
                        userRecharge.getPrice().toString(),wechatUser.getOpenid());
            }
        }
    }

    @Override
    public YxUserRecharge getInfoByOrderId(String orderId) {
        YxUserRecharge userRecharge = new YxUserRecharge();
        userRecharge.setOrderId(orderId);

        return yxUserRechargeMapper.selectOne(Wrappers.query(userRecharge));
    }

    /**
     * 充值
     * @param param
     */
    @Override
    public void addRecharge(RechargeParam param,int uid) {
        YxUserRecharge yxUserRecharge = new YxUserRecharge();

        YxUser user = yxUserMapper.selectById(uid);

        yxUserRecharge.setNickname(user.getNickname());
        yxUserRecharge.setOrderId(param.getOrderSn());
        yxUserRecharge.setUid(uid);
        yxUserRecharge.setPrice(BigDecimal.valueOf(param.getPrice()));
        yxUserRecharge.setGivePrice(BigDecimal.valueOf(param.getPaidPrice()));
        yxUserRecharge.setRechargeType(PayTypeEnum.WEIXIN.getValue());
        yxUserRecharge.setPaid(OrderInfoEnum.PAY_STATUS_0.getValue());
        yxUserRecharge.setAddTime(OrderUtil.getSecondTimestampTwo());

        yxUserRechargeMapper.insert(yxUserRecharge);

    }

    @Override
    public YxUserRechargeQueryVo getYxUserRechargeById(Serializable id) throws Exception{
        return yxUserRechargeMapper.getYxUserRechargeById(id);
    }

    @Override
    public Paging<YxUserRechargeQueryVo> getYxUserRechargePageList(YxUserRechargeQueryParam yxUserRechargeQueryParam) throws Exception{
        Page page = setPageParam(yxUserRechargeQueryParam,OrderItem.desc("create_time"));
        IPage<YxUserRechargeQueryVo> iPage = yxUserRechargeMapper.getYxUserRechargePageList(page,yxUserRechargeQueryParam);
        return new Paging(iPage);
    }

}
