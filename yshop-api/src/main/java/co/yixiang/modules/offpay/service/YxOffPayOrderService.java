package co.yixiang.modules.offpay.service;

import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.offpay.web.param.YxOffPayOrderQueryParam;
import co.yixiang.modules.offpay.web.vo.YxOffPayOrderQueryVo;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 线下支付订单表 服务类
 * </p>
 *
 * @author sss
 * @since 2020-09-05
 */
public interface YxOffPayOrderService extends BaseService<YxOffPayOrder> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxOffPayOrderQueryVo getYxOffPayOrderById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxOffPayOrderQueryParam
     * @return
     */
    Paging<YxOffPayOrderQueryVo> getYxOffPayOrderPageList(YxOffPayOrderQueryParam yxOffPayOrderQueryParam) throws Exception;

    /**
     * 小程序支付
     * @param uuid
     * @param ipAddress
     * @return
     */
    WxPayMpOrderResult wxAppPay(String uuid, String openid, BigDecimal price, String ipAddress) throws WxPayException;

    /**
     * 商户线下支付
     * @param offPayOrder
     */
    void updatePaySuccess(YxOffPayOrder offPayOrder, YxStoreInfoQueryVo storeInfoQueryVo);
}
