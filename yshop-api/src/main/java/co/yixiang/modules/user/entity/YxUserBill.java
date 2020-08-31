package co.yixiang.modules.user.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账单表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxUserBill对象", description="用户账单表")
public class YxUserBill extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "用户账单id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "用户uid")
private Integer uid;

@ApiModelProperty(value = "关联id")
private String linkId;

@ApiModelProperty(value = "0 = 支出 1 = 获得")
private Integer pm;

@ApiModelProperty(value = "账单标题")
private String title;

@ApiModelProperty(value = "明细种类")
private String category;

@ApiModelProperty(value = "明细类型")
private String type;

    @ApiModelProperty(value = "佣金类型")
    private Integer brokerageType;

@ApiModelProperty(value = "明细数字")
private BigDecimal number;

@ApiModelProperty(value = "剩余")
private BigDecimal balance;

@ApiModelProperty(value = "备注")
private String mark;

@ApiModelProperty(value = "添加时间")
private Integer addTime;

@ApiModelProperty(value = "0 = 带确定 1 = 有效 -1 = 无效")
@TableField(value = "`status`")
private Integer status;

@ApiModelProperty(value = "订单商品所属商户id")
private Integer merId;

@ApiModelProperty(value = "订单商品所属合伙人id")
private Integer partnerId;

@ApiModelProperty(value = "用户类型 0:预留 1:前台用户 2：后台商户 3：后台合伙人")
private Integer userType;

    @ApiModelProperty(value = "用户名")
    private String username;

}
