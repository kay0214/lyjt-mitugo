/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
@Mapper
public interface StoreProductMapper extends CoreMapper<YxStoreProduct> {


    @Update("update yx_store_product set is_del = #{status} where id = #{id}")
    void updateDel(@Param("status")int status,@Param("id") Integer id);
    @Update("update yx_store_product set is_show = #{status} where id = #{id}")
    void updateOnsale(@Param("status")int status, @Param("id")Integer id);
}
