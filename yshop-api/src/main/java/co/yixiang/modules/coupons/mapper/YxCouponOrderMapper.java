package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxCouponOrderMapper extends BaseMapper<YxCouponOrder> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderQueryVo getYxCouponOrderById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponOrderQueryParam
     * @return
     */
    IPage<YxCouponOrderQueryVo> getYxCouponOrderPageList(@Param("page") Page page, @Param("param") YxCouponOrderQueryParam yxCouponOrderQueryParam);

}
