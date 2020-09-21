package co.yixiang.modules.shop.mapper;

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
}
