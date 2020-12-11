package co.yixiang.modules.order.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class YxCouponOrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
