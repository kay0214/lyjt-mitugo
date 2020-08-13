package co.yixiang.modules.user.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 平台资金明细
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxFundsDetail对象", description="平台资金明细")
public class YxFundsDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键")
@TableId(value = "id", type = IdType.AUTO)
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
