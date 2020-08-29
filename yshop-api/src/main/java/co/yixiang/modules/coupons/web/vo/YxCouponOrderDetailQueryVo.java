package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 卡券订单详情表 查询结果对象
 * </p>
 *
 * @author liusy
 * @date 2020-08-28
 */
@Data
@ApiModel(value = "YxCouponOrderDetailQueryVo对象", description = "卡券订单详情表查询参数")
public class YxCouponOrderDetailQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "卡券id")
    private Integer couponId;

    @ApiModelProperty(value = "卡券名称")
    private String couponName;

    @ApiModelProperty(value = "有效期")
    private String expireDate;

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

    @ApiModelProperty(value = "卡券类型;1:代金券, 2:折扣券, 3:满减券")
    private Integer couponType;

    @ApiModelProperty(value = "代金券面额, coupon_type为1时使用")
    private BigDecimal denomination;

    @ApiModelProperty(value = "折扣券折扣率, coupon_type为2时使用")
    private BigDecimal discount;

    @ApiModelProperty(value = "使用门槛, coupon_type为3时使用")
    private BigDecimal threshold;

    @ApiModelProperty(value = "优惠金额, coupon_type为3时使用")
    private BigDecimal discountAmount;

}