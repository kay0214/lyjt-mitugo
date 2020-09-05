package co.yixiang.modules.offpay.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 线下支付
 * @Author : sss
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="线下支付", description="线下支付")
public class OffPayQueryParam extends QueryParam {

    @ApiModelProperty(value = "用户打开小程序获取到的参数")
    @NotBlank(message = "参数错误")
    private String storeNid;
    @ApiModelProperty(value = "调用userPay的随机串")
    private String payRand;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal price;

}
