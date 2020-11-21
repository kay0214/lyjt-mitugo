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
public class YxStoreOrderQueryCriteria extends BaseCriteria {

    // 模糊
    @Query(type = Query.Type.UNIX_TIMESTAMP)
    private List<String> addTime;


    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String orderId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String realName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String userPhone;

    @Query
    private Integer paid;

    @Query
    private Integer status;

    @Query
    private Integer refundStatus;

    @Query
    private Integer isDel;

    @Query
    private Integer combinationId;

    @Query
    private Integer seckillId;

    @Query
    private Integer bargainId;

    @Query(propName = "combinationId", type = Query.Type.NOT_EQUAL)
    private Integer newCombinationId;

    @Query(propName = "seckillId", type = Query.Type.NOT_EQUAL)
    private Integer newSeckillId;

    @Query(propName = "bargainId", type = Query.Type.NOT_EQUAL)
    private Integer newBargainId;

    @Query
    private Integer shippingType;

    @Query
    private Integer storeId;

    @Query
    private String merUsername;

    private Integer merUserId;

    /** 商品名称 */
    private String storeName;

}
