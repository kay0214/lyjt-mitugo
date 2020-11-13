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
@ApiModel(value="ShipInfoQueryParam对象", description="ShipInfoQueryParam对象")
public class ShipInfoQueryParam extends QueryParam {
    @ApiModelProperty(value = "船只系列id")
    private Integer seriesId;
    /*@ApiModelProperty(value = "船只名称")
    private String shipName;*/
    @ApiModelProperty(value = "船只当前状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;
}
