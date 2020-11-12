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
 * 本地生活帆船订单乘客表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipPassenger对象", description="本地生活帆船订单乘客表")
public class YxShipPassengerVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

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

@ApiModelProperty(value = "是否合同签订人 0:否 1:是")
private Integer signStatus;

private String ageArea;
}
