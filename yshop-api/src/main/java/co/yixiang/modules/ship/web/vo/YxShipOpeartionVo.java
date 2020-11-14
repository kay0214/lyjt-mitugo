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
@ApiModel(value = "YxShipOpeartionVo对象", description = "船只运营记录")
public class YxShipOpeartionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "船只出港批次号")
    private String batchNo;

    @ApiModelProperty(value = "船只id")
    private Integer shipId;

    @ApiModelProperty(value = "船只名称")
    private String shipName;

    @ApiModelProperty(value = "船长id")
    private Integer captainId;

    @ApiModelProperty(value = "船长姓名")
    private String captainName;

    @ApiModelProperty(value = "承载人数")
    private Integer totalPassenger;

    @ApiModelProperty(value = "老年人人数")
    private Integer oldPassenger;

    @ApiModelProperty(value = "未成年人数")
    private Integer underagePassenger;

    @ApiModelProperty(value = "出港时间")
    private Integer leaveTime;

    @ApiModelProperty(value = "回港时间")
    private Integer returnTime;

    @ApiModelProperty(value = "船只状态 0:待出港 1：出港 2：回港")
    private Integer status;

    @ApiModelProperty(value = "船只状态（显示）")
    private String statusFormat;

    @ApiModelProperty(value = "出港时间（显示）")
    private String leaveTimeFormat;

    @ApiModelProperty(value = "回港时间（显示）")
    private String returnTimeFormat;

    @ApiModelProperty(value = "系列名称")
    private String seriesName;
    @ApiModelProperty(value = "船只图片")
    private String shipImageUrl;
    @ApiModelProperty(value = "核销时间")
    private String userdTime;
    @ApiModelProperty(value = "核销人员")
    private String userdUserName;
}