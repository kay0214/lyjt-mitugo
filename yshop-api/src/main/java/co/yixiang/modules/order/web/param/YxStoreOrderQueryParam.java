package co.yixiang.modules.order.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreOrderQueryParam对象", description = "订单表查询参数")
public class YxStoreOrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private Integer type = 1;
}
