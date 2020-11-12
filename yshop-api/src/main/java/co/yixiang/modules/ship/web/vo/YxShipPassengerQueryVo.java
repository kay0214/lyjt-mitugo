package co.yixiang.modules.ship.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 本地生活帆船订单乘客表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxShipPassengerQueryVo对象", description="本地生活帆船订单乘客表查询参数")
public class YxShipPassengerQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
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

@ApiModelProperty(value = "是否合同签订人 0:否 1:是")
private Integer signStatus;

/*@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;*/

}