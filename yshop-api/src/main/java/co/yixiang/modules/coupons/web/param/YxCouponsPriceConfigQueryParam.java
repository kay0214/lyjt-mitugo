package co.yixiang.modules.coupons.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 价格配置 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponsPriceConfigQueryParam对象", description="价格配置查询参数")
public class YxCouponsPriceConfigQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
