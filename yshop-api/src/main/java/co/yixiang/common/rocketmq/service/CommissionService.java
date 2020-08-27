/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service;

/**
 * @author zhangqingqing
 * @version CommissionService, v0.1 2020/8/27 14:34
 */
public interface CommissionService {

    /**
     * 分佣
     * @param orderId
     * @param orderType
     */
    void updateInfo(String orderId, String orderType);
}
