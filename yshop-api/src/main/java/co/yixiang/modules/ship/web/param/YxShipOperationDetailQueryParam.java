package co.yixiang.modules.ship.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 船只运营记录详情 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipOperationDetailQueryParam对象", description="船只运营记录详情查询参数")
public class YxShipOperationDetailQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "船只出港批次号")
    private String batchNo;
}
