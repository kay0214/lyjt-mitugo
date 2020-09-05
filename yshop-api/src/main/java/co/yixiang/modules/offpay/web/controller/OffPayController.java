package co.yixiang.modules.offpay.web.controller;

import cn.hutool.core.util.IdUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.monitor.service.RedisService;
import co.yixiang.modules.offpay.web.param.OffPayQueryParam;
import co.yixiang.modules.offpay.web.vo.OffPayStoreInfoVo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @ApiOperation(value = "获取商户信息",notes = "获取商户信息",response = YxCouponOrderQueryVo.class)
    public ApiResult<OffPayStoreInfoVo> userPay(@Valid @RequestBody(required = false) OffPayQueryParam param ) throws Exception{
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
        // todo 插入数据库中
        redisService.saveCode(CommonConstant.USER_OFF_PAY_ORDER+uuid,param.getStoreNid(),0L);

        // get


        return ApiResult.ok(null);
    }



}
