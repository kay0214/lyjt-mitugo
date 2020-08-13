package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 用户地址表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxCouponOrderUseQueryVo对象", description="用户地址表查询参数")
public class YxCouponOrderUseQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "订单ID")
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

}