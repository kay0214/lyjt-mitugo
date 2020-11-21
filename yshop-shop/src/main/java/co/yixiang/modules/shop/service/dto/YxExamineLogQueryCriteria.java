/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
 * @author liusy
 * @date 2020-08-19
 */
@Data
public class YxExamineLogQueryCriteria extends BaseCriteria {

    /**
     * 审批类型 1:提现 2:商户信息
     */
    @Query(type = Query.Type.EQUAL)
    private Integer type;

    /**
     * 冗余字段：被审核人信息
     */
    @Query(type = Query.Type.LEFT_LIKE)
    private String username;

    @Query(type = Query.Type.INNER_LIKE)
    private String merchantsName;

    @Query(type = Query.Type.EQUAL)
    private String contactMobile;

    @Query(type = Query.Type.EQUAL)
    private Integer status;
}
