package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 本地生活分类列表卡券相关
 * @Author : huanghui
 */
@Data
public class LocalLiveCouponsVo {

    @ApiModelProperty(value = "卡券主键")
    private Integer id;

    @ApiModelProperty(value = "卡券编号")
    private String couponNum;

    @ApiModelProperty(value = "卡券名称")
    private String couponName;

    @ApiModelProperty(value = "卡券类型;1:代金券, 2:折扣券, 3:满减券")
    private Integer couponType;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "卡券图片")
    private String img;
}
