/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现信息
 */
@Data
public class WithdrawInfo {

    private Integer id;

    @ApiModelProperty(value = "交易流水号")
    private String seqNo;

    @ApiModelProperty(value = "提现金额")
    private String totalAmount;

    @ApiModelProperty(value = "收款方银行帐号")
    private String payeeNo ;

    @ApiModelProperty(value = "收款方户名")
    private String payeeName;

    @ApiModelProperty(value = "是否对公账户")
    private Integer payerAccttype;

    @ApiModelProperty(value = "银行联行号")
    private String bankCode;

    @ApiModelProperty(value = "对公账户所属行")
    private String bankName ;

    @ApiModelProperty(value = "银行预留手机号")
    private String payeeMobile ;

    @ApiModelProperty(value = "添加时间")
    private String addTime;



}
