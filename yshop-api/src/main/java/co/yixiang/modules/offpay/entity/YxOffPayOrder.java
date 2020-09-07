package co.yixiang.modules.offpay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 线下支付订单表
 * </p>
 *
 * @author sss
 * @since 2020-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxOffPayOrder对象", description="线下支付订单表")
public class YxOffPayOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "订单ID")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "订单号")
private String orderId;

@ApiModelProperty(value = "用户id")
private Integer uid;

@ApiModelProperty(value = "商户Nid")
private String storeNid;

@ApiModelProperty(value = "商户ID")
private Integer storeId;

@ApiModelProperty(value = "商户名称")
private String storeName;

@ApiModelProperty(value = "订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回")
private Integer status;

@ApiModelProperty(value = "支付时间")
private Integer payTime;

@ApiModelProperty(value = "支付金额")
private BigDecimal totalPrice;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人 根据创建人关联店铺")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}
