package co.yixiang.common.rocketmq;

import co.yixiang.common.rocketmq.service.WithdrawService;
import co.yixiang.constant.MQConstant;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 提现消费端
 */
@Service
@Slf4j
@RocketMQMessageListener(topic = MQConstant.MITU_TOPIC, selectorExpression = MQConstant.MITU_WITHDRAW_TAG, consumerGroup = MQConstant.MITU_WITHDRAW_GROUP)
public class WithdrawConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    private static int MAX_RECONSUME_TIME = 3;

    @Value("${yshop.isProd}")
    private boolean isProd;

    @Autowired
    WithdrawService withdrawService;

    @Override
    public void onMessage(String message) {
        JSONObject callBackResult = JSONObject.parseObject(message);
        String id = callBackResult.getString("id");
        if (!isProd) {
            log.info("测试环境提现MITU_WITHDRAW_TAG不执行" + id);
            return;
        }
        log.info("提现id：{}", id);
        if (StringUtils.isBlank(id)) {
            log.info("提现id为空：{}");
            return;
        }
        withdrawService.updateWithdraw(id);
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
        log.info("====commission consumer=====");
    }
}
