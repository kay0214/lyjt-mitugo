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
@ApiModel(value="ShipAppointParam对象", description="ShipInfoQueryParam对象")
public class ShipAppointParam {
    @ApiModelProperty(value = "船只id")
    private Integer shipId;
    @ApiModelProperty(value = "当前月份")
    private String currenDate;
}
