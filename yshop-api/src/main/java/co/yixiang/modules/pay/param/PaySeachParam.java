package co.yixiang.modules.pay.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PaySeachParam implements Serializable {

    private Integer id;

    @ApiModelProperty(value = "交易流水号")
    private String seqNo;

    @ApiModelProperty(value = "提现金额")
    private String totalAmount;

    @ApiModelProperty(value = "收款方银行帐号")
    private String payeeNo ;

    @ApiModelProperty(value = "收款方户名")
    private String payeeName;

    @ApiModelProperty(value = "添加时间")
    private String addTime;

}
