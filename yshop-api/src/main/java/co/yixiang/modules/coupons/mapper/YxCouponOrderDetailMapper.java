package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.web.param.YxCouponOrderDetailQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单详情表 Mapper 接口
 * </p>
 *
 * @author liusy
 * @since 2020-08-28
 */
@Repository
public interface YxCouponOrderDetailMapper extends BaseMapper<YxCouponOrderDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderDetailQueryVo getYxCouponOrderDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponOrderDetailQueryParam
     * @return
     */
    IPage<YxCouponOrderDetailQueryVo> getYxCouponOrderDetailPageList(@Param("page") Page page, @Param("param") YxCouponOrderDetailQueryParam yxCouponOrderDetailQueryParam);

}
