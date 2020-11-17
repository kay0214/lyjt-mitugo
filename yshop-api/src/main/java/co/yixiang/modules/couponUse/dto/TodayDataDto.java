package co.yixiang.modules.couponUse.dto;

import co.yixiang.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel(value="核销端数据统计", description="核销端数据统计")
public class TodayDataDto implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "今天时间")
    private String nowDate = DateUtils.getDate();

    // 本地生活相关-----------------------
    @ApiModelProperty(value = "卡券数量")
    private Integer localProduct;

    @ApiModelProperty(value = "待上架卡券")
    private Integer localProductUnder;

    @ApiModelProperty(value = "今日营业额")
    private BigDecimal localSumPrice;

    @ApiModelProperty(value = "今日订单数")
    private Integer localOrderCount;

    @ApiModelProperty(value = "未核销订单")
    private Integer localOrderWait;

    @ApiModelProperty(value = "待处理退款")
    private Integer localOrderRefund;


    // 商城相关-----------------------
    @ApiModelProperty(value = "商品数量")
    private Integer shopProduct;

    @ApiModelProperty(value = "待上架商品")
    private Integer shopProductUnder;

    @ApiModelProperty(value = "今日营业额")
    private BigDecimal shopSumPrice;

    @ApiModelProperty(value = "今日订单数")
    private Integer shopOrderCount;

    @ApiModelProperty(value = "待发货订单")
    private Integer shopOrderSend;

    @ApiModelProperty(value = "待处理退款")
    private Integer shopOrderRefund;


    // 船只信息---------------
    @ApiModelProperty(value = "今日出港次数")
    private Integer leaveCount;

    @ApiModelProperty(value = "今日运营船只")
    private Integer shipCount;

    @ApiModelProperty(value = "出港最多的船长")
    private List<ShipUserLeaveVO> shipUserLeaveS;

    @ApiModelProperty(value = "船只出港次数最多的船只")
    private List<ShipUserLeaveVO> shipLeaves;
    
}