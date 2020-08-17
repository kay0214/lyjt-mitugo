/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.domain;

import lombok.Data;

/**
* @author hupeng
* @date 2020-05-12
*/


@Data
public class YxStoreProductChange {

    /** 商品id */
    private Integer id;

    // 状态（0：关闭，1：开启）
    private int changeStatus;
    //促销类型
    /*benefit - 促销
    best - 精品
    hot - 热卖
    new - 新品*/
    private String changeType;
}
