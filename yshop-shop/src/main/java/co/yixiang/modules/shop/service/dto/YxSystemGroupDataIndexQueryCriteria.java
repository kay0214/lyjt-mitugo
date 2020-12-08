/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

import java.util.List;

/**
* @author hupeng
* @date 2020-05-12
*/
@Data
public class YxSystemGroupDataIndexQueryCriteria {
    // 精确
    @Query(type = Query.Type.IN)
    private List<String> groupName;

    // 精确
    @Query
    private Integer status;
}
