package co.yixiang.modules.couponUse.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 船只表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="ShipInfoChangeParam对象", description="ShipInfoQueryParam对象")
public class ShipInfoChangeParam {
    @ApiModelProperty(value = "船只id")
    private Integer shipId;
    @ApiModelProperty(value = "船只状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;

}
