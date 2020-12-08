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
@ApiModel(value="ShipInAppotionDaysParam对象", description="ShipInAppotionDaysParam对象")
public class ShipAppotionDaysParam extends QueryParam {
    @ApiModelProperty(value = "船只id")
    private Integer shipId;
    @ApiModelProperty(value = "日期")
    private String date;
}
