/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.dto;

import co.yixiang.modules.shop.domain.YxCustomizeRate;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
* @author liusy
* @date 2020-08-31
*/
@Data
public class YxCouponsDto implements Serializable {

    /** 卡券主键 */

    private Integer id;


    /** 卡券编号 */

    private String couponNum;


    /** 卡券名称 */

    private String couponName;


    /** 卡券类型;1:代金券, 2:折扣券, 3:满减券 */

    private Integer couponType;


    /** 卡券所属分类 */

    private Integer couponCategory;

    /** 所属分类名称 */
    private String couponCategoryName;


    /** 代金券面额, coupon_type为1时使用 */

    private BigDecimal denomination;


    /** 折扣券折扣率, coupon_type为2时使用 */

    private BigDecimal discount;


    /** 使用门槛, coupon_type为3时使用 */

    private BigDecimal threshold;


    /** 优惠金额, coupon_type为3时使用 */

    private BigDecimal discountAmount;


    /** 销售价格 */

    private BigDecimal sellingPrice;


    /** 原价 */

    private BigDecimal originalPrice;


    /** 平台结算价 */

    private BigDecimal settlementPrice;


    /** 佣金 */

    private BigDecimal commission;


    /** 每人限购数量 */

    private Integer quantityLimit;


    /** 库存 */

    private Integer inventory;


    /** 销量 */

    private Integer sales;


    /** 虚拟销量 */

    private Integer ficti;


    /** 核销次数 */

    private Integer writeOff;


    /** 有效期始 */

    private Timestamp expireDateStart;


    /** 有效期止 */

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

    private String useCondition;


    /** 可用时间始 */

    private String availableTimeStart;


    /** 可用时间止 */

    private String availableTimeEnd;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 根据创建人关联店铺 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;


    /** 卡券详情 */

    private String content;


    /** 卡券所属商铺 */

    private Integer storeId;


    /** 卡券简介 */

    private String couponInfo;

    /** 卡券缩略图 */
    private String image;

    /** 幻灯片 */
    private List<String> sliderImage;

    /** 可被核销次数 */
    private Integer useCount;

    /** 已被核销次数 */
    private Integer usedCount;

    /** 当前卡券状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 排序 */
    private Integer sort;


    /** 船只系列id */
    private Integer seriesId;


    /** 船只id */
    private Integer shipId;


    /** 合同模板id */
    private Integer tempId;


    /** 乘客人数 */
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

    private YxCustomizeRate yxCustomizeRate;
}
