package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxCouponOrderService extends BaseService<YxCouponOrder> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderQueryVo getYxCouponOrderById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponOrderQueryParam
     * @return
     */
    Paging<YxCouponOrderQueryVo> getYxCouponOrderPageList(YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception;

}
