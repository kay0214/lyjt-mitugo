/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service.impl;

import co.yixiang.common.rocketmq.entity.OrderInfo;
import co.yixiang.common.rocketmq.service.CommissionService;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.mapper.YxCommissionRateMapper;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.funds.entity.YxFundsAccount;
import co.yixiang.modules.funds.mapper.YxFundsAccountMapper;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.mapper.SystemUserMapper;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.mapper.YxStoreOrderMapper;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.mapper.YxStoreCartMapper;
import co.yixiang.modules.user.entity.YxFundsDetail;
import co.yixiang.modules.user.entity.YxPointDetail;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.mapper.YxFundsDetailMapper;
import co.yixiang.modules.user.mapper.YxPointDetailMapper;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapper.YxUserMapper;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangqingqing
 * @version CommissionServiceImpl, v0.1 2020/8/27 14:34
 */
@Slf4j
@Service
public class CommissionServiceImpl implements CommissionService {


    @Autowired
    YxStoreOrderMapper yxStoreOrderMapper;

    @Autowired
    YxCommissionRateMapper yxCommissionRateMapper;

    @Autowired
    YxUserMapper yxUserMapper;

    @Autowired
    YxUserBillMapper yxUserBillMapper;

    @Autowired
    SystemUserMapper systemUserMapper;

    @Autowired
    YxPointDetailMapper yxPointDetailMapper;

    @Autowired
    YxFundsDetailMapper yxFundsDetailMapper;

    @Autowired
    YxFundsAccountMapper yxFundsAccountMapper;

    @Autowired
    YxCouponOrderMapper yxCouponOrderMapper;

    @Autowired
    YxStoreCartMapper yxStoreCartMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateInfo(String orderId, String orderType) {
        String value = RedisUtil.get(ShopConstants.COMMISSION_ORDER + orderType + orderId);
        if (null != value) {
            log.info("订单重复分佣，订单类型:{},订单号：{}", orderType, orderId);
            return;
        }
        RedisUtil.set(ShopConstants.COMMISSION_ORDER + orderType + orderId, 1, 5);
        if (orderType.equals("0")) {
            //商品购买
            updateOrderInfo(orderId);

        } else if (orderType.equals("1")) {
            //本地生活
            updateCouponInfo(orderId);
        } else {
            log.info("订单类型错误，类型为：{}", orderType);
            return;
        }
        RedisUtil.del(ShopConstants.COMMISSION_ORDER + orderType + orderId);
    }

    /**
     * 商品购买分佣
     *
     * @param orderId
     */
    public void updateOrderInfo(String orderId) {
        //根据订单号查询订单信息
        YxStoreOrder yxStoreOrder = yxStoreOrderMapper.selectOne(new QueryWrapper<YxStoreOrder>().lambda().eq(YxStoreOrder::getOrderId, orderId));
        if (yxStoreOrder.getRebateStatus() == 1) {
            log.info("分佣失败，该订单重复分佣,订单号：{}", orderId);
            return;
        }
        yxStoreOrder.setRebateStatus(1);
        yxStoreOrderMapper.updateById(yxStoreOrder);
        String cartIds = yxStoreOrder.getCartId();
        if (StringUtils.isBlank(cartIds)) {
            log.info("分佣失败，该订单无可分佣商品,订单号：{}", orderId);
            return;
        }

        OrderInfo orderInfo = new OrderInfo();
        YxUser yxUser = yxUserMapper.selectById(yxStoreOrder.getUid());
        List<String> cartIdList = Arrays.asList(cartIds.split(","));
        for (String cartId : cartIdList) {
            YxStoreCart yxStoreCart = yxStoreCartMapper.selectById(Integer.parseInt(cartId));
            if (yxStoreCart.getCommission().compareTo(BigDecimal.ZERO) <= 0) {
                log.info("分佣失败，该商品可分佣金额为0,订单号：{}==>>>商品号：{}", orderId, cartId);
                continue;
            }
            BeanUtils.copyProperties(yxStoreCart, orderInfo);
            orderInfo.setOrderId(orderId);
            orderInfo.setBrokerageType(0);
            orderInfo.setCartId(cartId);
            orderInfo.setUsername(yxUser.getUsername());
            orderInfo.setCommission(yxStoreCart.getCommission().multiply(new BigDecimal(yxStoreCart.getCartNum().toString())));
            updateAccount(orderInfo);
        }
    }

