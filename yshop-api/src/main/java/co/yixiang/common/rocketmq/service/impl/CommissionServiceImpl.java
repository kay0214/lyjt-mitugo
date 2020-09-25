/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service.impl;

import co.yixiang.common.rocketmq.entity.OrderInfo;
import co.yixiang.common.rocketmq.service.CommissionService;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.BillDetailEnum;
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
import co.yixiang.modules.user.entity.YxPointDetail;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
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
        switch (orderType){
            case "0":
                //商品购买
                updateOrderInfo(orderId);
                break;
            case "1":
                //本地生活
                updateCouponInfo(orderId);
                break;
            default:
                log.info("订单类型错误，类型为：{}", orderType);
                break;

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
        //购物车列表
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
            orderInfo.setUsername(yxUser.getNickname());
            orderInfo.setCommission(yxStoreCart.getCommission());
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
        orderInfo.setUsername(yxUser.getNickname());
        orderInfo.setCommission(yxCouponOrder.getCommission());
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
        BigDecimal allBonus = new BigDecimal("0");

        //推荐人
        if (null != orderInfo.getParentId() && orderInfo.getParentType() == 3 && 0 != orderInfo.getParentId()) {
            BigDecimal parentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getParentRate()).setScale(2, BigDecimal.ROUND_DOWN);
            YxUser yxUser = yxUserMapper.selectById(orderInfo.getParentId());
            if(parentBonus.compareTo(BigDecimal.ZERO)>0){
                BigDecimal oldMoney = yxUser.getNowMoney();
                yxUserMapper.updateUserMoney(parentBonus, oldMoney, yxUser.getUid());

                insertBill(orderInfo, orderInfo.getParentId(), BillDetailEnum.TYPE_2.getValue(), parentBonus, yxUser.getNickname(), yxUser.getNowMoney(), 3, "推荐人分佣");
            }

            //拉新池
            BigDecimal referencePoint = orderInfo.getCommission().multiply(yxCommissionRate.getReferenceRate()).setScale(2, BigDecimal.ROUND_DOWN);
            if(referencePoint.compareTo(BigDecimal.ZERO)>0){
                yxFundsAccount = updatePullNewPoint(orderInfo, referencePoint, yxFundsAccount);
                insertBill(orderInfo, orderInfo.getParentId(), BillDetailEnum.TYPE_12.getValue(), referencePoint, yxUser.getNickname(), yxFundsAccount.getReferencePoint(), 0, "拉新分佣");

            }
            allBonus = allBonus.add(parentBonus).add(referencePoint);
        }

        //分享人
        if (null != orderInfo.getShareId()&&orderInfo.getShareId() != 0) {
            BigDecimal shareBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareRate()).setScale(2, BigDecimal.ROUND_DOWN);
            if(shareBonus.compareTo(BigDecimal.ZERO)>0){
                allBonus = allBonus.add(shareBonus);

                YxUser yxUser = yxUserMapper.selectById(orderInfo.getShareId());
                BigDecimal oldMoney = yxUser.getNowMoney();
                //更新佣金金额
                yxUserMapper.updateUserMoney(shareBonus, oldMoney, yxUser.getUid());

                insertBill(orderInfo, orderInfo.getShareId(), BillDetailEnum.TYPE_2.getValue(), shareBonus, yxUser.getNickname(), yxUser.getNowMoney(), 3, "分享人分佣");
            }
        }

        //分享人推荐人
        if (null != orderInfo.getShareParentId() && orderInfo.getShareParentType() == 3 && 0 != orderInfo.getShareParentId()) {
            BigDecimal shareParentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareParentRate()).setScale(2, BigDecimal.ROUND_DOWN);
            if(shareParentBonus.compareTo(BigDecimal.ZERO)>0){
                allBonus = allBonus.add(shareParentBonus);

                YxUser yxUser = yxUserMapper.selectById(orderInfo.getShareParentId());
                BigDecimal oldMoney = yxUser.getNowMoney();
                //更新佣金金额
                yxUser.setNowMoney(shareParentBonus);
                yxUser.setBrokeragePrice(shareParentBonus);
                yxUserMapper.updateUserMoney(shareParentBonus, oldMoney, yxUser.getUid());

                insertBill(orderInfo, orderInfo.getShareParentId(), BillDetailEnum.TYPE_2.getValue(), shareParentBonus, yxUser.getNickname(), yxUser.getNowMoney(), 3, "分享人推荐人分佣");
            }

        }

        //商户、合伙人积分(分红池)
        if (null != orderInfo.getMerId() && orderInfo.getMerId() != 0) {
            //商户收益
            BigDecimal merchantsPoint = orderInfo.getCommission().multiply(yxCommissionRate.getMerRate()).setScale(2, BigDecimal.ROUND_DOWN);
            //合伙人收益
            BigDecimal partnerPoint = orderInfo.getCommission().multiply(yxCommissionRate.getPartnerRate()).setScale(2, BigDecimal.ROUND_DOWN);
            yxFundsAccount = updateDividendPoint(orderInfo, merchantsPoint, partnerPoint, yxFundsAccount);
            allBonus = allBonus.add(merchantsPoint).add(partnerPoint);
        }

        //平台
        BigDecimal fundsBonus = orderInfo.getCommission().subtract(allBonus);
        if(fundsBonus.compareTo(BigDecimal.ZERO)>0){
            insertBill(orderInfo, 0, BillDetailEnum.TYPE_2.getValue(), fundsBonus, "平台账户", BigDecimal.ZERO, 4, "平台账户分佣");
        }
        yxFundsAccountMapper.updateFundsAccount(fundsBonus, yxFundsAccount.getId(),yxFundsAccount.getBonusPoint(),yxFundsAccount.getReferencePoint());

    }

    public YxFundsAccount updatePullNewPoint(OrderInfo orderInfo, BigDecimal referencePoint, YxFundsAccount yxFundsAccount) {

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
     * @param merchantsPoint
     * @param partnerPoint
     * @param yxFundsAccount
     * @return
     */
    public YxFundsAccount updateDividendPoint(OrderInfo orderInfo, BigDecimal merchantsPoint, BigDecimal partnerPoint, YxFundsAccount yxFundsAccount) {
        //商户
        SystemUser merInfo = systemUserMapper.selectById(orderInfo.getMerId());
        BigDecimal oldTotal = merInfo.getTotalScore();

        if(merchantsPoint.compareTo(BigDecimal.ZERO)>0){
            insertPointDetail(orderInfo, merchantsPoint, merInfo.getParentId(), partnerPoint, 1);
            merInfo.setTotalScore(merchantsPoint);
            systemUserMapper.updateTotalScore(merchantsPoint, oldTotal, merInfo.getId());
            //插入明细数据(商户)
            insertBill(orderInfo, orderInfo.getMerId(), BillDetailEnum.TYPE_11.getValue(), merchantsPoint, merInfo.getNickName(), merInfo.getTotalScore(), 1, "商户分佣");
        }

        if(partnerPoint.compareTo(BigDecimal.ZERO)>0){
            //合伙人
            SystemUser partnerInfo = systemUserMapper.selectById(merInfo.getParentId());
            oldTotal = partnerInfo.getTotalScore();
            partnerInfo.setTotalScore(partnerPoint);
            systemUserMapper.updateTotalScore(partnerPoint, oldTotal, partnerInfo.getId());
            //插入明细数据(合伙人)
            insertBill(orderInfo, merInfo.getParentId(), BillDetailEnum.TYPE_11.getValue(), partnerPoint, partnerInfo.getNickName(), partnerInfo.getTotalScore(), 2, "合伙人分佣");
        }
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
    public void insertBill(OrderInfo orderInfo, Integer uid, String type, BigDecimal parentBonus, String userName, BigDecimal nowMoney, Integer userType, String title) {
        //插入明细数据
        YxUserBill yxUserBill = new YxUserBill();
        yxUserBill.setUid(uid);
        yxUserBill.setLinkId(orderInfo.getOrderId());
        yxUserBill.setUsername(userName);
        yxUserBill.setPm(1);
        yxUserBill.setTitle(title);
        yxUserBill.setCategory(userType == 3||userType == 4 ? BillDetailEnum.CATEGORY_1.getValue() : BillDetailEnum.CATEGORY_2.getValue());
        yxUserBill.setType(type);
        yxUserBill.setBrokerageType(orderInfo.getBrokerageType());
        yxUserBill.setNumber(parentBonus);
        yxUserBill.setBalance(nowMoney.add(parentBonus));
        yxUserBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserBill.setStatus(1);
        yxUserBill.setUserType(userType);
        yxUserBillMapper.insert(yxUserBill);
    }
}