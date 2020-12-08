package co.yixiang.modules.couponUse.dto;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船只系列表
 * </p>
 *
 * @author lsy
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipSeries对象", description="船只系列表")
public class YxShipSeriesVO extends BaseEntity {

@ApiModelProperty(value = "系列id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "系列名称")
private String seriesName;

@ApiModelProperty(value = "所属商铺")
private Integer storeId;

@ApiModelProperty(value = "商户id")
private Integer merId;

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

}
