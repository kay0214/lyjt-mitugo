package co.yixiang.modules.order.service;

import co.yixiang.common.api.ApiResult;
import co.yixiang.modules.order.entity.YxCouponOrder;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.order.web.param.OrderStatusQueryParam;
import co.yixiang.modules.order.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.order.web.vo.YxCouponOrderQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单表 服务类
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
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

    /**
     * 根据订单号获取订单状态
     * @param idParam
     * @return
     */
    ApiResult selectByOrderId(OrderStatusQueryParam idParam);
}
