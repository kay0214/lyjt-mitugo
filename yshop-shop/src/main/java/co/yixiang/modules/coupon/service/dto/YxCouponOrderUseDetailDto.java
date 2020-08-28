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

import java.io.Serializable;
import java.util.List;

/**
 * @author liusy
 * @date 2020-08-27
 */
@Data
public class YxCouponOrderUseDetailDto implements Serializable {

    /** 核销商铺id */
    private Integer storeId;

    /** 店铺名称 */
    private String storeName;

    /** 核销人员 */
    private String manageUserName;

    /** 管理人电话 */
    private String manageMobile;

    /** 核销记录 */
    private List<YxCouponOrderUseDto> list;

}
