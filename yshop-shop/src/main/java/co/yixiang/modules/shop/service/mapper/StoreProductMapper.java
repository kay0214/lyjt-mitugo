/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

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

    @Update("update yx_store_product set sales=sales-#{num}" +
            " where id=#{productId}")
    int decSales(@Param("num") int num,@Param("productId") int productId);
    @Update("update yx_store_product set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{productId}")
    int incStockDecSales(@Param("num") int num,@Param("productId") int productId);




    /**
     * 商户的本地生活商品数量
     * @param storeId
     * @return
     */
    @Select({"<script>","SELECT count(is_show = 0 or null) AS localProductUnder,count(0) AS localProduct FROM yx_coupons WHERE 1=1 ","<if test='storeId!=0'>",
            " AND store_id = #{storeId} </if>"," AND del_flag = 0 ","</script>"})
    Map<String, Long> getLocalProductCount(@Param("storeId")Integer storeId);

    /**
     * 本地生活订单相关数量
     * @param storeId
     * @return
     */
    @Select({"<script>","select  count(0) AS localOrderCount,count(a.status in(4,5) or null) AS localOrderWait, count(a.status = 7 or null) AS localOrderRefund from yx_coupon_order a " +
            "<if test='storeId!=0'> left join yx_store_info b on a.mer_id = b.mer_id and b.id=#{storeId} </if> " +
            "where a.pay_staus=1 and (a.status =2 or a.status >3) and a.del_flag=0 and DATE_FORMAT(a.create_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') ","</script>"})
    Map<String, Long> getLocalProductOrderCount(@Param("storeId") Integer storeId);

    /**
     * 今日营业额
     * @param storeId
     * @return
     */
    @Select({"<script>","select IFNULL(sum(a.total_price),0) from yx_coupon_order a <if test='storeId!=0'> left join yx_store_info b on a.mer_id = b.mer_id and b.id=#{storeId} </if>" +
            " where a.del_flag=0 and a.status in (2,4,5,6) and DATE_FORMAT(a.create_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')","</script>"})
    BigDecimal getLocalSumPrice(@Param("storeId") Integer storeId);

    /**
     * 商城商品相关
     * @param storeId
     * @return
     */
    @Select({"<script>"," SELECT count(is_show = 0 or null) AS shopProductUnder,count(0) AS shopProduct FROM yx_store_product WHERE 1=1 <if test='storeId!=0'> and store_id = #{storeId} </if> AND is_del = 0 ","</script>"})
    Map<String, Long> getShopProductCount(@Param("storeId")Integer storeId);

    /**
     * 商城订单数量相关
     * @param storeId
     * @return
     */
    @Select({"<script>"," select  count(0) AS shopOrderCount,count(status =0 or null) AS shopOrderSend, count(status = -1 or null) AS shopOrderRefund from yx_store_order where 1=1  <if test='storeId!=0'>  and store_id=#{storeId}  </if>  and is_del=0 and DATE_FORMAT(FROM_UNIXTIME(add_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')","</script>"})
    Map<String, Long> getShopOrderCount(@Param("storeId")Integer storeId);

    /**
     * 商城今日营业额
     * @param storeId
     * @return
     */
    @Select({"<script>"," select  IFNULL(sum(total_price),0) AS shopSumPrice from yx_store_order where 1=1 <if test='storeId!=0'>  and  store_id=#{storeId} </if> and status in (0,1,2,3)   and is_del=0 and DATE_FORMAT(FROM_UNIXTIME(add_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')","</script>"})
    BigDecimal getShopSumPrice(@Param("storeId") Integer storeId);

    /**
     * 本月本地生活成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    @Select({"<script>","select IFNULL(sum(a.total_price),0) from yx_coupon_order a <if test='storeId!=0'> left join yx_store_info b on a.mer_id = b.mer_id and b.id=#{storeId} </if> where a.del_flag=0 and a.status in (2,4,5,6) and a.create_time>= FROM_UNIXTIME(#{nowMonth}) ","</script>"})
    Double getMonthLocalPrice(@Param("nowMonth")int nowMonth, @Param("storeId")int storeId);

    /**
     * 本月本地生活成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    @Select({"<script>","select count(0) from yx_coupon_order a <if test='storeId!=0'> left join yx_store_info b on a.mer_id = b.mer_id and b.id=#{storeId} </if> where a.del_flag=0 and a.status in (2,4,5,6) and a.create_time >= FROM_UNIXTIME(#{nowMonth})","</script>"})
    Integer getMonthLocalCount(@Param("nowMonth")int nowMonth, @Param("storeId")int storeId);

    /**
     * 本月商城成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    @Select({"<script>"," select  IFNULL(sum(total_price),0) AS shopSumPrice from yx_store_order where 1=1 <if test='storeId!=0'>  and  store_id=#{storeId} </if> and status in (0,1,2,3)   and is_del=0 and add_time >= FROM_UNIXTIME(#{nowMonth})","</script>"})
    Double getMonthShopPrice(@Param("nowMonth")int nowMonth, @Param("storeId")int storeId);

    /**
     * 本月商城成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    @Select({"<script>"," select  count(0)  from yx_store_order where 1=1 <if test='storeId!=0'>  and  store_id=#{storeId} </if> and status in (0,1,2,3)   and is_del=0 and add_time >= FROM_UNIXTIME(#{nowMonth})","</script>"})
    Integer getMonthShopCount(@Param("nowMonth")int nowMonth, @Param("storeId")int storeId);

}
