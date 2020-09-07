package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.web.param.YxStoreCouponUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 优惠券发放记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxStoreCouponUserMapper extends BaseMapper<YxStoreCouponUser> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponUserQueryVo getYxStoreCouponUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCouponUserQueryParam
     * @return
     */
    IPage<YxStoreCouponUserQueryVo> getYxStoreCouponUserPageList(@Param("page") Page page, @Param("param") YxStoreCouponUserQueryParam yxStoreCouponUserQueryParam);


    @Select("<script> SELECT id,cid,uid,coupon_title,coupon_price,use_min_price,add_time,end_time,use_time,type,`status`,is_fail,store_id from yx_store_coupon_user " +
            "where 1 =1 and uid =  #{param.uid}" +
            "<if test=\"param.type!=null\">  and `status` = #{param.type} </if> </script>")
    IPage<YxStoreCouponUserQueryVo> selectCouponUserListPage(@Param("page") Page page , @Param("param") YxStoreCouponUserQueryParam couponUserQueryParam);

    @Select("<script> SELECT count(1) from yx_store_coupon_user " +
            "where 1 =1 and uid =  #{param.uid}" +
            "<if test=\"param.type!=null\">  and `status` = #{param.type} </if> </script>")
    int getCouponUserCount(@Param("param") YxStoreCouponUserQueryParam couponUserQueryParam);

}
