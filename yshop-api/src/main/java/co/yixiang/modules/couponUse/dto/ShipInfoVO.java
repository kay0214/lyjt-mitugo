package co.yixiang.modules.couponUse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 所有船只
 * @author sss
 */
@Data
public class ShipInfoVO implements Serializable {


    @ApiModelProperty(value = "船只id")
    private Integer id;

    @ApiModelProperty(value = "船只名称")
    private String shipName;

    @ApiModelProperty(value = "船只系列id")
    private Integer seriesId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "所属商铺")
    private Integer storeId;

    @ApiModelProperty(value = "帆船所属商户名")
    private String merName;

    @ApiModelProperty(value = "帆船负责人")
    private String managerName;

    @ApiModelProperty(value = "负责人电话")
    private String managerPhone;

    @ApiModelProperty(value = "船只当前状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;

    @ApiModelProperty(value = "最近一次出港时间")
    private Integer lastLeaveTime;

    @ApiModelProperty(value = "最近一次返港时间")
    private Integer lastReturnTime;

}
