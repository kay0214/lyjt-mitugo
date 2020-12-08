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
@ApiModel(value="ShipCaptainModifyParam对象", description="ShipCaptainModifyParam对象")
public class ShipCaptainModifyParam {
    @ApiModelProperty(value = "船长id")
    private Integer captainId;
    @ApiModelProperty(value = "batchNo")
    private String batchNo;
}
