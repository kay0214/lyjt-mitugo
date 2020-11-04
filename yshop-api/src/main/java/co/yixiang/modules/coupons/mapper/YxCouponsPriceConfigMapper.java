package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.web.param.YxCouponsPriceConfigQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsPriceConfigQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 价格配置 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxCouponsPriceConfigMapper extends BaseMapper<YxCouponsPriceConfig> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsPriceConfigQueryVo getYxCouponsPriceConfigById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponsPriceConfigQueryParam
     * @return
     */
    IPage<YxCouponsPriceConfigQueryVo> getYxCouponsPriceConfigPageList(@Param("page") Page page, @Param("param") YxCouponsPriceConfigQueryParam yxCouponsPriceConfigQueryParam);

}
