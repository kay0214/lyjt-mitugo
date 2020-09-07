package co.yixiang.modules.offpay.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 线下支付订单表 查询参数对象
 * </p>
 *
 * @author sss
 * @date 2020-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxOffPayOrderQueryParam对象", description="线下支付订单表查询参数")
public class YxOffPayOrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
