package co.yixiang.modules.coupons.web.vo;

import co.yixiang.modules.shop.entity.YxStoreInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单画面卡券详情 返回实体
 * @Author : huanghui
 */
@Data
public class CouponInfoQueryVo implements Serializable {

    @ApiModelProperty(value = "卡券主键")
    private Integer id;

    /** 所属商户 */
    @ApiModelProperty(value = "所属商户")
    private Integer belong;

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

    @ApiModelProperty(value = "核销次数")
    private Integer writeOff;

    @ApiModelProperty(value = "有效期始")
    private Date expireDateStart;

    @ApiModelProperty(value = "有效期止")
    private Date expireDateEnd;

    @ApiModelProperty(value = "卡券所属商铺")
    private YxStoreInfo storeInfo;
}
