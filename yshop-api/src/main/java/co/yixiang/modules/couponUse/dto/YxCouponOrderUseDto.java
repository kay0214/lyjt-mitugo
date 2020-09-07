/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.couponUse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author huiy
 * @date 2020-08-27
 */
@Data
public class YxCouponOrderUseDto implements Serializable {

    /** 订单ID */

    private Integer id;


    /** 订单号 */

    private String orderId;


    /** 核销商铺id */

    private Integer storeId;


    /** 店铺名称 */

    private String storeName;


    /** 核销次数 */

    private Integer usedCount;


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


    /** 卡券id */
    private Integer couponId;

    /** 卡券名称 */
    private String couponName;

    /** 用户昵称 */
    private String nickName;

    /** 卡券类型;1:代金券, 2:折扣券, 3:满减券 */
    private Integer couponType;

    /** 代金券面额, coupon_type为1时使用 */
    private BigDecimal denomination;

    /** 折扣券折扣率, coupon_type为2时使用 */
    private BigDecimal discount;

    /** 使用门槛, coupon_type为3时使用 */
    private BigDecimal threshold;

    /** 优惠金额, coupon_type为3时使用 */
    private BigDecimal discountAmount;

    /** 核销时间 */
    private String useTime;
}
