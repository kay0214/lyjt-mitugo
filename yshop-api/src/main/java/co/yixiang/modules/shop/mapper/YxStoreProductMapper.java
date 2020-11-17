package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.couponUse.dto.ShipUserLeaveVO;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author nxl
 * @since 2020-08-19
 */
@Repository
public interface YxStoreProductMapper extends BaseMapper<YxStoreProduct> {

    @Update("update yx_store_product set stock=CAST(stock  AS SIGNED)-#{num}, sales=sales+#{num} " +
            " where id=#{productId}")
    int decStockIncSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set stock=stock+#{num}, sales=CAST(sales  AS SIGNED) -#{num}" +
            " where id=#{productId}")
    int incStockDecSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set sales=sales + #{num} " +
            " where id=#{productId}")
    int incSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set sales=CAST(sales  AS SIGNED)-#{num}" +
            " where id=#{productId}")
    int decSales(@Param("num") int num,@Param("productId") int productId);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductQueryVo getYxStoreProductById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductQueryParam
     * @return
     */
    IPage<YxStoreProductQueryVo> getYxStoreProductPageList(@Param("page") Page page, @Param("param") YxStoreProductQueryParam yxStoreProductQueryParam);

    /**
     * 所有商品数量
     * @return
     */
    @Select("select count(0) from yx_store_product where is_del=0")
    Integer getAllProduct();

    /**
     * 本地生活商品数量
     * @return
     */
    @Select("select count(0) from yx_coupons where is_show=0 and del_flag=0")
    Integer getLocalProduct();

    @Select("select * from (select sp.* from yx_store_product sp INNER JOIN yx_store_info si ON sp.store_id = si.id AND si.`status` = 0 AND si.del_flag = 0 ) t ${ew.customSqlSegment}")
    IPage<YxStoreProduct> selectPageAllProduct(Page<YxStoreProduct> pageModel,@Param(Constants.WRAPPER) QueryWrapper<YxStoreProduct> wrapper);

    /**
     * 商户的本地生活商品数量
     * @param storeId
     * @return
     */
    @Select("SELECT count(is_show = 0 or null) AS localProductUnder,count(0) AS localProduct FROM yx_coupons WHERE store_id = #{storeId} AND del_flag = 0 ")
    Map<String, Integer> getLocalProductCount(@Param("storeId")Integer storeId);

    /**
     * 本地生活订单相关数量
     * @param storeId
     * @return
     */
    @Select("select  count(0) AS localOrderCount,count(status in(4,5) or null) AS localOrderWait, count(status = 7 or null) AS localOrderRefund from yx_coupon_order where mer_id=#{storeId} pay_staus=1 and (status =2 or status >3) and del_flag=0 and DATE_FORMAT(FROM_UNIXTIME(create_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    Map<String, Integer> getLocalProductOrderCount(@Param("storeId") Integer storeId);

    /**
     * 今日营业额
     * @param storeId
     * @return
     */
    @Select("select IFNULL(sum(total_price),0) from yx_coupon_order where mer_id=#{storeId} and del_flag=0 and status in (2,4,5,6) and DATE_FORMAT(FROM_UNIXTIME(create_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') and mer_id=#{mer_id}")
    BigDecimal getLocalSumPrice(@Param("storeId") Integer storeId);

    /**
     * 商城商品相关
     * @param storeId
     * @return
     */
    @Select("SELECT count(is_show = 0 or null) AS shopProductUnder,count(0) AS shopProduct FROM yx_store_product WHERE store_id = #{storeId} AND is_del = 0 ")
    Map<String, Integer> getShopProductCount(@Param("storeId")Integer storeId);

    /**
     * 商城订单数量相关
     * @param storeId
     * @return
     */
    @Select("select  count(0) AS shopOrderCount,count(status =0 or null) AS shopOrderSend, count(status = -1 or null) AS shopOrderRefund from yx_store_order where mer_id=#{storeId}   and is_del=0 and DATE_FORMAT(FROM_UNIXTIME(create_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    Map<String, Integer> getShopOrderCount(@Param("storeId")Integer storeId);

    /**
     * 商城今日营业额
     * @param storeId
     * @return
     */
    @Select("select  IFNULL(sum(total_price),0) AS shopSumPrice from yx_store_order where mer_id=#{storeId} and status in (0,1,2,3)   and is_del=0 and DATE_FORMAT(FROM_UNIXTIME(create_time),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    BigDecimal getShopSumPrice(@Param("storeId") Integer storeId);

    /**
     * 船只出港次数最多的船只
     * @param storeId
     * @return
     */
    @Select("select a.ship_name name,count(0) counts from `yx_ship_operation` a left JOIN `yx_ship_info` b on a.ship_id=b.id and b.`mer_id`=#{storeId} WHERE a.del_flag=0 and a.`status` in (1,2)  and DATE_FORMAT(FROM_UNIXTIME(a.`create_time`),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') group by a.ship_id limit 10")
    List<ShipUserLeaveVO> getTopShipLeaves(@Param("storeId") Integer storeId);

    /**
     * 船只出港次数最多的船长
     * @param storeId
     * @return
     */
    @Select("select a.captain_name name,count(0) counts from `yx_ship_operation` a left JOIN `yx_ship_info` b on a.ship_id=b.id and b.`mer_id`=#{storeId} WHERE a.del_flag=0 and a.`status` in (1,2)  and DATE_FORMAT(FROM_UNIXTIME(a.`create_time`),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') group by a.captain_id limit 10")
    List<ShipUserLeaveVO> getShipUserLeaveS(@Param("storeId") Integer storeId);

    /**
     * 今日出港次数
     * @param storeId
     * @return
     */
    @Select("select count(0) counts from `yx_ship_operation` a left JOIN `yx_ship_info` b on a.ship_id=b.id and b.`mer_id`=#{storeId} WHERE a.del_flag=0 and a.`status` in (1,2)  and DATE_FORMAT(FROM_UNIXTIME(a.`create_time`),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') ")
    Integer getShipLeaveCount(@Param("storeId") Integer storeId);

    /**
     * 今日运营船只
     * @param storeId
     * @return
     */
    @Select("select count(0) counts from `yx_ship_operation` a left JOIN `yx_ship_info` b on a.ship_id=b.id and b.`mer_id`=#{storeId} WHERE a.del_flag=0 and a.`status` in (0,1,2)  and DATE_FORMAT(FROM_UNIXTIME(a.`create_time`),'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') group by `ship_id`")
    Integer getShipCount(@Param("storeId") Integer storeId);
}
