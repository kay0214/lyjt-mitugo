package co.yixiang.modules.funds.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 平台账户表 查询结果对象
 * </p>
 *
 * @author zqq
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxFundsAccountQueryVo对象", description="平台账户表查询参数")
public class YxFundsAccountQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键")
private Integer id;

@ApiModelProperty(value = "平台分佣总额")
private BigDecimal price;

@ApiModelProperty(value = "分红总积分")
private BigDecimal bonusPoint;

@ApiModelProperty(value = "拉新总积分")
private BigDecimal referencePoint;

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