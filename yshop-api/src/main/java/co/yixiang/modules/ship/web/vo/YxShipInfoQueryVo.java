package co.yixiang.modules.ship.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 船只表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value = "YxShipInfoQueryVo对象", description = "船只表查询参数")
public class YxShipInfoQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "船只状态：0：启用，1：禁用")
    private Integer shipStatus;

    @ApiModelProperty(value = "船只当前状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;

    @ApiModelProperty(value = "最近一次出港时间")
    private Integer lastLeaveTime;

    @ApiModelProperty(value = "最近一次返港时间")
    private Integer lastReturnTime;

    @ApiModelProperty(value = "船只当前状态（显示）")
    private String currentStatusFormat;

    @ApiModelProperty(value = "最近一次出港时间（显示）")
    private String lastLeaveTimeFormat;

    @ApiModelProperty(value = "最近一次返港时间（显示）")
    private String lastReturnTimeFormat;
    @ApiModelProperty(value = "系列名称")
    private String seriesName;
    @ApiModelProperty(value = "船只图片")
    private String shipImageUrl;

/*
@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;
*/

}