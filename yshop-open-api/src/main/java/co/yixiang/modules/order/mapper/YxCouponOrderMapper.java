package co.yixiang.modules.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.order.entity.YxCouponOrder;
import co.yixiang.modules.order.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.order.web.vo.YxCouponOrderQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单表 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
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
