package co.yixiang.modules.plat.domain;

import co.yixiang.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@ApiModel(value="数据统计", description="数据统计")
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


    // 以下   平台运营和admin才展示---------
    @ApiModelProperty(value = "商城待处理退款")
    private Integer shopOrderRefund;

    @ApiModelProperty(value = "本地生活待处理退款")
    private Integer localOrderRefund;
    // 会员相关
    @ApiModelProperty(value = "会员数量")
    private Integer userCount;

    @ApiModelProperty(value = "分享达人数量")
    private Integer shareUserCount;

    @ApiModelProperty(value = "商户数量")
    private Integer merCount;

    @ApiModelProperty(value = "已上架商户数量")
    private Integer okMerCount;


    // 以下为近七天相关 本月相关
    @ApiModelProperty(value = "本月本地生活成交额")
    private Double monthLocalPrice;
    @ApiModelProperty(value = "本月本地生活成交量")
    private Integer monthLocalCount;

    @ApiModelProperty(value = "本月商城成交额")
    private Double monthShopPrice;
    @ApiModelProperty(value = "本月商城成交量")
    private Integer monthShopCount;


    @ApiModelProperty(value = "近七天本地生活成交量")
    private Integer lastWeekLocalCount;
    @ApiModelProperty(value = "近七天本地生活成交额")
    private Double lastWeekLocalPrice;

    @ApiModelProperty(value = "近七天商城成交量")
    private Integer lastWeekShopCount;
    @ApiModelProperty(value = "近七天商城成交额")
    private Double lastWeekShopPrice;

}