package co.yixiang.modules.ship.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 船只运营记录 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipOperationQueryParam对象", description="船只运营记录查询参数")
public class YxShipOperationQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "船长id")
    private Integer captainId;
    @ApiModelProperty(value = "船只id")
    private List<Integer> shipIdList;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "船只状态 0:待出港 1：出港 2：回港")
    private Integer status;
}
