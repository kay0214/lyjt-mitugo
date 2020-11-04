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
 * 本地生活帆船订单乘客表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipPassenger对象", description="本地生活帆船订单乘客表")
public class YxShipPassenger extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "卡券订单id")
private Integer couponOrderId;

@ApiModelProperty(value = "船只出港批次号")
private String batchNo;

@ApiModelProperty(value = "船只id")
private Integer shipId;

@ApiModelProperty(value = "乘客姓名")
private String passengerName;

@ApiModelProperty(value = "乘客身份证")
private String idCard;

@ApiModelProperty(value = "乘客电话")
private String phone;

@ApiModelProperty(value = "0:未成年 1:成年人 2：老年人")
private Integer isAdult;

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
