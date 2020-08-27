package co.yixiang.modules.coupons.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.CouponInfoQueryVo;
import co.yixiang.modules.coupons.web.vo.CouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.order.web.dto.ComputeDTO;
import co.yixiang.modules.order.web.dto.CouponCacheDTO;
import co.yixiang.modules.order.web.dto.PriceGroupDTO;
import co.yixiang.modules.order.web.param.OrderParam;

import java.io.Serializable;
import java.util.List;

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

    PriceGroupDTO getOrderPriceGroup(CouponOrderQueryVo couponOrderQueryVo);

    Double getOrderSumPrice(CouponOrderQueryVo couponOrderQueryVo, String key);

    String cacheOrderInfo(int uid, List<YxCouponsQueryVo> couponsQueryVoList, PriceGroupDTO priceGroup);

    /**
     * 卡券创建订单
     * @param uid
     * @param key
     * @param param
     * @return
     */
    YxCouponOrder createOrder(int uid, String key, OrderParam param);

    CouponCacheDTO getCacheOrderInfo(int uid, String key);

    /**
     * 删除缓存
     * @param uid
     * @param key
     */
    void delCacheOrderInfo(int uid, String key);

    /**
     * 余额支付
     * @param orderId
     * @param uid
     */
    void yuePay(String orderId, int uid);

    /**
     * 获取订单信息
     * @param unique
     * @param uid
     * @return
     */
    YxCouponOrderQueryVo getOrderInfo(String unique, int uid);

    void paySuccess(String orderId, String payType);

    /**
     * 计算订单价格
     * @param uid
     * @param key
     * @param couponId
     * @param useIntegral
     * @param shippingType
     * @return
     */
    ComputeDTO computedOrder(int uid, String key, int couponId, int useIntegral, int shippingType);

    /**
     * 通过卡券ID 获取卡券信息和所属公司信息
     * @param couponId
     * @return
     */
    CouponInfoQueryVo getCouponInfo(Integer couponId);
}
