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
 * 用户地址表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponOrderUse对象", description="用户地址表")
public class YxCouponOrderUse extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "订单ID")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "订单号")
private String orderId;

@ApiModelProperty(value = "核销商铺id")
private Integer storeId;

@ApiModelProperty(value = "店铺名称")
private String storeName;

@ApiModelProperty(value = "核销次数")
private Integer usedCount;

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

@ApiModelProperty(value = "卡券id")
private Integer couponId;

}
