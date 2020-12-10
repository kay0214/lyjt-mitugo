package co.yixiang.modules.bill.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户账单明细表 查询结果对象
 * </p>
 *
 * @author zqq
 * @date 2020-12-10
 */
@Data
@ApiModel(value="YxWxUserBillQueryVo对象", description="用户账单明细表查询参数")
public class YxWxUserBillQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键")
private Integer id;

@ApiModelProperty(value = "用户uid")
private Integer billUid;

@ApiModelProperty(value = "用户昵称")
private Integer username;

@ApiModelProperty(value = "账单标题")
private String title;

@ApiModelProperty(value = "明细类型")
private String billType;

@ApiModelProperty(value = "0 = 支出 1 = 获得")
private Integer pm;

@ApiModelProperty(value = "明细数字")
private BigDecimal billNumber;

@ApiModelProperty(value = "剩余")
private BigDecimal balance;

@ApiModelProperty(value = "备注")
private String mark;

@ApiModelProperty(value = "订单类型0:商品 1：卡券")
private Integer brokerageType;

@ApiModelProperty(value = "关联单号id")
private Integer linkId;

@ApiModelProperty(value = "关联订单号")
private String orderId;

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