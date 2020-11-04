/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxCouponsPriceConfigDto implements Serializable {

    /** id */

    private Integer id;


    /** 卡券id */

    private Integer couponId;


    /** 开始日期(YYYYMMDD) */

    private Integer startDate;


    /** 结束日期(YYYYMMDD) */

    private Integer endDate;


    /** 销售价格 */

    private BigDecimal sellingPrice;


    /** 佣金 */

    private BigDecimal commission;


    /** 景区推广价格 */

    private BigDecimal scenicPrice;


    /** 旅行社价格 */

    private BigDecimal travelPrice;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;

}
