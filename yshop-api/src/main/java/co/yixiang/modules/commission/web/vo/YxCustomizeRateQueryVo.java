package co.yixiang.modules.commission.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 自定义分佣配置表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxCustomizeRateQueryVo对象", description="自定义分佣配置表查询参数")
public class YxCustomizeRateQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "主键")
private Integer id;

@ApiModelProperty(value = "类型：0：本地生活，1：商城")
private Integer rateType;

@ApiModelProperty(value = "卡券/商品关联id")
private Integer linkId;

@ApiModelProperty(value = "平台抽成")
private BigDecimal fundsRate;

@ApiModelProperty(value = "分享人")
private BigDecimal shareRate;

@ApiModelProperty(value = "分享人上级")
private BigDecimal shareParentRate;

@ApiModelProperty(value = "推荐人")
private BigDecimal parentRate;

@ApiModelProperty(value = "商户")
private BigDecimal merRate;

@ApiModelProperty(value = "合伙人")
private BigDecimal partnerRate;

@ApiModelProperty(value = "拉新池")
private BigDecimal referenceRate;

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