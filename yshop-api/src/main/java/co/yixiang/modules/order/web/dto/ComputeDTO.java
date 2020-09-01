package co.yixiang.modules.order.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ComputeDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class ComputeDTO implements Serializable {
    @ApiModelProperty(value = "优惠金额")
    private Double couponPrice;
    @ApiModelProperty(value = "折扣金额")
    private Double deductionPrice;
    @ApiModelProperty(value = "支付邮费")
    private Double payPostage;
    @ApiModelProperty(value = "实际支付")
    private Double payPrice;
    @ApiModelProperty(value = "总金额")
    private Double totalPrice;
}
