/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shopConfig.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Data
public class YxMerchantsSettlementQueryCriteria {

    /** 公司名 */
    @Query(type = Query.Type.INNER_LIKE)
    private String companyName;


    /** 联系人 */
    @Query(type = Query.Type.INNER_LIKE)
    private String contactsName;


    /** 联系电话 */
    @Query(type = Query.Type.EQUAL)
    private String phone;

    /** 状态：0：待联系，1：有意向，2：已拒绝 */
    @Query(type = Query.Type.EQUAL)
    private Integer status;

    @Query(type = Query.Type.EQUAL)
    private Integer delFlag = 0;
}
