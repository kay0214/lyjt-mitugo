package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponsCategory;
import co.yixiang.modules.coupons.web.param.YxCouponsCategoryQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsCategoryQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 本地生活, 卡券分类表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxCouponsCategoryMapper extends BaseMapper<YxCouponsCategory> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsCategoryQueryVo getYxCouponsCategoryById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponsCategoryQueryParam
     * @return
     */
    IPage<YxCouponsCategoryQueryVo> getYxCouponsCategoryPageList(@Param("page") Page page, @Param("param") YxCouponsCategoryQueryParam yxCouponsCategoryQueryParam);

}
