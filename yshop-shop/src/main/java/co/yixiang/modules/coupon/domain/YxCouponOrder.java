/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author huiy
* @date 2020-08-14
*/
@Data
@TableName("yx_coupon_order")
public class YxCouponOrder implements Serializable {

    /** 订单ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 唯一值 */
    @TableField(value = "`unique`")
    private String unique;

    /** 订单号 */
    @NotBlank
    private String orderId;


    /** 用户id */
    @NotNull
    private Integer uid;


    /** 用户姓名 */
    @NotBlank
    private String realName;


    /** 用户电话 */
    @NotBlank
    private String userPhone;


    /** 订单商品总数 */
    @NotNull
    private Integer totalNum;


    /** 订单总价 */
    @NotNull
    private BigDecimal totalPrice;


    /** 卡券id */
    @NotNull
    private Integer couponId;


    /** 卡券金额 */
    @NotNull
    private BigDecimal couponPrice;


    /** 支付状态 0未支付 1已支付 */
    @NotNull
    private Integer payStaus;

    @NotNull
    private String payType;

    private Integer isChannel;

    /** 支付时间 */
    private Integer payTime;


    /** 可被核销次数 */
    private Integer useCount;


    /** 已核销次数 */
    private Integer usedCount;


    /** 订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回 */
    @NotNull
    @TableField(value = "`status`")
    private Integer status;


    /** 0 未退款 1 申请中 2 已退款 */
    @NotNull
    private Integer refundStatus;


    /** 退款用户说明 */
    private String refundReasonWapExplain;


    /**
     * 退款用户理由
     */
    private String refundReasonWap;

    /** 退款时间 */
    private Integer refundReasonTime;


    /** 不退款的理由 */
    private String refundReason;


    /** 退款金额 */
    @NotNull
    private BigDecimal refundPrice;


    /** 备注 */
    @NotBlank
    private String mark;


    /** 商户ID */
    @NotNull
    private Integer merId;


    /** 推荐人用户ID */
    private Integer parentId;


    /** 推荐人类型:1商户;2合伙人;3用户 */
    private Integer parentType;


    /** 分享人Id */
    private Integer shareId;


    /** 分享人的推荐人id */
    private Integer shareParentId;


    /** 分享人的推荐人类型 */
    private Integer shareParentType;


    /** 核销码 */
    @NotBlank
    private String verifyCode;

    /** 佣金 */
    private BigDecimal commission;

    /** 分佣状态 0:未分佣 1:已分佣 */
    private Integer rebateStatus;

    /** 是否删除（0：未删除，1：已删除） */
    @NotNull
    @TableLogic
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 根据创建人关联店铺 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    public void copy(YxCouponOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
