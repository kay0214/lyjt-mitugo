/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author liusy
* @date 2020-08-31
*/
@Data
@TableName("yx_coupons")
public class YxCoupons implements Serializable {

    /** 卡券主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 卡券编号 */
    @NotBlank
    private String couponNum;


    /** 卡券名称 */
    @NotBlank
    private String couponName;


    /** 卡券类型;1:代金券, 2:折扣券, 3:满减券 ，4：船票券*/
    @NotNull
    private Integer couponType;


    /** 卡券所属分类 */
    @NotNull
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
    @NotNull
    private BigDecimal sellingPrice;


    /** 原价 */
    @NotNull
    private BigDecimal originalPrice;


    /** 平台结算价 */
    @NotNull
    private BigDecimal settlementPrice;


    /** 佣金 */
    @NotNull
    private BigDecimal commission;


    /** 每人限购数量 */
    @NotNull
    private Integer quantityLimit;


    /** 库存 */
    @NotNull
    private Integer inventory;


    /** 销量 */
    private Integer sales;


    /** 虚拟销量 */
    private Integer ficti;


    /** 核销次数 */
    @NotNull
    private Integer writeOff;


    /** 有效期始 */
    @NotNull
    private Timestamp expireDateStart;


    /** 有效期止 */
    @NotNull
    private Timestamp expireDateEnd;


    /** 热门优惠; 1:是, 0否 */
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
    @NotBlank
    private String useCondition;


    /** 可用时间始 */
    @NotBlank
    private String availableTimeStart;


    /** 可用时间止 */
    @NotBlank
    private String availableTimeEnd;


    /** 是否删除（0：未删除，1：已删除） */
    @NotNull
    @TableLogic
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 根据创建人关联店铺 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    /** 卡券详情 */
    @NotBlank
    private String content;

    /** 卡券所属商铺 */
    @NotNull
    private Integer storeId;

    /** 卡券简介 */
    @NotBlank
    private String couponInfo;

    /** 排序 */
    private Integer sort;

    /** 船只系列id */
    @NotNull
    private Integer seriesId;


    /** 船只id */
    @NotNull
    private Integer shipId;


    /** 合同模板id */
    @NotNull
    private Integer tempId;


    /** 乘客人数 */
    @NotNull
    private Integer passengersNum;


    /** 合同规则（0：无需保险，1：必须购买，2：非必须） */
    @NotNull
    private Integer insuranceRole;


    /** 有效期（0：不限，1：其他） */
    @NotNull
    private Integer validity;


    /** 有效天数（有效期=1时） */
    @NotNull
    private Integer validityDays;


    /** 在线发票（0：不支持，1：支持） */
    @NotNull
    private Integer onlineInvoice;


    /** 景区推广价格 */
    private BigDecimal scenicPrice;


    /** 旅行社价格 */
    private BigDecimal travelPrice;


    /** 健康确认(逗号分隔) */
    private String confirmation;


    /** 分佣模式（0：按平台，1：不分佣，2：自定义分佣） */
//    @NotNull
    private Integer customizeType;

    public void copy(YxCoupons source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
