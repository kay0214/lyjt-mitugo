/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.tools.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.tools.domain.AlipayConfig;
import co.yixiang.tools.domain.vo.TradeVo;

/**
* @author hupeng
* @date 2020-05-13
*/
public interface AlipayConfigService  extends BaseService<AlipayConfig>{

    /**
     * 处理来自PC的交易请求
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 查询配置
     * @return AlipayConfig
     */
    AlipayConfig find();

    /**
     * 更新配置
     * @param alipayConfig 支付宝配置
     * @return AlipayConfig
     */
    void update(AlipayConfig alipayConfig);
}
