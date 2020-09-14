/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service;

/**
 * 提现操作
 */
public interface WithdrawService {

    /**
     * 调用银行进行提现操作
     * @param id
     */
    void updateWithdraw(String id);
}
