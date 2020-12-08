package co.yixiang.modules.couponUse.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船只表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInOperationParam对象", description="ShipInOperationParam对象")
public class ShipOutInParam extends QueryParam {
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    @ApiModelProperty(value = "状态：1：出港，2：回港")
    private Integer status;
}
