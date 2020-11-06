package co.yixiang.modules.ship.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 船只系列表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-06
 */
@Data
@ApiModel(value="YxShipSeriesQueryVo对象", description="船只系列表查询参数")
public class YxShipSeriesQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "系列id")
private Integer id;

@ApiModelProperty(value = "系列名称")
private String seriesName;

@ApiModelProperty(value = "所属商铺")
private Integer storeId;

@ApiModelProperty(value = "船只类别")
private Integer shipCategory;

@ApiModelProperty(value = "限乘人数")
private Integer rideLimit;

@ApiModelProperty(value = "尺寸")
private String shipSize;

@ApiModelProperty(value = "状态：0：启用，1：禁用")
private Integer status;

@ApiModelProperty(value = "乘船省市区")
private String shipProvince;

@ApiModelProperty(value = "乘船地址")
private String shipAddress;

@ApiModelProperty(value = "地图坐标")
private String coordinate;

@ApiModelProperty(value = "地图坐标经度")
private String coordinateX;

@ApiModelProperty(value = "地图坐标纬度")
private String coordinateY;

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

@ApiModelProperty(value = "商户id")
private Integer merId;

}