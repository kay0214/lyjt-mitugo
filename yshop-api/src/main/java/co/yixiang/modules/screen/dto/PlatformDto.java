package co.yixiang.modules.screen.dto;

import co.yixiang.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@ApiModel(value="平台dto", description="平台dto")
public class PlatformDto implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "今天时间")
    private String nowDate = DateUtils.getDate();

    @ApiModelProperty(value = "线下支付金额")
    private BigDecimal todayOffPay;

    @ApiModelProperty(value = "线上支付金额")
    private BigDecimal todayOnlinePay;

    @ApiModelProperty(value = "线下支付笔数")
    private Long  todayOffPayCount;

    @ApiModelProperty(value = "线上支付笔数")
    private Long todayOnlinePayCount;

    @ApiModelProperty(value = "所有商户数量")
    private Integer allMer;

    @ApiModelProperty(value = "认证通过商户数量")
    private Integer okMer;

    @ApiModelProperty(value = "用户数量")
    private Integer allUser;

    @ApiModelProperty(value = "分销用户数量")
    private Integer fxUser;

    @ApiModelProperty(value = "本地生活商品数量")
    private Integer localProduct;

    @ApiModelProperty(value = "本地生活商品数量")
    private Integer product;

    
}