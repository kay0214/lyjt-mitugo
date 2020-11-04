package co.yixiang.modules.ship.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 船只预约表详情 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxShipAppointDetailQueryVo对象", description="船只预约表详情查询参数")
public class YxShipAppointDetailQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
private Integer id;

@ApiModelProperty(value = "船只预约表id")
private Integer appointId;

@ApiModelProperty(value = "船只系列id")
private Integer seriesId;

@ApiModelProperty(value = "所选船只id")
private Integer shipId;

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

}