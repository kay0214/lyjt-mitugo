/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxStoreCart;
import co.yixiang.modules.shop.service.dto.CountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
@Mapper
public interface StoreCartMapper extends CoreMapper<YxStoreCart> {
    @Select("SELECT t.cate_name as catename,count(1) as count from yx_store_cart c  " +
            "LEFT JOIN yx_store_product p on c.product_id = p.id  " +
            "LEFT JOIN yx_store_category t on p.cate_id = t.id " +
            "WHERE c.is_pay = 1 group by t.cate_name")
    List<CountDto> findCateName();
}
