package co.yixiang.modules.shop.web.vo;

import co.yixiang.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreOrderRedis对象", description = "订单表")
public class YxStoreOrderRedisVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车id")
    private Integer cartId;
    @ApiModelProperty(value = "产品id")
    private Integer productId;
    @ApiModelProperty(value = "产品价格")
    private BigDecimal productPrice;
    /** 邮费 */
    private BigDecimal postage;
    /** 是否包邮 */
    private Integer isPostage;




}
