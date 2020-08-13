package co.yixiang.modules.activity.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>
 * 用户参与砍价表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "YxStoreBargainUser对象", description = "用户参与砍价表")
public class YxStoreBargainUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户参与砍价表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "砍价产品id")
    private Integer bargainId;

    @ApiModelProperty(value = "砍价的最低价")
    private BigDecimal bargainPriceMin;

    @ApiModelProperty(value = "砍价金额")
    private BigDecimal bargainPrice;

    @ApiModelProperty(value = "砍掉的价格")
    private BigDecimal price;

    @ApiModelProperty(value = "状态 1参与中 2 活动结束参与失败 3活动结束参与成功")
    private Integer status;

    @ApiModelProperty(value = "参与时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否取消")
    private Integer isDel;

}
