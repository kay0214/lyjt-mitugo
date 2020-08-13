package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 积分获取明细 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxPointDetailQueryVo对象", description="积分获取明细查询参数")
public class YxPointDetailQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键id")
private Integer id;

@ApiModelProperty(value = "用户ID")
private Integer uid;

@ApiModelProperty(value = "积分类别 0:拉新 1:分红")
private Integer type;

@ApiModelProperty(value = "订单编号")
private String orderId;

@ApiModelProperty(value = "订单类型 0:商品购买 1:本地生活")
private Integer orderType;

@ApiModelProperty(value = "订单金额")
private BigDecimal orderPrice;

@ApiModelProperty(value = "订单佣金")
private BigDecimal commission;

@ApiModelProperty(value = "商户id")
private Integer merchantsId;

@ApiModelProperty(value = "商户获取积分数")
private BigDecimal merchantsPoint;

@ApiModelProperty(value = "合伙人id")
private Integer partnerId;

@ApiModelProperty(value = "合伙人获取积分数")
private BigDecimal partnerPoint;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}