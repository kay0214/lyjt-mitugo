/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.dto;

import co.yixiang.annotation.Query;
import co.yixiang.modules.shop.service.dto.BaseCriteria;
import lombok.Data;

/**
 * @author hupeng
 * @date 2020-05-13
 */
@Data
public class YxStoreCouponUserQueryCriteria extends BaseCriteria {

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String couponTitle;
    @Query(type = Query.Type.INNER_LIKE)
    private String userName;
    private Integer status;
    /** 是否可用 */
    private Integer isUsed;
    private String userPhone;

}
