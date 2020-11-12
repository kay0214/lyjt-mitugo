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

    /** 卡券所属商铺 */
    private Integer storeId;

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

    /** 卡券视频说明 */
    private String video;

    /** 卡券简介 */
    private String couponInfo;

    /** 排序 */
    private Integer sort;

    /** 船只系列id */
    @NotNull(message = "请选择船只系列")
    private Integer seriesId;


    /** 船只id */
    private Integer shipId;


    /** 合同模板id */
    @NotNull(message = "合同模板")
    private Integer tempId;


    /** 乘客人数 */
    @NotNull(message = "乘客人数")
    private Integer passengersNum;


    /** 合同规则（0：无需保险，1：必须购买，2：非必须） */
    private Integer insuranceRole;


    /** 有效期（0：不限，1：其他） */
    private Integer validity;


    /** 有效天数（有效期=1时） */
    private Integer validityDays;


    /** 在线发票（0：不支持，1：支持） */
    private Integer onlineInvoice;


    /** 景区推广价格 */
    private BigDecimal scenicPrice;


    /** 旅行社价格 */
    private BigDecimal travelPrice;


    /** 健康确认(逗号分隔) */
    private String confirmation;


    /** 分佣模式（0：按平台，1：不分佣，2：自定义分佣） */
    private Integer customizeType;

    private Integer createUser;
}
