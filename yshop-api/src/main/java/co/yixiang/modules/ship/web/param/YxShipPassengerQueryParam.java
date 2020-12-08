package co.yixiang.modules.ship.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 本地生活帆船订单乘客表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipPassengerQueryParam对象", description="本地生活帆船订单乘客表查询参数")
public class YxShipPassengerQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
