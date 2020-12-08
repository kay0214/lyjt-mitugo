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
public class ShipOperationParam extends QueryParam {
    @ApiModelProperty(value = "船只系列id")
    private Integer seriesId;
    @ApiModelProperty(value = "日期筛选标示 1:今日，2：近七天，3：近一个月")
    private String dateStatus;
}
