package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户地址表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxCouponOrderUseMapper extends BaseMapper<YxCouponOrderUse> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderUseQueryVo getYxCouponOrderUseById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponOrderUseQueryParam
     * @return
     */
    IPage<YxCouponOrderUseQueryVo> getYxCouponOrderUsePageList(@Param("page") Page page, @Param("param") YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam);

}
