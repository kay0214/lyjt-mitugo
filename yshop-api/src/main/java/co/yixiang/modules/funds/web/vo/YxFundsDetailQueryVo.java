package co.yixiang.modules.funds.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 平台资金明细 查询结果对象
 * </p>
 *
 * @author zqq
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxFundsDetailQueryVo对象", description="平台资金明细查询参数")
public class YxFundsDetailQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键")
private Integer id;

@ApiModelProperty(value = "1:微商城下单,2:本地生活下单,3:微商城退款,4:本地生活退款")
private Integer type;

@ApiModelProperty(value = "用户uid")
private Integer uid;

@ApiModelProperty(value = "用户名")
private String username;

@ApiModelProperty(value = "订单号")
private String orderId;

@ApiModelProperty(value = "明细种类; 0:支出;1:收入")
private Integer pm;

@ApiModelProperty(value = "订单金额")
private BigDecimal orderAmount;

@ApiModelProperty(value = "订单日期")
private Date addTime;

}