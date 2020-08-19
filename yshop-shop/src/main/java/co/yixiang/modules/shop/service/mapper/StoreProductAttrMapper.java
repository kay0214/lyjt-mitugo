/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxStoreProductAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
@Mapper
public interface StoreProductAttrMapper extends CoreMapper<YxStoreProductAttr> {
    @Select("SELECT IFNULL(sum(stock),0) as stock from yx_store_product_attr_value where product_id  = #{productId}")
    int  getStocketByProductId(@Param("productId")Integer productId);
}
