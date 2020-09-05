package co.yixiang.modules.offpay.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.util.IpUtils;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.monitor.service.RedisService;
import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.service.YxOffPayOrderService;
import co.yixiang.modules.offpay.web.param.OffPayQueryParam;
import co.yixiang.modules.offpay.web.vo.OffPayStoreInfoVo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商户线下支付
 * @Author : sss
 */
@Slf4j
@RestController
@RequestMapping("/offPay")
@Api(value = "线下支付")
public class OffPayController {

    @Autowired
    private YxSystemGroupDataService yxSystemGroupDataService;

    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private YxOffPayOrderService offPayOrderService;


    @Autowired
    private YxWechatUserService wechatUserService;

    // 扫码获取商户信息  传入加密的store_nid
    @AnonymousAccess
    @PostMapping("/getStoreInfo")
    @ApiOperation(value = "获取商户信息",notes = "获取商户信息",response = YxCouponOrderQueryVo.class)
    public ApiResult<OffPayStoreInfoVo> getYxCouponOrderPageList(@Valid @RequestBody(required = false) OffPayQueryParam param ) throws Exception{
        YxStoreInfoQueryVo storeInfo =  yxStoreInfoService.getYxStoreInfoByNid(param.getStoreNid());
        if(storeInfo==null){
            return ApiResult.fail("查询店铺信息错误");
        }
        OffPayStoreInfoVo result = new OffPayStoreInfoVo();
        result.setStoreMobile(storeInfo.getStoreMobile());
        result.setStoreAddress(storeInfo.getStoreAddress());
        result.setStoreImage(storeInfo.getStoreImage());
        result.setStoreName(storeInfo.getStoreName());
        String uuid = IdUtil.getSnowflake(0, 0).nextIdStr();
        result.setPayRand(uuid);
        // 十分钟内支付有用
        redisService.saveCode(CommonConstant.USER_OFF_PAY+uuid,param.getStoreNid(),600L);
        return ApiResult.ok(result);
    }


    // 输入金额 创建支付订单  获取支付信息
    @AnonymousAccess
    @PostMapping("/userPay")
    @ApiOperation(value = "支付",notes = "支付",response = Map.class)
    public ApiResult<Map<String, Object>> userPay(@Valid @RequestBody(required = false) OffPayQueryParam param , HttpServletRequest request){
        int uid = SecurityUtils.getUserId().intValue();
        String uuid = param.getPayRand();
        if(StringUtils.isBlank(uuid)){
            return ApiResult.fail("请求参数错误");
        }
        String storeNid = redisService.getCodeVal(CommonConstant.USER_OFF_PAY+uuid);
        if(StringUtils.isBlank(storeNid)){
            return ApiResult.fail("支付超时，请重新扫码支付");
        }
        if(!storeNid.equals(param.getStoreNid())){
            return ApiResult.fail("商品信息错误");
        }
        YxStoreInfoQueryVo storeInfo =  yxStoreInfoService.getYxStoreInfoByNid(param.getStoreNid());
        if(storeInfo==null){
            return ApiResult.fail("查询店铺信息错误");
        }
        YxWechatUser wechatUser = wechatUserService.getById(uid);
        if (ObjectUtil.isNull(wechatUser)) throw new ErrorRequestException("用户错误");

        YxOffPayOrder offPayOrder = new YxOffPayOrder();
        offPayOrder.setOrderId(uuid);
        offPayOrder.setUid(0);
        offPayOrder.setStoreNid(storeNid);
        offPayOrder.setStoreName(storeInfo.getStoreName());
        offPayOrder.setStoreId(storeInfo.getId());
        offPayOrder.setTotalPrice(param.getPrice());
        // 订单状态（0:待支付 3:支付失败  4支付成功
        offPayOrder.setStatus(0);
        offPayOrderService.save(offPayOrder);
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("status", "SUCCESS");
            Map<String, String> jsConfig = new HashMap<>();
            map.put("status", "WECHAT_PAY");
            WxPayMpOrderResult wxPayMpOrderResult = offPayOrderService
                    .wxAppPay(uuid, wechatUser.getRoutineOpenid(), param.getPrice(), IpUtils.getIpAddress(request));
            jsConfig.put("appId", wxPayMpOrderResult.getAppId());
            jsConfig.put("timeStamp", wxPayMpOrderResult.getTimeStamp());
            jsConfig.put("nonceStr", wxPayMpOrderResult.getNonceStr());
            jsConfig.put("package", wxPayMpOrderResult.getPackageValue());
            jsConfig.put("signType", wxPayMpOrderResult.getSignType());
            jsConfig.put("paySign", wxPayMpOrderResult.getPaySign());
            map.put("result", jsConfig);
            return ApiResult.ok(map, "订单创建成功");
        } catch (Exception e) {
            log.error("报错了",e);
            return ApiResult.fail(e.getMessage());
        }
    }

}
