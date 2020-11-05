/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
@TableName("yx_coupons_price_config")
public class YxCouponsPriceConfigRequest implements Serializable {

    /** id */
    private Integer id;


    /** 卡券id */
    @NotNull
    private Integer couponId;


    /** 开始日期(YYYYMMDD) */
    private String startDateStr;


    /** 结束日期(YYYYMMDD) */
    private String endDateStr;


    /** 销售价格 */
    private BigDecimal sellingPrice;


    /** 佣金 */
    private BigDecimal commission;


    /** 景区推广价格 */
    private BigDecimal scenicPrice;


    /** 旅行社价格 */
    private BigDecimal travelPrice;


    public void copy(YxCouponsPriceConfigRequest source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
