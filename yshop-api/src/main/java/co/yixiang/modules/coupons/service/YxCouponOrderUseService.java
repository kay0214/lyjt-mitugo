package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxCouponOrderUseService extends BaseService<YxCouponOrderUse> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderUseQueryVo getYxCouponOrderUseById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponOrderUseQueryParam
     * @return
     */
    Paging<YxCouponOrderUseQueryVo> getYxCouponOrderUsePageList(YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam) throws Exception;

}
