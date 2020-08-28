package co.yixiang.common.rocketmq;

import com.hyjf.framework.starter.recketmq.MQException;
import com.hyjf.framework.starter.recketmq.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName 生成者
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/1/1
 **/
@Component
@Slf4j
public class MqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 发送普通队列通用方法
     * @param messageContent
     * @return
     * @throws MQException
     */
    public boolean messageSend(MessageContent messageContent) throws MQException {
        log.debug("普通队列mq消息发送, messageContent is: {}", messageContent);
        try {
            SendResult sendResult = null;
            if(StringUtils.isNotEmpty(messageContent.keys)) {
                org.springframework.messaging.Message<?> message = MessageBuilder.withPayload(messageContent.body).setHeader(MessageConst.PROPERTY_KEYS, messageContent.keys).build();
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, message);
            }else {
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, messageContent.body);
            }
            if (sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("通用发消息异常", e);
            throw new MQException("mq send error", e);
        }
    }

    /**
     * 发送普通队列通用方法(异常已处理)
     * @param messageContent
     * @return
     */
    public boolean messageSend2(MessageContent messageContent) {
        log.debug("普通队列mq消息发送, messageContent is: {}", messageContent);
        try {
            SendResult sendResult = null;
            if(StringUtils.isNotEmpty(messageContent.keys)) {
                org.springframework.messaging.Message<?> message = MessageBuilder.withPayload(messageContent.body).setHeader(MessageConst.PROPERTY_KEYS, messageContent.keys).build();
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, message);
            }else {
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, messageContent.body);
            }
            if (sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("通用发消息异常" + "。 Topic:" +messageContent.topic+ "，Tag:" +messageContent.tag+ "，Message:" + messageContent.body, e);
            return false;
        }
    }

    /**
     * 发送延时队列通用方法
     *
     * @param messageContent
     * @param delayLevel
     * @return
     * @throws MQException
     */
    public boolean messageSendDelay(MessageContent messageContent, int delayLevel) throws MQException {
        try {
            org.springframework.messaging.Message<?> message = MessageBuilder.withPayload(messageContent.body).setHeader(MessageConst.PROPERTY_KEYS, messageContent.keys).build();
            SendResult sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, message, rocketMQTemplate.getProducer().getSendMsgTimeout(), delayLevel);

            if (sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("通用延时消息发送异常", e);
            throw new MQException("mq send error", e);
        }
    }

    /**
     * 发送延时队列通用方法(异常已处理)
     * @param messageContent
     * @param delayLevel
     * @return
     */
    public boolean messageSendDelay2(MessageContent messageContent, int delayLevel) {
        try {
            org.springframework.messaging.Message<?> message = MessageBuilder.withPayload(messageContent.body).setHeader(MessageConst.PROPERTY_KEYS, messageContent.keys).build();
            SendResult sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, message, rocketMQTemplate.getProducer().getSendMsgTimeout(), delayLevel);

            if (sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("通用延时消息发送异常" + "。 Topic:" +messageContent.topic+ "，Tag:" +messageContent.tag+ "，Message:" + messageContent.body, e);
            return false;
        }
    }

}
