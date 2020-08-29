package co.yixiang.modules.coupons.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 卡券订单详情表 查询参数对象
 * </p>
 *
 * @author liusy
 * @date 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponOrderDetailQueryParam对象", description="卡券订单详情表查询参数")
public class YxCouponOrderDetailQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
