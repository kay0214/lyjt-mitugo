package co.yixiang.modules.couponUse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 船只预约表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxShipAppointVo对象", description="船只预约返回参数")
public class YxShipAppointVo implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "预约时间")
    private String appointmentDate;

    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "船只名")
    private List<String> shipNameList;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;
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