package co.yixiang.common.rocketmq;

import co.yixiang.common.rocketmq.service.CommissionService;
import co.yixiang.constant.MQConstant;
import co.yixiang.constant.ShopConstants;
import co.yixiang.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分佣消费端
 */
@Service
@Slf4j
@RocketMQMessageListener(topic = MQConstant.MITU_TOPIC, selectorExpression = MQConstant.MITU_COMMISSION_TAG, consumerGroup = MQConstant.MITU_COMMISSION_GROUP)
public class CommissionConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    private static int MAX_RECONSUME_TIME = 3;

    @Autowired
    CommissionService commissionService;

    @Override
    public void onMessage(String message) {
        JSONObject callBackResult = JSONObject.parseObject(message);
        String orderId = callBackResult.getString("orderId");
        String orderType = callBackResult.getString("orderType");
        log.info("分佣订单号：{},订单类型:{}", orderId,orderType);
        String value = RedisUtil.get(ShopConstants.COMMISSION_ORDER + orderType + orderId);
        if (null != value) {
            log.info("订单重复分佣，订单类型:{},订单号：{}", orderType, orderId);
            return;
        }
        RedisUtil.set(ShopConstants.COMMISSION_ORDER + orderType + orderId, 1, 5);
        commissionService.updateInfo(orderId,orderType);

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
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        defaultMQPushConsumer.setConsumeThreadMax(1);
        defaultMQPushConsumer.setConsumeThreadMin(1);
        log.info("====commission consumer=====");
    }
}
