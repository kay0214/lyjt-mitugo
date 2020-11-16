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
@ApiModel(value="ShipInAppotionParam对象", description="ShipInAppotionParam对象")
public class ShipInAppotionParam {

    @ApiModelProperty(value = "留言id")
    private Integer leaveId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "预约时间")
    private String appointmentDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "船只id，多个以逗号分隔")
    private String shipIds;
}
