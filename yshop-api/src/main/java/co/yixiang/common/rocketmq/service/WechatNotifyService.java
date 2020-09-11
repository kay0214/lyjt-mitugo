/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service;

/**
 * @author PC-LIUSHOUYI
 * @version WechatNotifyService, v0.1 2020/9/11 17:11
 */
public interface WechatNotifyService {
    /**
     * 处理支付异步回调
     *
     * @param orderId
     * @param attach
     */
    void updateNotifyStatus(String orderId, String attach);
}
