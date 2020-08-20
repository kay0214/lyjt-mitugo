package co.yixiang.common.rocketmq;

import co.yixiang.common.constant.MQConstant;
import co.yixiang.common.rocketmq.entity.OrderInfo;
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
import co.yixiang.modules.user.entity.YxFundsDetail;
import co.yixiang.modules.user.entity.YxPointDetail;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.mapper.YxFundsDetailMapper;
import co.yixiang.modules.user.mapper.YxPointDetailMapper;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapper.YxWechatUserMapper;
import co.yixiang.utils.OrderUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 短信消费端
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RocketMQMessageListener(topic = MQConstant.MITU_TOPIC, selectorExpression = MQConstant.MITU_COMMISSION_TAG, consumerGroup = MQConstant.MITU_COMMISSION_GROUP)
public class CommissionConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    private static int MAX_RECONSUME_TIME = 3;

    @Autowired
    YxStoreOrderMapper yxStoreOrderMapper;

    @Autowired
    YxCommissionRateMapper yxCommissionRateMapper;

    @Autowired
    YxWechatUserMapper yxWechatUserMapper;

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

    @Override
    public void onMessage(String message) {
        JSONObject callBackResult = JSONObject.parseObject(message);
        String orderId = callBackResult.getString("orderId");
        String orderType = callBackResult.getString("orderType");

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
    }

    /**
     * 商品购买分佣
     *
     * @param OrderId
     */
    private void updateOrderInfo(String OrderId) {
        //根据订单号查询订单信息
        YxStoreOrder yxStoreOrder = yxStoreOrderMapper.selectOne(new QueryWrapper<YxStoreOrder>().lambda().eq(YxStoreOrder::getOrderId, OrderId));
        if (yxStoreOrder.getRebateStatus() == 1) {
            log.info("分佣失败，该订单重复分佣,订单号：{}", OrderId);
            return;
        }
        yxStoreOrder.setRebateStatus(1);
        yxStoreOrderMapper.updateById(yxStoreOrder);
        if (yxStoreOrder.getCommission().compareTo(BigDecimal.ZERO)<=0) {
            log.info("分佣失败，该订单可分佣金额为0,订单号：{}", OrderId);
            return;
        }
        OrderInfo orderInfo = new OrderInfo();
        YxWechatUser yxWechatUser = yxWechatUserMapper.selectById(yxStoreOrder.getUid());
        BeanUtils.copyProperties(yxStoreOrder, orderInfo);
        orderInfo.setUsername(yxWechatUser.getNickname());
        updateaccount(orderInfo);
    }

    /**
     * 本地生活分佣
     *
     * @param OrderId
     */
    private void updateCouponInfo(String OrderId) {
        YxCouponOrder yxCouponOrder = yxCouponOrderMapper.selectOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId, OrderId));
        if (yxCouponOrder.getRebateStatus().equals(1)) {
            log.info("分佣失败，该订单重复分佣,订单号：{}", OrderId);
            return;
        }
        if (yxCouponOrder.getCommission().compareTo(BigDecimal.ZERO)<=0) {
            log.info("分佣失败，该订单可分佣金额为0,订单号：{}", OrderId);
            return;
        }
        yxCouponOrder.setRebateStatus(1);
        yxCouponOrderMapper.updateById(yxCouponOrder);
        YxWechatUser yxWechatUser = yxWechatUserMapper.selectById(yxCouponOrder.getUid());
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(yxCouponOrder, orderInfo);
        orderInfo.setPayPrice(yxCouponOrder.getCouponPrice());
        orderInfo.setUsername(yxWechatUser.getNickname());
        updateaccount(orderInfo);
    }

    /**
     * 更新账户信息
     *
     * @param orderInfo
     */
    private void updateaccount(OrderInfo orderInfo) {
        YxFundsAccount yxFundsAccount = yxFundsAccountMapper.selectById(1);
        //查询分佣比例
        YxCommissionRate yxCommissionRate = yxCommissionRateMapper.selectOne(new QueryWrapper<>());
        //平台抽成
        BigDecimal fundsRate = yxCommissionRate.getFundsRate();
        //推荐人
        if (null != orderInfo.getParentId() && orderInfo.getParentType() == 3) {
            BigDecimal parentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getParentRate());
            //获取用户信息
            YxWechatUser yxWechatUser = yxWechatUserMapper.selectById(orderInfo.getParentId());
            //更新佣金金额
            yxWechatUser.setNowMoney(yxWechatUser.getNowMoney().add(parentBonus));
            yxWechatUserMapper.updateById(yxWechatUser);
            insertBill(orderInfo.getParentId(), parentBonus, yxWechatUser);
        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getParentRate());
        }
        //分享人
        if (null != orderInfo.getShareId() && orderInfo.getShareId() == 3) {
            BigDecimal shareBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareRate());
            //获取用户信息
            YxWechatUser yxWechatUser = yxWechatUserMapper.selectById(orderInfo.getShareId());
            //更新佣金金额
            yxWechatUser.setNowMoney(yxWechatUser.getNowMoney().add(shareBonus));
            yxWechatUserMapper.updateById(yxWechatUser);
            insertBill(orderInfo.getShareId(), shareBonus, yxWechatUser);

        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getShareRate());
        }
        //分享人推荐人
        if (null != orderInfo.getShareParentId() && orderInfo.getShareParentType() == 3) {
            BigDecimal shareParentBonus = orderInfo.getCommission().multiply(yxCommissionRate.getShareParentRate());
            //获取用户信息
            YxWechatUser yxWechatUser = yxWechatUserMapper.selectById(orderInfo.getShareParentId());
            //更新佣金金额
            yxWechatUser.setNowMoney(yxWechatUser.getNowMoney().add(shareParentBonus));
            yxWechatUserMapper.updateById(yxWechatUser);
            insertBill(orderInfo.getShareParentId(), shareParentBonus, yxWechatUser);
        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getShareParentRate());
        }
        //商户、合伙人积分
        if (null != orderInfo.getMerId() && orderInfo.getMerId() != 0) {
            //分红池
            yxFundsAccount = updatePoint(orderInfo, yxCommissionRate, yxFundsAccount, 1);
            //拉新池
            yxFundsAccount = updatePoint(orderInfo, yxCommissionRate, yxFundsAccount, 0);
        } else {
            fundsRate = fundsRate.add(yxCommissionRate.getMerRate().add(yxCommissionRate.getPartnerRate()));
        }
        //平台
        BigDecimal fundsBonus = orderInfo.getCommission().multiply(fundsRate);
        YxFundsDetail yxFundsDetail = new YxFundsDetail();
        yxFundsDetail.setType(1);
        yxFundsDetail.setUid(orderInfo.getUid());
        yxFundsDetail.setUsername(orderInfo.getUsername());
        yxFundsDetail.setOrderId(orderInfo.getOrderId());
        yxFundsDetail.setPm(1);
        yxFundsDetail.setOrderAmount(fundsBonus);
        yxFundsDetailMapper.insert(yxFundsDetail);
        yxFundsAccount.setPrice(yxFundsAccount.getPrice().add(fundsBonus));
        yxFundsAccountMapper.updateById(yxFundsAccount);
    }

    /**
     * 更新商户合伙人积分明细以及平台总积分
     *
     * @param orderInfo
     * @param yxCommissionRate
     * @param yxFundsAccount
     * @param type
     * @return
     */
    private YxFundsAccount updatePoint(OrderInfo orderInfo, YxCommissionRate yxCommissionRate, YxFundsAccount yxFundsAccount, Integer type) {
        BigDecimal merBonus = orderInfo.getCommission().multiply(yxCommissionRate.getMerRate());
        SystemUser merInfo = systemUserMapper.selectById(orderInfo.getMerId());
        YxPointDetail yxPointDetail = new YxPointDetail();
        yxPointDetail.setUid(orderInfo.getUid());
        yxPointDetail.setUsername(orderInfo.getUsername());
        yxPointDetail.setType(type);
        yxPointDetail.setOrderId(orderInfo.getOrderId());
        yxPointDetail.setOrderType(0);
        yxPointDetail.setOrderPrice(orderInfo.getPayPrice());
        yxPointDetail.setCommission(orderInfo.getCommission());
        yxPointDetail.setMerchantsId(orderInfo.getMerId());
        yxPointDetail.setMerchantsPoint(merBonus);
        //合伙人ID
        yxPointDetail.setPartnerId(merInfo.getParentId());
        //合伙人收益
        BigDecimal partnerBonus = orderInfo.getCommission().multiply(yxCommissionRate.getPartnerRate());
        yxPointDetail.setPartnerPoint(partnerBonus);
        yxPointDetailMapper.insert(yxPointDetail);

        merInfo.setTotalScore(merInfo.getTotalScore().add(merBonus));
        systemUserMapper.updateById(merInfo);

        SystemUser partnerInfo = systemUserMapper.selectById(merInfo.getParentId());
        partnerInfo.setTotalScore(partnerInfo.getTotalScore().add(partnerBonus));
        systemUserMapper.updateById(partnerInfo);
        //总积分
        BigDecimal totalPoint = yxFundsAccount.getBonusPoint().add(merBonus).add(partnerBonus);
        if (type == 1) {
            // TODO: 2020/8/20 是否将对应金额累积到平台总佣金中 
            //分红总积分
            yxFundsAccount.setBonusPoint(totalPoint);
        } else {
            //拉新总积分
            yxFundsAccount.setReferencePoint(totalPoint);
        }
        return yxFundsAccount;
    }

    /**
     * 插入用户资金明细
     *
     * @param uid
     * @param parentBonus
     * @param yxWechatUser
     */
    private void insertBill(Integer uid, BigDecimal parentBonus, YxWechatUser yxWechatUser) {
        //插入明细数据
        YxUserBill yxUserBill = new YxUserBill();
        yxUserBill.setUid(uid);
        yxUserBill.setUsername(yxWechatUser.getNickname());
        yxUserBill.setPm(1);
        yxUserBill.setTitle("推荐佣金");
        yxUserBill.setCategory("now_money");
        yxUserBill.setType("brokerage");
        yxUserBill.setNumber(parentBonus);
        yxUserBill.setBalance(yxWechatUser.getNowMoney().add(parentBonus));
        yxUserBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserBill.setStatus(1);
        yxUserBillMapper.insert(yxUserBill);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 设置为集群消费(区别于广播消费)
        // MQ默认集群消费
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        //设置最大重试次数
        defaultMQPushConsumer.setMaxReconsumeTimes(MAX_RECONSUME_TIME);
        log.info("====sms consumer=====");
    }
}
