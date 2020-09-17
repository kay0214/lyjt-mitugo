package co.yixiang.modules.order.web.param;

import co.yixiang.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreProductReplyParam extends BaseEntity {

    @ApiModelProperty(value = "产品id")
    private Integer productId;

    @ApiModelProperty(value = "商品分数")
    private Integer productScore;

    @ApiModelProperty(value = "服务分数")
    private Integer serviceScore;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评论图片")
    private String pics;

    @ApiModelProperty(value = "产品属性unique")
    private String unique;
}
