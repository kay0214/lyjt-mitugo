package co.yixiang.modules.order.web.vo;

import co.yixiang.modules.order.web.dto.StatusDTO;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value = "YxStoreOrderQueryVo对象", description = "订单表查询参数")
public class YxStoreOrderQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "额外订单号")
    private String extendOrderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "详细地址")
    private String userAddress;

    @ApiModelProperty(value = "购物车id")
    private String cartId;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal freightPrice;

    @ApiModelProperty(value = "订单商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "订单总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "邮费")
    private BigDecimal totalPostage;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "支付邮费")
    private BigDecimal payPostage;

    @ApiModelProperty(value = "抵扣金额")
    private BigDecimal deductionPrice;

    @ApiModelProperty(value = "优惠券id")
    private Integer couponId;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponPrice;

    @ApiModelProperty(value = "支付状态")
    private Integer paid;

    @ApiModelProperty(value = "支付时间")
    private Integer payTime;

    @ApiModelProperty(value = "支付方式")
    private String payType;

    @ApiModelProperty(value = "创建时间")
    private Integer addTime;

    @ApiModelProperty(value = "订单状态（-1 : 申请退款 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价；-1：已退款）")
    private Integer status;

    @ApiModelProperty(value = "0 未退款 1 申请中 2 已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款图片")
    private String refundReasonWapImg;

    @ApiModelProperty(value = "退款用户说明")
    private String refundReasonWapExplain;

    @ApiModelProperty(value = "退款时间")
    private Integer refundReasonTime;

    @ApiModelProperty(value = "前台退款原因")
    private String refundReasonWap;

    @ApiModelProperty(value = "不退款的理由")
    private String refundReason;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "快递公司编号")
    private String deliverySn;

    @ApiModelProperty(value = "快递名称/送货人姓名")
    private String deliveryName;

    @ApiModelProperty(value = "发货类型")
    private String deliveryType;

    @ApiModelProperty(value = "快递单号/手机号")
    private String deliveryId;

    @ApiModelProperty(value = "消费赚取积分")
    private BigDecimal gainIntegral;

    @ApiModelProperty(value = "使用积分")
    private BigDecimal useIntegral;

    @ApiModelProperty(value = "给用户退了多少积分")
    private BigDecimal backIntegral;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;

    @ApiModelProperty(value = "唯一id(md5加密)类似id")
    private String unique;

    @ApiModelProperty(value = "管理员备注")
    private String remark;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    private Integer isMerCheck;

    @ApiModelProperty(value = "拼团产品id0一般产品")
    private Integer combinationId;

    @ApiModelProperty(value = "拼团id 0没有拼团")
    private Integer pinkId;

    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    @ApiModelProperty(value = "秒杀产品ID")
    private Integer seckillId;

    @ApiModelProperty(value = "砍价id")
    private Integer bargainId;

    @ApiModelProperty(value = "核销码")
    private String verifyCode;

    @ApiModelProperty(value = "门店id")
    private Integer storeId;

    @ApiModelProperty(value = "配送方式 1=快递 ，2=门店自提")
    private Integer shippingType;

    @ApiModelProperty(value = "支付渠道(0微信公众号1微信小程序)")
    private Integer isChannel;

    private Integer isRemind;

    private Integer isSystemDel;

    @ApiModelProperty(value = "推荐人用户ID")
    private Integer parentId;

    @ApiModelProperty(value = "分享人用户ID")
    private Integer shareId;

    @ApiModelProperty(value = "推荐人类型:1商户;2合伙人;3用户")
    private Integer parentType;

    @ApiModelProperty(value = "分享人的推荐人用户ID")
    private Integer shareParentId;

    @ApiModelProperty(value = "分享人的推荐人类型:1商户;2合伙人;3用户")
    private Integer shareParentType;

    @ApiModelProperty(value = "下单时的佣金")
    private BigDecimal commission;
    @ApiModelProperty(value = "付款单号")
    private String paymentNo;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "redis的key")
    private String uniqueKey;
    private List<YxStoreCartQueryVo> cartInfo;

    private StatusDTO _status;
    private String code;

    private String mapKey;

    private YxSystemStoreQueryVo systemStore;

}