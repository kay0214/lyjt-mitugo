/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangqingqing
 * @version OrderInfo, v0.1 2020/8/15 11:19
 */
@Data
public class OrderInfo {

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

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



}
