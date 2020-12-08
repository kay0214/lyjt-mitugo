/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.param.RechargeParam;
import co.yixiang.mp.service.YxPayService;
import co.yixiang.utils.SecurityUtils;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 用户充值 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-03-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户充值", tags = "用户:用户充值", description = "用户充值")
public class UserRechargeController extends BaseController {

    private final YxUserRechargeService userRechargeService;
    private final YxSystemConfigService systemConfigService;
    private final YxPayService payService;
    private final YxWechatUserService wechatUserService;
    private final YxSystemGroupDataService systemGroupDataService;

    /**
     * 充值方案
     */
    @GetMapping("/recharge/index")
    @ApiOperation(value = "充值方案", notes = "充值方案", response = ApiResult.class)
    public ApiResult<Object> getWays() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("recharge_price_ways", systemGroupDataService.getDatas(ShopConstants.YSHOP_RECHARGE_PRICE_WAYS));
        return ApiResult.ok(map);
    }

    /**
     * 公众号充值/H5充值
     */
    @PostMapping("/recharge/wechat")
    @ApiOperation(value = "公众号充值/H5充值", notes = "公众号充值/H5充值", response = ApiResult.class)
    public ApiResult<Map<String, Object>> add(@Valid @RequestBody RechargeParam param) {
        int uid = SecurityUtils.getUserId().intValue();
        String money = systemConfigService.getData(SystemConfigConstants.STORE_USER_MIN_RECHARGE);
        Double newMoney = 0d;
        if (StrUtil.isNotEmpty(money)) newMoney = Double.valueOf(money);
        if (newMoney > param.getPrice()) throw new ErrorRequestException("充值金额不能低于" + newMoney);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", param.getFrom());

        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();

        param.setOrderSn(orderSn);
        userRechargeService.addRecharge(param, uid);

        BigDecimal bigDecimal = new BigDecimal(100);
        int price = bigDecimal.multiply(BigDecimal.valueOf(param.getPrice())).intValue();
        try {
            if (AppFromEnum.WEIXIN_H5.getValue().equals(param.getFrom())) {
                WxPayMwebOrderResult result = payService.wxH5Pay(orderSn, "H5充值", price,
                        BillDetailEnum.TYPE_1.getValue());
                map.put("data", result.getMwebUrl());
            } else if (AppFromEnum.ROUNTINE.getValue().equals(param.getFrom())) {
                YxWechatUser wechatUser = wechatUserService.getById(uid);
                if (ObjectUtil.isNull(wechatUser)) throw new ErrorRequestException("用户错误");
                WxPayMpOrderResult result = payService.routinePay(orderSn, "小程序充值", wechatUser.getRoutineOpenid(), price,
                        BillDetailEnum.TYPE_1.getValue());
                Map<String, String> jsConfig = new HashMap<>();
                jsConfig.put("appId", result.getAppId());
                jsConfig.put("timeStamp", result.getTimeStamp());
                jsConfig.put("nonceStr", result.getNonceStr());
                jsConfig.put("package", result.getPackageValue());
                jsConfig.put("signType", result.getSignType());
                jsConfig.put("paySign", result.getPaySign());
                map.put("data", jsConfig);
            } else if (AppFromEnum.APP.getValue().equals(param.getFrom())) {
                WxPayAppOrderResult result = payService.appPay(orderSn, "app充值", price,
                        BillDetailEnum.TYPE_1.getValue());
                Map<String, String> jsConfig = new HashMap<>();
                jsConfig.put("appid", result.getAppId());
                jsConfig.put("partnerid", result.getPartnerId());
                jsConfig.put("prepayid", result.getPrepayId());
                jsConfig.put("package", result.getPackageValue());
                jsConfig.put("noncestr", result.getNonceStr());
                jsConfig.put("timestamp", result.getTimeStamp());
                jsConfig.put("sign", result.getSign());
                map.put("data", jsConfig);
            } else {
                YxWechatUser wechatUser = wechatUserService.getById(uid);
                if (ObjectUtil.isNull(wechatUser)) throw new ErrorRequestException("用户错误");
                WxPayMpOrderResult result = payService.wxPay(orderSn, wechatUser.getOpenid(),
                        "公众号充值", price, BillDetailEnum.TYPE_1.getValue());
                Map<String, String> jsConfig = new HashMap<>();
                jsConfig.put("appId", result.getAppId());
                jsConfig.put("timestamp", result.getTimeStamp());
                jsConfig.put("nonceStr", result.getNonceStr());
                jsConfig.put("package", result.getPackageValue());
                jsConfig.put("signType", result.getSignType());
                jsConfig.put("paySign", result.getPaySign());
                map.put("data", jsConfig);
            }
        } catch (WxPayException e) {
            return ApiResult.fail(e.getMessage());
        }


        return ApiResult.ok(map);
    }


}

