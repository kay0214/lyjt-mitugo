package co.yixiang.modules.coupon.service.dto;

import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author huiy
 * @date 2020-08-27
 */
@Data
public class YxCouponOrderDto implements Serializable {

    /**
     * 订单ID
     */

    private Integer id;

    /**
     * 状态说明
     */
    private String statusName;

    /**
     * 订单号
     */

    private String orderId;


    /**
     * 用户id
     */

    private Integer uid;


    /**
     * 用户姓名
     */

    private String realName;


    /**
     * 用户电话
     */

    private String userPhone;


    /**
     * 订单商品总数
     */

    private Integer totalNum;


    /**
     * 订单总价
     */

    private BigDecimal totalPrice;


    /**
     * 卡券id
     */

    private Integer couponId;


    /**
     * 卡券金额
     */

    private BigDecimal couponPrice;


    /**
     * 支付状态 0未支付 1已支付
     */

    private Integer payStaus;


    /**
     * 支付时间
     */

    private Integer payTime;


    /**
     * 可被核销次数
     */

    private Integer useCount;


    /**
     * 已核销次数
     */

    private Integer usedCount;


    /**
     * 订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回
     */

    private Integer status;


    /**
     * 0 未退款 1 申请中 2 已退款
     */

    private Integer refundStatus;


    /**
     * 退款用户说明
     */

    private String refundReasonWapExplain;

    /**
     * 退款用户说明
     */

    private String refundReasonWap;

    /**
     * 退款时间
     */

    private Integer refundReasonTime;


    /**
     * 不退款的理由
     */

    private String refundReason;


    /**
     * 退款金额
     */

    private BigDecimal refundPrice;


    /**
     * 备注
     */

    private String mark;


    /**
     * 商户ID
     */

    private Integer merId;


    /**
     * 推荐人用户ID
     */

    private Integer parentId;


    /**
     * 推荐人类型:1商户;2合伙人;3用户
     */

    private Integer parentType;


    /**
     * 分享人Id
     */

    private Integer shareId;


    /**
     * 分享人的推荐人id
     */

    private Integer shareParentId;


    /**
     * 分享人的推荐人类型
     */

    private Integer shareParentType;


    /**
     * 核销码
     */

    private String verifyCode;


    /**
     * 是否删除（0：未删除，1：已删除）
     */

    private Integer delFlag;


    /**
     * 创建人 根据创建人关联店铺
     */

    private Integer createUserId;


    /**
     * 修改人
     */

    private Integer updateUserId;


    /**
     * 创建时间
     */

    private Timestamp createTime;


    /**
     * 更新时间
     */

    private Timestamp updateTime;


    /**
     * 唯一id(md5加密)类似id
     */

    private String unique;


    /**
     * 支付方式
     */

    private String payType;


    /**
     * 支付渠道(0微信公众号1微信小程序)
     */

    private Integer isChannel;


    /**
     * 佣金
     */

    private BigDecimal commission;


    /**
     * 分佣状态 0:未分佣 1:已分佣
     */

    private Integer rebateStatus;

    /** 评价状态：0未评价 1已评价 */
    private Integer evaluate;

    /**
     * 卡券核销记录
     */
    List<YxCouponOrderUse> couponOrderUseList;

    YxCouponsDto yxCouponsDto;

    private String image;

    List<YxCouponOrderDetailDto> detailList;

    /** 在线发票（0：不支持，1：支持） */
    private Integer onlineInvoice;

}
