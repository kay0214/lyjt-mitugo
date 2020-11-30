package co.yixiang.modules.couponUse.dto;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 船只运营记录详情
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxShipOperationDetail对象", description = "船只运营记录详情")
public class YxShipOperationDetailVO extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "卡券订单id")
    private String couponOrderId;

    @ApiModelProperty(value = "船只id")
    private Integer shipId;

    @ApiModelProperty(value = "船只出港批次号")
    private String batchNo;

    @ApiModelProperty(value = "船只名称")
    private String shipName;

    @ApiModelProperty(value = "船长id")
    private Integer captainId;

    @ApiModelProperty(value = "船长姓名")
    private String captainName;

    @ApiModelProperty(value = "核销人id")
    private Integer useId;

    @ApiModelProperty(value = "核销人姓名")
    private String useName;

    @ApiModelProperty(value = "乘客身体状况")
    private String healthStatus;

    @ApiModelProperty(value = "承载人数")
    private Integer totalPassenger;

    @ApiModelProperty(value = "老年人人数")
    private Integer oldPassenger;

    @ApiModelProperty(value = "未成年人数")
    private Integer underagePassenger;

    @ApiModelProperty(value = "乘客信息")
    List<YxShipPassengerVO> passengerVOList;

    @ApiModelProperty(value = "购买时间")
    private String buyDate;

    @ApiModelProperty(value = "最多乘坐")
    private Integer rideLimit;

    @ApiModelProperty(value = "系列名称")
    private String  seriesName ="";
    @ApiModelProperty(value = "核销时间")
    private String userdTime;
    @ApiModelProperty(value = "核销人员")
    private String userdUserName;

    @ApiModelProperty(value = "船只图片")
    private String shipImageUrl="";
    @ApiModelProperty(value = "创建人")
    private Integer createUserId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "卡券订单编号")
    private String orderId;
}
