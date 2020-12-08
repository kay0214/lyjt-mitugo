package co.yixiang.modules.coupons.entity;

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
 * 价格配置
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponsPriceConfig对象", description="价格配置")
public class YxCouponsPriceConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "卡券id")
private Integer couponId;

@ApiModelProperty(value = "开始日期(YYYYMMDD)")
private Integer startDate;

@ApiModelProperty(value = "结束日期(YYYYMMDD)")
private Integer endDate;

@ApiModelProperty(value = "销售价格")
private BigDecimal sellingPrice;

@ApiModelProperty(value = "佣金")
private BigDecimal commission;

@ApiModelProperty(value = "景区推广价格")
private BigDecimal scenicPrice;

@ApiModelProperty(value = "旅行社价格")
private BigDecimal travelPrice;

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