    /**
     * 本地生活分佣
     *
     * @param orderId
     */
    public void updateCouponInfo(String orderId) {
        YxCouponOrder yxCouponOrder = yxCouponOrderMapper.selectOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId, orderId));
        if (yxCouponOrder.getRebateStatus().equals(1)) {
            log.info("分佣失败，该订单重复分佣,订单号：{}", orderId);
            return;
        }
        yxCouponOrder.setRebateStatus(1);
        yxCouponOrderMapper.updateById(yxCouponOrder);
        if (yxCouponOrder.getCommission().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("分佣失败，该订单可分佣金额为0,订单号：{}", orderId);
            return;
        }

        YxUser yxUser = yxUserMapper.selectById(yxCouponOrder.getUid());
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(yxCouponOrder, orderInfo);
        orderInfo.setBrokerageType(1);
        orderInfo.setPayPrice(yxCouponOrder.getCouponPrice());
        orderInfo.setUsername(yxUser.getUsername());
        orderInfo.setCommission(yxCouponOrder.getCommission().multiply(new BigDecimal(yxCouponOrder.getTotalNum().toString())));
        updateAccount(orderInfo);
    }

    /**
     * 更新账户信息
     *
     * @param orderInfo
     */
    public void updateAccount(OrderInfo orderInfo) {
        YxFundsAccount yxFundsAccount = yxFundsAccountMapper.selectById(1);
        //查询分佣比例
        YxCommissionRate yxCommissionRate = yxCommissionRateMapper.selectOne(new QueryWrapper<>());
        //平台抽成
        BigDecimal fundsRate = yxCommissionRate.getFundsRate();

        //推荐人
        if (null != orderInfo.getParentId() && orderInfo.getParentType() == 3) {
            BigDecimal parentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getParentRate());
            //获取用户信息
            YxUser yxUser = yxUserMapper.selectById(orderInfo.getParentId());
            //更新佣金金额
            //yxUser.setNowMoney(yxUser.getNowMoney().add(parentBonus));
            //yxUser.setBrokeragePrice(yxUser.getBrokeragePrice().add(parentBonus));
            yxUser.setNowMoney(parentBonus);
            yxUser.setBrokeragePrice(parentBonus);
            // 更新用户金额
            yxUserMapper.updateUserMoney(yxUser);
            //yxUserMapper.updateById(yxUser);
            insertBill(orderInfo.getOrderId(), orderInfo.getParentId(), orderInfo.getBrokerageType(), parentBonus, yxUser.getUsername(), yxUser.getNowMoney(), 1);
            //拉新池
            yxFundsAccount = updatePullNewPoint(orderInfo, yxCommissionRate, yxFundsAccount);
        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getParentRate()).add(yxCommissionRate.getReferenceRate());
        }

        //分享人
        if (null != orderInfo.getShareId() && orderInfo.getShareId() == 3) {
            BigDecimal shareBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareRate());
            //获取用户信息
            YxUser yxUser = yxUserMapper.selectById(orderInfo.getShareId());
            //更新佣金金额
            yxUser.setNowMoney(shareBonus);
            yxUser.setBrokeragePrice(shareBonus);
            //yxUserMapper.updateById(yxUser);
            yxUserMapper.updateUserMoney(yxUser);

            insertBill(orderInfo.getOrderId(), orderInfo.getShareId(), orderInfo.getBrokerageType(), shareBonus, yxUser.getUsername(), yxUser.getNowMoney(), 1);

        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getShareRate());
        }

        //分享人推荐人
        if (null != orderInfo.getShareParentId() && orderInfo.getShareParentType() == 3) {
            BigDecimal shareParentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareParentRate());
            //获取用户信息
            YxUser yxUser = yxUserMapper.selectById(orderInfo.getShareParentId());
            //更新佣金金额
            yxUser.setNowMoney(shareParentBonus);
            yxUser.setBrokeragePrice(shareParentBonus);
            //yxUserMapper.updateById(yxUser);
            yxUserMapper.updateUserMoney(yxUser);
            insertBill(orderInfo.getOrderId(), orderInfo.getShareParentId(), orderInfo.getBrokerageType(), shareParentBonus, yxUser.getUsername(), yxUser.getNowMoney(), orderInfo.getShareParentType());
        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getShareParentRate());
        }

        //商户、合伙人积分
        if (null != orderInfo.getMerId() && orderInfo.getMerId() != 0) {
            //分红池
            yxFundsAccount = updateDividendPoint(orderInfo, yxCommissionRate, yxFundsAccount);

        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getMerRate().add(yxCommissionRate.getPartnerRate()));
        }

        //平台
        BigDecimal fundsBonus = orderInfo.getCommission().multiply(fundsRate);
        YxFundsDetail yxFundsDetail = new YxFundsDetail();
        yxFundsDetail.setType(orderInfo.getBrokerageType() == 0 ? 1 : 2);
        yxFundsDetail.setUid(orderInfo.getUid());
        yxFundsDetail.setUsername(orderInfo.getUsername());
        yxFundsDetail.setOrderId(orderInfo.getOrderId());
        yxFundsDetail.setPm(1);
        yxFundsDetail.setOrderAmount(fundsBonus);
        yxFundsDetailMapper.insert(yxFundsDetail);
        yxFundsAccount.setPrice(yxFundsAccount.getPrice().add(fundsBonus));
        yxFundsAccountMapper.updateById(yxFundsAccount);
        // todo 改成sql+=那种
    }

    public YxFundsAccount updatePullNewPoint(OrderInfo orderInfo, YxCommissionRate yxCommissionRate, YxFundsAccount yxFundsAccount) {
        //拉新积分
        BigDecimal referencePoint = orderInfo.getCommission().multiply(yxCommissionRate.getReferenceRate());
        SystemUser merInfo = systemUserMapper.selectById(orderInfo.getMerId());
        insertPointDetail(orderInfo, referencePoint, merInfo.getParentId(), new BigDecimal("0"), 0);
        yxFundsAccount.setReferencePoint(yxFundsAccount.getReferencePoint().add(referencePoint));
        yxFundsAccount.setPrice(yxFundsAccount.getPrice().add(referencePoint));
        return yxFundsAccount;
    }


    /**
     * 更新商户合伙人积分明细以及平台总积分
     *
     * @param orderInfo
     * @param yxCommissionRate
     * @param yxFundsAccount
     * @return
     */
    public YxFundsAccount updateDividendPoint(OrderInfo orderInfo, YxCommissionRate yxCommissionRate, YxFundsAccount yxFundsAccount) {
        BigDecimal merchantsPoint = orderInfo.getCommission().multiply(yxCommissionRate.getMerRate());
        SystemUser merInfo = systemUserMapper.selectById(orderInfo.getMerId());
        //合伙人收益
        BigDecimal partnerPoint = orderInfo.getCommission().multiply(yxCommissionRate.getPartnerRate());
        insertPointDetail(orderInfo, merchantsPoint, merInfo.getParentId(), partnerPoint, 1);
        merInfo.setTotalScore(merInfo.getTotalScore().add(merchantsPoint));
        systemUserMapper.updateById(merInfo);

        SystemUser partnerInfo = systemUserMapper.selectById(merInfo.getParentId());
        partnerInfo.setTotalScore(partnerInfo.getTotalScore().add(partnerPoint));
        systemUserMapper.updateById(partnerInfo);
        //插入明细数据(商户)
        insertBill(orderInfo.getOrderId(), orderInfo.getMerId(), orderInfo.getBrokerageType(), merchantsPoint, merInfo.getUsername(), merInfo.getTotalScore(), 2);
        //插入明细数据(合伙人)
        insertBill(orderInfo.getOrderId(), merInfo.getParentId(), orderInfo.getBrokerageType(), partnerPoint, partnerInfo.getUsername(), partnerInfo.getTotalScore(), 3);
        BigDecimal totalPoint = yxFundsAccount.getBonusPoint().add(merchantsPoint).add(partnerPoint);
        //分红总积分
        yxFundsAccount.setBonusPoint(totalPoint);
        yxFundsAccount.setPrice(yxFundsAccount.getPrice().add(totalPoint));
        return yxFundsAccount;
    }

    /**
     * 插入积分明细
     *
     * @param orderInfo
     * @param merchantsPoint
     * @param parentId
     * @param partnerPoint
     * @param type
     */
    public void insertPointDetail(OrderInfo orderInfo, BigDecimal merchantsPoint, Integer parentId, BigDecimal partnerPoint, Integer type) {
        YxPointDetail yxPointDetail = new YxPointDetail();
        yxPointDetail.setUid(orderInfo.getUid());
        yxPointDetail.setUsername(orderInfo.getUsername());
        yxPointDetail.setType(type);
        yxPointDetail.setOrderId(orderInfo.getOrderId());
        yxPointDetail.setOrderType(orderInfo.getBrokerageType());
        yxPointDetail.setOrderPrice(orderInfo.getPayPrice());
        yxPointDetail.setCommission(orderInfo.getCommission());
        yxPointDetail.setMerchantsId(orderInfo.getMerId());
        yxPointDetail.setMerchantsPoint(merchantsPoint);
        yxPointDetail.setPartnerId(parentId);
        yxPointDetail.setPartnerPoint(partnerPoint);
        yxPointDetailMapper.insert(yxPointDetail);
    }


    /**
     * 插入用户资金明细
     *
     * @param uid
     * @param parentBonus
     * @param
     */
    public void insertBill(String orderId, Integer uid, Integer brokerageType, BigDecimal parentBonus, String userName, BigDecimal nowMoney, Integer userType) {
        //插入明细数据
        YxUserBill yxUserBill = new YxUserBill();
        yxUserBill.setUid(uid);
        yxUserBill.setLinkId(orderId);
        yxUserBill.setUsername(userName);
        yxUserBill.setPm(1);
        yxUserBill.setTitle("商品返佣");
        yxUserBill.setCategory(userType == 1 ? "now_money" : "integral");
        yxUserBill.setType("brokerage");
        yxUserBill.setBrokerageType(brokerageType);
        yxUserBill.setNumber(parentBonus);
        yxUserBill.setBalance(nowMoney.add(parentBonus));
        yxUserBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserBill.setStatus(1);
        yxUserBill.setUserType(userType);
        yxUserBillMapper.insert(yxUserBill);
    }
}