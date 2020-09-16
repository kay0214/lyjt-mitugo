package co.yixiang.modules.order.service;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.web.dto.OrderDataDTO;
import co.yixiang.modules.manage.web.dto.OrderTimeDataDTO;
import co.yixiang.modules.manage.web.param.OrderDeliveryParam;
import co.yixiang.modules.manage.web.param.OrderPriceParam;
import co.yixiang.modules.manage.web.param.OrderRefundParam;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.web.dto.*;
import co.yixiang.modules.order.web.param.*;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreStoreCartQueryVo;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxStoreOrderService extends BaseService<YxStoreOrder> {

    Map<String, Object> chartCount(int cate, int type);

    void orderRefund(OrderRefundParam param);

    void orderDelivery(OrderDeliveryParam param);

    void editOrderPrice(OrderPriceParam param);

    List<OrderDataDTO> getOrderDataPriceCount(int page, int limit);

    OrderTimeDataDTO getOrderTimeData();

    YxStoreOrder getOrderPink(int pid, int uid, int type);

    void regressionCoupon(YxStoreOrderQueryVo order);

    void regressionStock(YxStoreOrderQueryVo order);

    void regressionIntegral(YxStoreOrderQueryVo order);

    void cancelOrder(String orderId, int uid);

    void cancelOrderByTask(int id);

    void orderApplyRefund(RefundParam param, int uid);

    void removeOrder(String orderId, int uid);

    void gainUserIntegral(YxStoreOrderQueryVo order);

    void takeOrder(String orderId, int uid);

    void verificOrder(String orderId);

    List<YxStoreOrderQueryVo> orderList(int uid, int type, int page, int limit);

    //@WebMethod
    OrderCountDTO orderData(int uid);

    YxStoreOrderQueryVo handleOrder(YxStoreOrderQueryVo order);

    void paySuccess(String orderId, String payType);

    void yuePay(String orderId, int uid);

    WxPayMpOrderResult wxAppPay(String orderId,String ip) throws WxPayException;

    WxPayMpOrderResult wxPay(String orderId) throws WxPayException;

    WxPayMwebOrderResult wxH5Pay(String orderId) throws WxPayException;

    WxPayAppOrderResult appPay(String orderId) throws WxPayException;

    String aliPay(String orderId) throws Exception;

    void delCacheOrderInfo(int uid, String key);

    YxStoreOrder createOrder(int uid, String key, OrderParam param);

    ComputeDTO computedOrder(int uid, String key, int couponId,
                             int useIntegral, int shippingType);

    YxStoreOrderQueryVo getOrderInfo(String unique, int uid);

    String cacheOrderInfo(int uid, List<YxStoreCartQueryVo> cartInfo,
                          PriceGroupDTO priceGroup, OtherDTO other);

    CacheDTO getCacheOrderInfo(int uid, String key);

    PriceGroupDTO getOrderPriceGroup(List<YxStoreCartQueryVo> cartInfo);

    Double getOrderSumPrice(List<YxStoreCartQueryVo> cartInfo, String key);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxStoreOrderQueryVo getYxStoreOrderById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param yxStoreOrderQueryParam
     * @return
     */
    Paging<YxStoreOrderQueryVo> getYxStoreOrderPageList(YxStoreOrderQueryParam yxStoreOrderQueryParam) throws Exception;

    String cacheOrderStroeInfo(int uid, List<YxStoreStoreCartQueryVo> cartInfo);

    /**
     * 创建订单（同一商铺为同一订单）
     * @param uid
     * @param key
     * @param param
     * @return
     */
    public List<YxStoreOrder> createOrderNew(int uid, String key, OrderNewParam param);

    /**
     * 订单信息列表
     *
     * @param unique
     * @param uid
     * @return
     */
    public List<YxStoreOrderQueryVo> getOrderInfoList(String unique, int uid);
    /**
     * 余额支付
     *
     * @param orderIdList 订单号
     * @param uid     用户id
     */
    public void yuePayOrderList(List<String> orderIdList, int uid);

    /**
     * 小程序支付(订单列表）
     *
     * @param orderIdList
     * @return
     * @throws WxPayException
     */
    WxPayMpOrderResult wxAppPayList(List<String> orderIdList,String payNo,String ip) throws WxPayException;

    /**
     * 支付成功后操作（多订单）
     *
     * @param orderId 订单号
     * @param payType 支付方式
     */
    void paySuccessNew(String orderId, String payType);

    /**
     * 获取店铺名称
     * @param storeId
     * @return
     */
    String getStoreName(Integer storeId);

    ComputeDTO computedOrderNew(int uid, String key,List<Integer> couponIds);


    List<OrderCartInfoDTO> getReplyProductList(String unique);

    ApiResult<Object> replyOrderInfo(OrderReplyParam replyParam, int uid);
}
