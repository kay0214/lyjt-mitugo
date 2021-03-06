package co.yixiang.modules.shop.web.vo;

import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 购物车表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-25
 */
@Data
@ApiModel(value = "YxStoreStoreCartQueryVo对象", description = "购物车参数")
public class YxStoreStoreCartQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private Integer storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "购物车商品信息")
    private List<YxStoreCartQueryVo> cartList;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    //订单价格
    @ApiModelProperty(value = "订单价格")
    private BigDecimal orderSumPrice;
    // 订单成本价
    @ApiModelProperty(value = "订单成本价")
    private BigDecimal orderCostPrice;
    //VIP价格
    @ApiModelProperty(value = "VIP价格")
    private BigDecimal orderVipTruePrice;

    //邮费
    @ApiModelProperty(value = "邮费")
    private BigDecimal storePostage;

    /*@ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;*/

    @ApiModelProperty(value = "可用优惠券列表")
    private List<YxStoreCouponUser> usableCoupon;


}