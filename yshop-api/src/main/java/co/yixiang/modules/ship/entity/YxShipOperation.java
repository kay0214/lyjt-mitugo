package co.yixiang.modules.ship.entity;

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
 * 船只运营记录
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipOperation对象", description="船只运营记录")
public class YxShipOperation extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "船只出港批次号")
private String batchNo;

@ApiModelProperty(value = "船只id")
private Integer shipId;

@ApiModelProperty(value = "船只名称")
private String shipName;

@ApiModelProperty(value = "船长id")
private Integer captainId;

@ApiModelProperty(value = "船长姓名")
private String captainName;

@ApiModelProperty(value = "承载人数")
private Integer totalPassenger;

@ApiModelProperty(value = "老年人人数")
private Integer oldPassenger;

@ApiModelProperty(value = "未成年人数")
private Integer underagePassenger;

@ApiModelProperty(value = "出港时间")
private Integer leaveTime;

@ApiModelProperty(value = "回港时间")
private Integer returnTime;

@ApiModelProperty(value = "船只状态 0:待出港 1：出港 2：回港")
private Integer status;

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
