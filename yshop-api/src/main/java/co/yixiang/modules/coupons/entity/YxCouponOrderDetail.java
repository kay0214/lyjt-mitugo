package co.yixiang.modules.coupons.entity;

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
 * 卡券订单详情表
 * </p>
 *
 * @author liusy
 * @since 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponOrderDetail对象", description="卡券订单详情表")
public class YxCouponOrderDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "订单ID")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "订单号")
private String orderId;

@ApiModelProperty(value = "用户id")
private Integer uid;

@ApiModelProperty(value = "卡券id")
private Integer couponId;

@ApiModelProperty(value = "可被核销次数")
private Integer useCount;

@ApiModelProperty(value = "已核销次数")
private Integer usedCount;

@ApiModelProperty(value = "卡券状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回")
private Integer status;

@ApiModelProperty(value = "备注")
private String remark;

@ApiModelProperty(value = "核销码")
private String verifyCode;

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
