/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author hupeng
* @date 2019-10-04
*/
@Data
public class YxStoreProductSmallDto implements Serializable {

    // 商品id
    private Integer id;

    // 商品图片
    private String image;


    // 商品名称
    private String storeName;


}
