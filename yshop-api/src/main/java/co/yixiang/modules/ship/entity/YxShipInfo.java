package co.yixiang.modules.ship.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 船只表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipInfo对象", description="船只表")
public class YxShipInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "船只id")
@TableId(value = "id", type = IdType.AUTO)
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
