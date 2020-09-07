package co.yixiang.modules.coupons.mapper;

import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 本地生活, 卡券表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxCouponsMapper extends BaseMapper<YxCoupons> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxCouponsQueryVo getYxCouponsById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param yxCouponsQueryParam
     * @return
     */
    @Select("<script>select yc.* from yx_coupons yc " +
            "inner join yx_store_info ysi on ysi.id = yc.store_id and ysi.status = 0 and ysi.del_flag = 0" +
            "where yc.is_show = 1 and yc.expire_date_start <![CDATA[ <= ]]> now() and yc.expire_date_end <![CDATA[ >= ]]> now() " +
            "<if test=\"param.couponCategory!=null\"> and yc.coupon_category = #{param.couponCategory} </if> " +
            "order by yc.sort asc,yc.create_time desc</script>")
    IPage<YxCouponsQueryVo> getYxCouponsPageList(@Param("page") Page page, @Param("param") YxCouponsQueryParam yxCouponsQueryParam);

    @Select("SELECT yc.* FROM yx_coupons yc " +
            "INNER JOIN yx_store_info ysi ON ysi.id = yc.store_id AND ysi. STATUS = 0 AND ysi.del_flag = 0 " +
            "WHERE yc.is_hot = 1 AND yc.is_show = 1 AND yc.del_flag = 0 and yc.expire_date_start <![CDATA[ <= ]]> now() and yc.expire_date_end <![CDATA[ >= ]]> now() " +
            "ORDER BY yc.sort ASC,yc.create_time")
    IPage<YxCouponsQueryVo> getCouponsHotList(@Param("page") Page page, @Param("param") YxCouponsQueryParam yxCouponsQueryParam);

    @Select("<script>select count(1) from yx_coupons yc inner join yx_store_info ysi on ysi.id = yc.store_id and ysi.status = 0 and ysi.del_flag = 0 " +
            "where is_show = 1 and yc.expire_date_start <![CDATA[ <= ]]> now() and yc.expire_date_end <![CDATA[ >= ]]> now() " +
            "<if test=\"param.couponCategory!=null\"> and yc.coupon_category = #{param.couponCategory} </if> </script>")
    int getCount(@Param("param") YxCouponsQueryParam yxCouponsQueryParam);

    @Select("select yc.* from yx_coupons yc " +
            "inner join yx_store_info ysi on ysi.id = yc.store_id and ysi.status = 0 and ysi.del_flag = 0" +
            "where yc.is_show = 1 and yc.store_id =#{storeId}  and yc.expire_date_start <![CDATA[ <= ]]> now() and yc.expire_date_end <![CDATA[ >= ]]> now() " +
            "order by yc.sort asc")
    IPage<YxCouponsQueryVo> getYxCouponsPageListByStoreId(@Param("page") Page page, @Param("storeId") Integer storeId);

    @Select("select count(1) from yx_coupons yc inner join yx_store_info ysi on ysi.id = yc.store_id and ysi.status = 0 and ysi.del_flag = 0 where is_show = 1 and yc.store_id =#{storeId} ")
    int getCountByStoreId(@Param("storeId") Integer storeId);
}
