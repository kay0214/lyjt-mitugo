package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 卡券订单表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponOrderQueryParam对象", description="卡券订单表查询参数")
public class YxCouponOrderPassengParam extends QueryParam {

    @ApiModelProperty(value = "订单号")
    private Integer orderId;
}
