package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author nxl
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCart对象", description = "购物车表")
public class YxStoreCart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品属性")
    private String productAttrUnique;

    @ApiModelProperty(value = "商品数量")
    private Integer cartNum;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "0 = 未购买 1 = 已购买 2=已下单未支付")
    private Integer isPay;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;

    @ApiModelProperty(value = "是否为立即购买")
    private Integer isNew;

    @ApiModelProperty(value = "拼团id")
    private Integer combinationId;

    @ApiModelProperty(value = "秒杀产品ID")
    private Integer seckillId;

    @ApiModelProperty(value = "砍价id")
    private Integer bargainId;

    @ApiModelProperty(value = "商铺id")
    private Integer storeId;

    @ApiModelProperty(value = "分享人用户ID")
    private Integer shareId;
    @ApiModelProperty(value = "分享人的推荐人类型:1商户;2合伙人;3用户")
    private Integer shareParentType;
    @ApiModelProperty(value = "分享人的推荐人用户ID")
    private Integer shareParentId;
    @ApiModelProperty(value = "推荐人用户ID")
    private Integer parentId;
    @ApiModelProperty(value = "推荐人类型:1商户;2合伙人;3用户")
    private Integer parentType;
    @ApiModelProperty(value = "下单时的佣金")
    private BigDecimal commission;
    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "产品总金额")
    private BigDecimal totalPrice;

}
