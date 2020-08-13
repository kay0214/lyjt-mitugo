package co.yixiang.modules.coupons.web.param;

import io.swagger.annotations.ApiModel;
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
}
