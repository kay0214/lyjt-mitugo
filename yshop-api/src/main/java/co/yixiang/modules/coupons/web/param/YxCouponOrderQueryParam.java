package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

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
public class YxCouponOrderQueryParam extends QueryParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "检索类型 1待付款  2待使用 3已使用  4已过期  5退款售后")
    private Integer type = 1;
}
