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
@ApiModel(value="ShipInAppotionDaysParam对象", description="ShipInAppotionDaysParam对象")
public class ShipInAppotionDaysParam {
    @ApiModelProperty(value = "船只id")
    private Integer shipId;
    @ApiModelProperty(value = "日期")
    private String date;
}
