package co.yixiang.modules.coupon.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 卡券添加实体
 * @Author : huanghui
 */
@Data
public class CouponAddRequest implements Serializable {

    /** 卡券名称 */
    @NotBlank(message = "卡券名称不可为空")
    private String couponName;


    /** 卡券类型;1:代金券, 2:折扣券, 3:满减券 */
    @NotNull(message = "卡券类型不可为空")
    private Integer couponType;


    /** 卡券所属分类 */
    @NotNull(message = "请选择卡券类别")
    private Integer couponCategory;


    /** 代金券面额, coupon_type为1时使用 */
    private BigDecimal denomination;


    /** 折扣券折扣率, coupon_type为2时使用 */
    private BigDecimal discount;


    /** 使用门槛, coupon_type为3时使用 */
    private BigDecimal threshold;


    /** 优惠金额, coupon_type为3时使用 */
    private BigDecimal discountAmount;


    /** 销售价格 */
    @NotNull(message = "销售价格不可为空")
    private BigDecimal sellingPrice;


    /** 原价 */
    @NotNull(message = "原价不可为空")
    private BigDecimal originalPrice;


    /** 平台结算价 */
    @NotNull(message = "平台结算价不可为空")
    private BigDecimal settlementPrice;


    /** 佣金 */
    private BigDecimal commission;


    /** 每人限购数量 */
    @NotNull(message = "请输入每人限购数量")
    private Integer quantityLimit;


    /** 库存 */
    @NotNull(message = "请输入库存数量")
    private Integer inventory;


    /** 销量 */
    private Integer sales;


    /** 虚拟销量 */
    private Integer ficti;


    /** 核销次数 */
    @NotNull(message = "请输入卡券核销次数")
    private Integer writeOff;


    /** 有效期始 */
    @NotNull(message = "请输入有效期起期")
    private Timestamp expireDateStart;


    /** 有效期止 */
    @NotNull(message = "请输入有效期止期")
    private Timestamp expireDateEnd;


    /** 热门优惠; 1:是, 0否 */
    @NotNull(message = "请选择是否热门优惠")
    private Integer isHot;


    /** 状态（0：未上架，1：上架） */
    private Integer isShow;


    /** 过期退 0:不支持 1支持 */
    private Integer outtimeRefund;


    /** 免预约 0:不支持 1支持 */
    private Integer needOrder;


    /** 随时退 0:不支持 1支持 */
    private Integer awaysRefund;


    /** 使用条件 描述 */
    @NotBlank(message = "使用条件不可为空")
    private String useCondition;


    /** 可用时间始 */
    @NotBlank(message = "可用时间起期不可为空")
    private String availableTimeStart;


    /** 可用时间止 */
    @NotBlank(message = "可用时间止期不可为空")
    private String availableTimeEnd;

    /** 卡券详情 */
    @NotBlank(message = "请输入卡券详情")
    private String content;

    @NotBlank(message = "请上传卡券图片")
    private String image;

    /** 轮播图 */
    @NotBlank(message = "请上传卡券轮播图")
    private String sliderImage;

}
