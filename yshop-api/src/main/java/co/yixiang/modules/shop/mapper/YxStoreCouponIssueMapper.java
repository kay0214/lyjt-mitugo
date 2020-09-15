package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueQueryParam;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券前台领取表 Mapper 接口
 * </p>
 *
 * @author liusy
 * @since 2020-08-31
 */
@Repository
public interface YxStoreCouponIssueMapper extends BaseMapper<YxStoreCouponIssue> {

    @Select("select A.cid,A.end_time as endTime,A.start_time as startTime," +
            "A.is_permanent as isPermanent,A.remain_count as remainCount," +
            "A.total_count as totalCount,A.id,B.coupon_price as couponPrice," +
            "B.use_min_price as useMinPrice, " +
            "B.store_id as storeId" +
            " from yx_store_coupon_issue A left join yx_store_coupon B " +
            "on A.cid = B.id " +
            "where A.status =1 " +
            "AND (  (  A.start_time < unix_timestamp(now())  AND A.end_time > unix_timestamp(now()) ) " +
            "OR (  A.start_time = 0  AND A.end_time = 0 ) )" +
            " AND A.is_del = 0  AND " +
            "( A.remain_count > 0 OR A.is_permanent = 1 ) ORDER BY B.sort DESC")
    List<YxStoreCouponIssueQueryVo> selectList(Page page);

    @Select("select A.cid,A.end_time as endTime,A.start_time as startTime," +
            "A.is_permanent as isPermanent,A.remain_count as remainCount," +
            "A.total_count as totalCount,A.id" +
            " from yx_store_coupon_issue A" +
            " where A.status =1 and A.id=#{id}" +
            " AND (  (  A.start_time < unix_timestamp(now())  AND A.end_time > unix_timestamp(now()) ) " +
            "OR (  A.start_time = 0  AND A.end_time = 0 ) )" +
            " AND A.is_del = 0  AND " +
            "( A.remain_count > 0 OR A.is_permanent = 1 )")
    YxStoreCouponIssueQueryVo selectOne(int id);

    @Update("update yx_store_coupon_issue set remain_count=remain_count-1" +
            " where id=#{id}")
    int decCount(@Param("id") int id);



    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponIssueQueryVo getYxStoreCouponIssueById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCouponIssueQueryParam
     * @return
     */
    IPage<YxStoreCouponIssueQueryVo> getYxStoreCouponIssuePageList(@Param("page") Page page, @Param("param") YxStoreCouponIssueQueryParam yxStoreCouponIssueQueryParam);

    @Select("select A.cid,A.end_time as endTime,A.start_time as startTime," +
            "A.is_permanent as isPermanent,A.remain_count as remainCount," +
            "A.total_count as totalCount,A.id,B.coupon_price as couponPrice," +
            "B.use_min_price as useMinPrice, " +
            "B.store_id as storeId, " +
            "A.`status` as status"+
            " from yx_store_coupon_issue A left join yx_store_coupon B " +
            "on A.cid = B.id " +
            "where A.status =1 " +
            "AND (  (  A.start_time < unix_timestamp(now())  AND A.end_time > unix_timestamp(now()) ) " +
            "OR (  A.start_time = 0  AND A.end_time = 0 ) )" +
            " AND A.is_del = 0  AND B.store_id = #{storeId} AND " +
            "( A.remain_count > 0 OR A.is_permanent = 1 ) ORDER BY B.sort DESC")
    List<YxStoreCouponIssueQueryVo> selectCouponListByStoreId(@Param("page") Page page ,@Param("storeId") Integer storeId);

    @Select("<script> select A.cid,A.end_time as endTime,A.start_time as startTime," +
            "A.is_permanent as isPermanent,A.remain_count as remainCount," +
            "A.total_count as totalCount,A.id,B.coupon_price as couponPrice," +
            "B.use_min_price as useMinPrice, " +
            "B.store_id as storeId, B.title as conponName " +
            " from yx_store_coupon_issue A left join yx_store_coupon B " +
            "on A.cid = B.id " +
            "where A.status =1 " +
            "AND (  (  A.start_time <![CDATA[ < ]]>  unix_timestamp(now())  AND A.end_time <![CDATA[ > ]]> unix_timestamp(now()) ) " +
            "OR (  A.start_time = 0  AND A.end_time = 0 ) )" +
            " AND A.is_del = 0   AND " +
            "<if test=\"param.storeId!=null\">  B.store_id = #{param.storeId} AND </if>" +
            "( A.remain_count <![CDATA[ > ]]> 0 OR A.is_permanent = 1 ) </script>")
    IPage<YxStoreCouponIssueQueryVo> selectCouponListByStoreIdPage(@Param("page") Page page ,@Param("param") YxStoreCouponQueryParam yxCouponsQueryParam);

    @Select("<script> select count(1)  "+
            " from yx_store_coupon_issue A left join yx_store_coupon B " +
            "on A.cid = B.id " +
            "where A.status =1 " +
            "AND (  (  A.start_time <![CDATA[ < ]]>  unix_timestamp(now())  AND A.end_time <![CDATA[ > ]]> unix_timestamp(now()) ) " +
            "OR (  A.start_time = 0  AND A.end_time = 0 ) )" +
            " AND A.is_del = 0  AND " +
            "<if test=\"param.storeId!=null\">  B.store_id = #{param.storeId} AND </if>" +
            "(A.remain_count <![CDATA[ > ]]>  0 OR A.is_permanent = 1)</script>")
    int getCountByStoreId(@Param("param") YxStoreCouponQueryParam yxCouponsQueryParam);
}
