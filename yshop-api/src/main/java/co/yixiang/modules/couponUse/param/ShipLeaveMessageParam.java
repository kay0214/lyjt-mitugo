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
@ApiModel(value="ShipLeaveMessageParam对象", description="ShipLeaveMessageParam对象")
public class ShipLeaveMessageParam extends QueryParam {
    @ApiModelProperty(value = "日期筛选标识 1:今日，2：近七天，3：近一个月")
    private Integer dateStatus;
    @ApiModelProperty(value = "状态 0 -> 待处理，1 -> 已处理，2 -> 不予处理")
    private Integer status;

}
