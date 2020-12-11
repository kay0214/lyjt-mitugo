package co.yixiang.modules.order.web.param;

import co.yixiang.common.web.param.BaseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 卡券订单表 查询参数对象
 * </p>
 *
 * @author zqq
 * @date 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponOrderQueryParam对象", description="卡券订单表查询参数")
public class OrderStatusQueryParam extends BaseParam {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "订单号不可为空")
    @ApiModelProperty(value = "订单号")
    private String orderId;
}
