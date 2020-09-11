/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.wechat.web.controller;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.rocketmq.MqProducer;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.MQConstant;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.service.YxOffPayOrderService;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.mp.config.WxMpConfiguration;
import co.yixiang.mp.config.WxPayConfiguration;
import co.yixiang.utils.BigNum;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hyjf.framework.starter.recketmq.MessageContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName WechatController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/5
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "微信模块", tags = "微信:微信模块", description = "微信模块")
public class WechatController extends BaseController {

    private final YxStoreOrderService orderService;
    private final YxSystemConfigService systemConfigService;
    private final YxUserRechargeService userRechargeService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxUserBillService yxUserBillService;
    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private YxOffPayOrderService offPayOrderService;

    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private MqProducer mqProducer;


    /**
     * 微信分享配置
     */
    @AnonymousAccess
    @GetMapping("/share")
    @ApiOperation(value = "微信分享配置", notes = "微信分享配置")
    public ApiResult<Object> share() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("img", systemConfigService.getData(SystemConfigConstants.WECHAT_SHARE_IMG));
        map.put("title", systemConfigService.getData(SystemConfigConstants.WECHAT_SHARE_TITLE));
        map.put("synopsis", systemConfigService.getData(SystemConfigConstants.WECHAT_SHARE_SYNOPSIS));
        Map<String, Object> mapt = new LinkedHashMap<>();
        mapt.put("data", map);
        return ApiResult.ok(mapt);
    }

    /**
     * jssdk配置
     */
    @AnonymousAccess
    @GetMapping("/wechat/config")
    @ApiOperation(value = "jssdk配置", notes = "jssdk配置")
    public ApiResult<Object> jsConfig(@RequestParam(value = "url") String url) throws WxErrorException {
        WxMpService wxService = WxMpConfiguration.getWxMpService();
        WxJsapiSignature jsapiSignature = wxService.createJsapiSignature(url);
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("appId", jsapiSignature.getAppId());
        map.put("jsApiList", new String[]{"updateAppMessageShareData", "openLocation", "scanQRCode",
                "chooseWXPay", "updateAppMessageShareData", "updateTimelineShareData",
                "openAddress", "editAddress", "getLocation"});
        map.put("nonceStr", jsapiSignature.getNonceStr());
        map.put("signature", jsapiSignature.getSignature());
        map.put("timestamp", jsapiSignature.getTimestamp());
        map.put("url", jsapiSignature.getUrl());
        return ApiResult.ok(map);
    }


    /**
     * 微信支付/充值回调
     */
    @AnonymousAccess
    @PostMapping("/wechat/notify")
    @ApiOperation(value = "微信支付充值回调", notes = "微信支付充值回调")
    public String renotify(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            String orderId = notifyResult.getOutTradeNo();
            String attach = notifyResult.getAttach();
            if (BillDetailEnum.TYPE_3.getValue().equals(attach)) {
                YxStoreOrderQueryVo orderInfo = orderService.getOrderInfo(orderId, 0);
                if (orderInfo == null) return WxPayNotifyResponse.success("处理成功!");
                if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(orderInfo.getPaid())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                orderService.paySuccess(orderInfo.getOrderId(), "weixin");
            } else if (BillDetailEnum.TYPE_1.getValue().equals(attach)) {
                //处理充值
                YxUserRecharge userRecharge = userRechargeService.getInfoByOrderId(orderId);
                if (userRecharge == null) return WxPayNotifyResponse.success("处理成功!");
                if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(userRecharge.getPaid())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }

                userRechargeService.updateRecharge(userRecharge);
            }

            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

    }

    /**
     * 微信退款回调
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @AnonymousAccess
    @ApiOperation(value = "退款回调通知处理", notes = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public String parseRefundNotifyResult(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
            String orderId = result.getReqInfo().getOutTradeNo();
            BigDecimal refundFee = BigNum.div(result.getReqInfo().getRefundFee(), 100);
            YxStoreOrderQueryVo orderInfo = orderService.getOrderInfo(orderId, 0);
            log.info("退款回调通知处理 ： " + JSONObject.toJSONString(orderInfo));
            if (orderInfo.getRefundStatus() == 2) {
                return WxPayNotifyResponse.success("处理成功!");
            }
            YxStoreOrder storeOrder = new YxStoreOrder();
            //修改状态
            storeOrder.setId(orderInfo.getId());
            storeOrder.setRefundStatus(2);
            storeOrder.setRefundPrice(refundFee);
            orderService.updateById(storeOrder);
            log.info("退款回调通知处理 ，更新 ： " + JSONObject.toJSONString(storeOrder));
            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException | IllegalAccessException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }


    /**
     * 微信退款回调
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @AnonymousAccess
    @ApiOperation(value = "退款回调通知处理", notes = "退款回调通知处理")
    @PostMapping("/notify/couponRefund")
    public String parseCouponRefundNotifyResult(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
            String orderId = result.getReqInfo().getOutTradeNo();
            BigDecimal refundFee = BigNum.div(result.getReqInfo().getRefundFee(), 100);
            YxCouponOrder yxCouponOrder = yxCouponOrderService.getOrderInfo(orderId, 0);
            if (yxCouponOrder.getRefundStatus() == 2) {
                return WxPayNotifyResponse.success("处理成功!");
            }
            YxCouponOrder couponOrder = new YxCouponOrder();
            //修改状态
            couponOrder.setId(yxCouponOrder.getId());
            couponOrder.setRefundStatus(2);
            couponOrder.setRefundPrice(refundFee);
            couponOrder.setStatus(8);
            yxCouponOrderService.updateById(couponOrder);

            // 插入bill表
            YxUserBill userBill = new YxUserBill();
            userBill.setUid(couponOrder.getUid());
            userBill.setLinkId(couponOrder.getOrderId());
            userBill.setPm(1);
            userBill.setTitle("本地生活订单退款");
            userBill.setCategory("now_money");
            userBill.setType("pay_product_refund");
            userBill.setNumber(refundFee);
            userBill.setBalance(BigDecimal.ZERO);
            userBill.setMark("");
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setStatus(1);
            yxUserBillService.save(userBill);

            // 更新商户余额
            SystemUser systemUser = this.systemUserService.getById(yxCouponOrder.getMerId());
            if (null == systemUser) {
                log.error("订单编号：" + yxCouponOrder.getOrderId() + "未查询到商户所属的id，无法记录退款资金去向");
                return WxPayNotifyResponse.success("处理成功!");
            }
            // 该笔资金实际到账
            SystemUser updateSystemUser = new SystemUser();
            BigDecimal truePrice = yxCouponOrder.getTotalPrice().subtract(yxCouponOrder.getCommission());
            if (refundFee.compareTo(truePrice) > 0) {
                updateSystemUser.setTotalAmount(truePrice.multiply(new BigDecimal(-1)));
                updateSystemUser.setWithdrawalAmount(truePrice.multiply(new BigDecimal(-1)));
            } else {
                updateSystemUser.setTotalAmount(refundFee.multiply(new BigDecimal(-1)));
                updateSystemUser.setWithdrawalAmount(refundFee.multiply(new BigDecimal(-1)));
            }
            updateSystemUser.setId(systemUser.getId());
            this.systemUserService.updateUserTotal(updateSystemUser);

            // 插入商户资金明细
            YxUserBill merBill = new YxUserBill();
            merBill.setUid(yxCouponOrder.getMerId());
            merBill.setLinkId(yxCouponOrder.getOrderId());
            merBill.setPm(0);
            merBill.setTitle("本地生活订单退款");
            merBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
            merBill.setType(BillDetailEnum.TYPE_5.getValue());
            merBill.setNumber(truePrice);
            // 目前只支持微信付款、没有余额
            merBill.setBalance(updateSystemUser.getWithdrawalAmount());
            merBill.setAddTime(DateUtils.getNowTime());
            merBill.setStatus(1);
            merBill.setMerId(yxCouponOrder.getMerId());
            merBill.setUserType(1);
            merBill.setUsername(systemUser.getNickName());
            this.yxUserBillService.save(merBill);

            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException | IllegalAccessException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    /**
     * 微信验证消息
     */
    @AnonymousAccess
    @GetMapping(value = "/wechat/serve", produces = "text/plain;charset=utf-8")
    @ApiOperation(value = "微信验证消息", notes = "微信验证消息")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        final WxMpService wxService = WxMpConfiguration.getWxMpService();
        if (wxService == null) {
            throw new IllegalArgumentException("未找到对应配置的服务，请核实！");
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "fail";
    }


    @AnonymousAccess
    @PostMapping("/wechat/serve")
    @ApiOperation(value = "微信获取消息", notes = "微信获取消息")
    public void post(@RequestBody String requestBody,
                     @RequestParam("signature") String signature,
                     @RequestParam("timestamp") String timestamp,
                     @RequestParam("nonce") String nonce,
                     @RequestParam("openid") String openid,
                     @RequestParam(name = "encrypt_type", required = false) String encType,
                     @RequestParam(name = "msg_signature", required = false) String msgSignature,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        WxMpService wxService = WxMpConfiguration.getWxMpService();

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) return;
            out = outMessage.toXml();
            ;
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) return;

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(out);
        writer.close();
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return WxMpConfiguration.getWxMpMessageRouter().route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }


    @AnonymousAccess
    @PostMapping("/wechat/notifyNew")
    @ApiOperation(value = "微信支付充值回调", notes = "微信支付充值回调")
    public String notifyNew(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            String orderId = notifyResult.getOutTradeNo();
            String attach = notifyResult.getAttach();
            log.info("收到支付异步回调" + JSON.toJSONString(notifyResult));

            // 发送mq异步处理支付结果
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("attach", attach);
                jsonObject.put("orderId", orderId);
                mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_WECHAT_NOTIFY_TAG, UUID.randomUUID().toString(), jsonObject));
            } catch (Exception e) {
                return WxPayNotifyResponse.fail("处理失败");
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }


    @AnonymousAccess
    @PostMapping("/wechat/notifyoffPay")
    @ApiOperation(value = "微信支付线下支付回调", notes = "微信支付线下支付回调")
    public String notifyoffPay(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            String orderId = notifyResult.getOutTradeNo();
            String attach = notifyResult.getAttach();
            log.info("收到微信支付线下支付回调" + JSON.toJSONString(notifyResult));
            if (BillDetailEnum.TYPE_10.getValue().equals(attach)) {
                // 微信支付线下支付回调
                YxOffPayOrder offPayOrder = this.offPayOrderService.getOne(new QueryWrapper<YxOffPayOrder>().eq("order_id", orderId));
                if (offPayOrder == null) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                if ("4".equals(offPayOrder.getStatus())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                offPayOrder.setPayTime(OrderUtil.getSecondTimestampTwo());
                offPayOrder.setStatus(4);
                YxStoreInfoQueryVo storeInfoQueryVo = yxStoreInfoService.getYxStoreInfoById(offPayOrder.getStoreId());


                offPayOrderService.updatePaySuccess(offPayOrder, storeInfoQueryVo);
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    @AnonymousAccess
    @ApiOperation(value = "退款回调通知处理", notes = "退款回调通知处理")
    @PostMapping("/notify/refundNew")
    public String parseRefundNotifyResultNew(@RequestBody String xmlData) {
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
            String orderId = result.getReqInfo().getOutTradeNo();
            BigDecimal refundFee = BigNum.div(result.getReqInfo().getRefundFee(), 100);
            YxStoreOrderQueryVo orderInfo = orderService.getOrderInfo(orderId, 0);
            log.info("退款回调通知处理 ： " + JSONObject.toJSONString(orderInfo));
            if (orderInfo.getRefundStatus() == 2) {
                return WxPayNotifyResponse.success("处理成功!");
            }
            YxStoreOrder storeOrder = new YxStoreOrder();
            //修改状态
            storeOrder.setId(orderInfo.getId());
            storeOrder.setRefundStatus(2);
            storeOrder.setRefundPrice(refundFee);
            orderService.updateById(storeOrder);
            //
            // 插入bill表
            YxUserBill userBill = new YxUserBill();
            userBill.setUid(orderInfo.getUid());
            userBill.setLinkId(orderInfo.getOrderId());
            userBill.setPm(1);
            userBill.setTitle("小程序购买商品订单退款");
            userBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
            userBill.setType(BillDetailEnum.TYPE_5.getValue());
            userBill.setNumber(refundFee);
            userBill.setBalance(BigDecimal.ZERO);
            userBill.setMark("小程序购买商品订单退款");
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setStatus(1);
            userBill.setUserType(3);
            yxUserBillService.save(userBill);

            // 更新商户余额
            SystemUser systemUser = this.systemUserService.getById(orderInfo.getMerId());
            if (ObjectUtil.isEmpty(systemUser)) {
                log.error("订单编号：" + orderInfo.getOrderId() + "未查询到商户所属的id，无法记录退款资金去向");
                return WxPayNotifyResponse.success("处理成功!");
            }
            // 该笔资金实际到账
            SystemUser updateSystemUser = new SystemUser();
            BigDecimal truePrice = orderInfo.getPayPrice().subtract(orderInfo.getCommission());
            if (refundFee.compareTo(truePrice) > 0) {
                updateSystemUser.setTotalAmount(truePrice.multiply(new BigDecimal(-1)));
                updateSystemUser.setWithdrawalAmount(truePrice.multiply(new BigDecimal(-1)));
            } else {
                updateSystemUser.setTotalAmount(refundFee.multiply(new BigDecimal(-1)));
                updateSystemUser.setWithdrawalAmount(refundFee.multiply(new BigDecimal(-1)));
            }
            updateSystemUser.setId(systemUser.getId());
//            this.systemUserService.updateById(updateSystemUser);
            this.systemUserService.updateUserTotal(updateSystemUser);

            // 插入商户资金明细
            YxUserBill merBill = new YxUserBill();
            merBill.setUid(orderInfo.getMerId());
            merBill.setLinkId(orderInfo.getOrderId());
            merBill.setPm(0);
            merBill.setTitle("小程序购买商品订单退款");
            merBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
            merBill.setType(BillDetailEnum.TYPE_5.getValue());
            merBill.setNumber(truePrice);
            // 目前只支持微信付款、没有余额
            merBill.setBalance(updateSystemUser.getWithdrawalAmount());
            merBill.setAddTime(DateUtils.getNowTime());
            merBill.setStatus(1);
            merBill.setMerId(orderInfo.getMerId());
            merBill.setUserType(1);
            merBill.setUsername(systemUser.getNickName());
            merBill.setMark("小程序购买商品订单退款");
            this.yxUserBillService.save(merBill);

            log.info("退款回调通知处理 ，更新 ： " + JSONObject.toJSONString(orderInfo));
            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException | IllegalAccessException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

}
