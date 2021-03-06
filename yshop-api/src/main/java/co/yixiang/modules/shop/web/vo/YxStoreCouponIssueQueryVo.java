package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 优惠券前台领取表 查询结果对象
 * </p>
 *
 * @author liusy
 * @date 2020-08-31
 */
@Data
@ApiModel(value = "YxStoreCouponIssueQueryVo对象", description = "优惠券前台领取表查询参数")
public class YxStoreCouponIssueQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "优惠券ID")
    private Integer cid;

    @ApiModelProperty(value = "卡券所属商铺")
    private Integer storeId;

    @ApiModelProperty(value = "优惠券领取开启时间")
    private Integer startTime;

    @ApiModelProperty(value = "优惠券领取结束时间")
    private Integer endTime;

    @ApiModelProperty(value = "优惠券领取数量")
    private Integer totalCount;

    @ApiModelProperty(value = "优惠券剩余领取数量")
    private Integer remainCount;

    @ApiModelProperty(value = "是否无限张数")
    private Integer isPermanent;

    @ApiModelProperty(value = "1 正常 0 未开启 -1 已无效")
    private Integer status;

    private Double couponPrice;

    private Double useMinPrice;

    private Boolean isUse;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "优惠券名")
    private String conponName;

    @ApiModelProperty(value = "使用状态（0：未使用，1：已使用, 2:已过期，-1：未领取）")
    private Integer usedFlg;


    @ApiModelProperty(value = "优惠券领取开启时间-格式化")
    private String formatStartTime;

    @ApiModelProperty(value = "优惠券领取结束时间-格式化")
    private String formatEndTime;

}