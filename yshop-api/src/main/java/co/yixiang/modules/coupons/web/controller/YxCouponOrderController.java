package co.yixiang.modules.coupons.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.util.IpUtils;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponComfirmRideParam;
import co.yixiang.modules.coupons.web.param.YxCouponOrderPassengParam;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.CouponInfoQueryVo;
import co.yixiang.modules.coupons.web.vo.CouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.order.web.dto.ComputeDTO;
import co.yixiang.modules.order.web.dto.ConfirmOrderDTO;
import co.yixiang.modules.order.web.dto.OrderExtendDTO;
import co.yixiang.modules.order.web.dto.PriceGroupDTO;
import co.yixiang.modules.order.web.param.OrderParam;
import co.yixiang.modules.order.web.param.PayParam;
import co.yixiang.modules.order.web.param.RefundParam;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.user.service.YxUsedContactsService;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 卡券订单表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponOrder")
@Api(value = "本地生活订单", tags = "本地生活:订单", description = "订单")
public class YxCouponOrderController extends BaseController {

    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    @Autowired
    private YxCouponsService yxCouponsService;

    @Autowired
    private YxUserAddressService yxUserAddressService;

    @Autowired
    private YxUserService userService;

    @Autowired
    private YxSystemStoreService systemStoreService;
    @Autowired
    private YxUsedContactsService yxUsedContactsService;

    @Autowired
    private YxShipPassengerService yxShipPassengerService;
    /**
     * 通过卡券ID, 获取卡券和卡券所属商户信息
     *
     * @param id
     * @return
     */
    @AnonymousAccess
    @GetMapping("/order/couponInfo/{id}")
    @ApiOperation(value = "订单确认", notes = "订单确认")
    public ApiResult<CouponInfoQueryVo> getCouponInfo(@PathVariable Integer id) {
        if (id == null) {
            return ApiResult.fail("请传入正确的卡券ID");
        }
        if (id <= 0) {
            return ApiResult.fail("请传入正确的卡券ID");
        }
        CouponInfoQueryVo couponInfoQueryVo = yxCouponOrderService.getCouponInfo(id);
        return ApiResult.ok(couponInfoQueryVo);
    }

    /**
     * 卡券订单确认
     *
     * @param jsonStr
     * @return
     */
    @PostMapping("/order/confirm")
    @ApiOperation(value = "订单确认", notes = "订单确认")
    public ApiResult<ConfirmOrderDTO> confirm(@RequestBody String jsonStr) {
        /**
         * 卡券无购物车,
         * 因此订单确认需要通过前端传入的信息校验商品信息.(商品状态, 当前是否可以购买, 当前价格与订单提交时商品价格是否一致等)
         */
        // 当前登录用户ID
        int uid = SecurityUtils.getUserId().intValue();
//        int uid = 27;

        // jsonStr 需要传入卡券主键(获取当前商品信息)和购买卡券数量(计算订单总金额)

        // 没有购物车, 一次只能购买一个产品.
        // 但是一个产品可以购买多个.
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        // 卡券主键
        String couponId = jsonObject.getString("couponId");
        // 购买数量
        String quantity = jsonObject.getString("quantity");
        if (StringUtils.isEmpty(couponId)) {
            return ApiResult.fail("请传入正确的卡券ID");
        }

        // 查询当前卡券是否存在.
        YxCoupons yxCoupons = yxCouponsService.getById(Integer.valueOf(couponId));
        if (yxCoupons == null) {
            return ApiResult.fail("请传入正确的卡券ID!");
        }
        try {
            if (yxCoupons.getInventory() < Integer.valueOf(quantity)) {
                return ApiResult.fail("卡券库存不足,无法购买!");
            }
        } catch (NumberFormatException e) {
            log.info("卡券订单确认接口传值：" + jsonStr);
            throw new BadRequestException("系统异常，请稍后重试");
        }

        // 购买卡券信息
        List<YxCoupons> couponsList = yxCouponsService.list(new QueryWrapper<YxCoupons>().eq("id", Integer.valueOf(couponId)).eq("del_flag", 0));
        if (couponsList.size() <= 0) {
            return ApiResult.fail("未查询到卡券信息,请确认!");
        }
        // 获取价格配置
        for (YxCoupons coupons : couponsList) {
            YxCouponsPriceConfig yxCouponsPriceConfig = yxCouponsService.getPirceConfig(coupons.getId());
            if (null != yxCouponsPriceConfig) {
                //佣金
                coupons.setCommission(yxCouponsPriceConfig.getCommission());
                //销售价格
                coupons.setSellingPrice(yxCouponsPriceConfig.getSellingPrice());
            }
        }
        CouponOrderQueryVo couponOrderQueryVo = new CouponOrderQueryVo();
        List<YxCouponsQueryVo> queryVoList = CommonsUtils.convertBeanList(couponsList, YxCouponsQueryVo.class);
        couponOrderQueryVo.setYxCouponsQueryVoList(queryVoList);
        couponOrderQueryVo.setQuantity(Integer.valueOf(quantity));
        PriceGroupDTO priceGroup = yxCouponOrderService.getOrderPriceGroup(couponOrderQueryVo);

        // 取出商品相关信息.
        ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
        confirmOrderDTO.setAddressInfo(yxUserAddressService.getUserDefaultAddress(uid));

        confirmOrderDTO.setPriceGroup(priceGroup);
        // 这里把购买信息塞入redis、然后后面的查询总价接口和订单入库都从redis取值
        confirmOrderDTO.setOrderKey(yxCouponOrderService.cacheOrderInfo(uid, queryVoList, Integer.valueOf(quantity), priceGroup));

        confirmOrderDTO.setUserInfo(userService.getYxUserById(uid));

        //门店
        confirmOrderDTO.setSystemStore(systemStoreService.getStoreInfo("", ""));

        return ApiResult.ok(confirmOrderDTO);
    }

    /**
     * 创建订单
     *
     * @param param
     * @param key
     * @return
     */
    @PostMapping("/order/create/{key}")
    @ApiOperation(value = "订单创建", notes = "订单创建")
    public ApiResult<Map<String, Object>> create(@Valid @RequestBody OrderParam param, @PathVariable String key, HttpServletRequest request) {

        Map<String, Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if (StrUtil.isEmpty(key)) {
            return ApiResult.fail("参数错误");
        }

        // 校验订单是否已存在
        YxCouponOrder couponOrder = yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("`unique`", key).eq("del_flag", 0));
        if (ObjectUtil.isNotNull(couponOrder)) {
            map.put("status", "EXTEND_ORDER");
            OrderExtendDTO orderExtendDTO = new OrderExtendDTO();
            orderExtendDTO.setKey(key);
            orderExtendDTO.setOrderId(couponOrder.getOrderId());
            map.put("result", orderExtendDTO);
            return ApiResult.ok(map, "订单已生成");
        }
        if (param.getFrom().equals("weixin")) {
            param.setIsChannel(0);
        }

        String lockKey = CommonConstant.LOCK_COUPON_COMMIT_ORDER + param.getCouponId();
        String requestId = UUID.randomUUID().toString();
        //创建订单
        YxCouponOrder order = null;
        while (true) {
            if (RedisUtil.tryGetLock(lockKey, requestId, 5)) {
                try {
                    order = yxCouponOrderService.createOrder(uid, key, param);
                    break;
                } catch (ErrorRequestException e) {
                    throw e;
                } finally {
                    RedisUtil.releaseLock(lockKey, requestId);
                }
            }
        }

        if (ObjectUtil.isNull(order)) {
            throw new ErrorRequestException("订单生成失败");
        }

        String orderId = order.getOrderId();

        OrderExtendDTO orderDTO = new OrderExtendDTO();
        orderDTO.setKey(key);
        orderDTO.setOrderId(orderId);
        orderDTO.setId(order.getId());
        map.put("status", "SUCCESS");
        map.put("result", orderDTO);
        //开始处理支付
        if (StrUtil.isNotEmpty(orderId)) {
            //处理金额为0的情况
            if (order.getTotalPrice().doubleValue() <= 0) {
                yxCouponOrderService.yuePay(orderId, uid);
                return ApiResult.ok(map, "支付成功");
            }
            try {
                Map<String, String> jsConfig = new HashMap<>();
                if (param.getFrom().equals("routine")) {
                    map.put("status", "WECHAT_PAY");
                    WxPayMpOrderResult wxPayMpOrderResult = yxCouponOrderService
                            .wxAppPay(orderId, IpUtils.getIpAddress(request));
                    jsConfig.put("appId", wxPayMpOrderResult.getAppId());
                    jsConfig.put("timeStamp", wxPayMpOrderResult.getTimeStamp());
                    jsConfig.put("nonceStr", wxPayMpOrderResult.getNonceStr());
                    jsConfig.put("package", wxPayMpOrderResult.getPackageValue());
                    jsConfig.put("signType", wxPayMpOrderResult.getSignType());
                    jsConfig.put("paySign", wxPayMpOrderResult.getPaySign());
                    orderDTO.setJsConfig(jsConfig);
                    map.put("result", orderDTO);
                    return ApiResult.ok(map, "订单创建成功");
                }

            } catch (Exception e) {
                return ApiResult.fail(e.getMessage());
            }
        }
        return ApiResult.ok(map, "订单创建成功");
    }

    /**
     * 订单支付
     */
    @Log(value = "订单支付", type = 1)
    @PostMapping("/order/pay")
    @ApiOperation(value = "订单支付", notes = "订单支付")
    public ApiResult<Map<String, Object>> pay(@Valid @RequestBody PayParam param, HttpServletRequest request) {

        Map<String, Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if (StrUtil.isEmpty(param.getUni())) return ApiResult.fail("参数错误");

        YxCouponOrder order = yxCouponOrderService
                .getOrderInfo(param.getUni(), uid);
        if (ObjectUtil.isNull(order)) return ApiResult.fail("订单不存在");

        if (order.getPayStaus().equals(OrderInfoEnum.REFUND_STATUS_1.getValue())) return ApiResult.fail("该订单已支付");


        String orderId = order.getOrderId();
        YxCoupons yxCoupons = this.yxCouponsService.getById(order.getCouponId());
        Integer buyCount = this.yxCouponOrderService.getBuyCount(uid, order.getCouponId());
        // 待付款的订单都已入库、这里不用再累加
//        buyCount = buyCount + order.getTotalNum();
        if (buyCount > yxCoupons.getQuantityLimit()) {
            throw new BadRequestException("当前购买卡券数量超限");
        }
        if (0 == yxCoupons.getIsShow() || 1 == yxCoupons.getDelFlag()) {
            throw new BadRequestException("当前卡券已被下架");
        }

        OrderExtendDTO orderDTO = new OrderExtendDTO();

        orderDTO.setOrderId(orderId);
        map.put("status", "SUCCESS");
        map.put("result", orderDTO);
        //开始处理支付
        if (StrUtil.isNotEmpty(orderId)) {
            try {
                Map<String, String> jsConfig = new HashMap<>();
                if (param.getFrom().equals("routine")) {
                    map.put("status", "WECHAT_PAY");
                    WxPayMpOrderResult wxPayMpOrderResult = yxCouponOrderService
                            .wxAppPay(orderId, IpUtils.getIpAddress(request));
                    jsConfig.put("appId", wxPayMpOrderResult.getAppId());
                    jsConfig.put("timeStamp", wxPayMpOrderResult.getTimeStamp());
                    jsConfig.put("nonceStr", wxPayMpOrderResult.getNonceStr());
                    jsConfig.put("package", wxPayMpOrderResult.getPackageValue());
                    jsConfig.put("signType", wxPayMpOrderResult.getSignType());
                    jsConfig.put("paySign", wxPayMpOrderResult.getPaySign());
                    orderDTO.setJsConfig(jsConfig);
                    map.put("result", orderDTO);
                    return ApiResult.ok(map, "订单创建成功");
                }
            } catch (Exception e) {
                return ApiResult.fail(e.getMessage());
            }
        }
        return ApiResult.fail("订单生成失败");
    }

    /**
     * 计算订单金额
     */
    @PostMapping("/order/computed/{key}")
    @ApiOperation(value = "计算订单金额", notes = "计算订单金额")
    public ApiResult<Map<String, Object>> computedOrder(@RequestBody String jsonStr,
                                                        @PathVariable String key) {

        Map<String, Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if (StrUtil.isEmpty(key)) {
            return ApiResult.fail("参数错误");
        }

        YxCouponOrder storeOrder = yxCouponOrderService.getOrderInfo(key, uid);
        if (ObjectUtil.isNotNull(storeOrder)) {
            map.put("status", "EXTEND_ORDER");
            OrderExtendDTO orderExtendDTO = new OrderExtendDTO();
            orderExtendDTO.setKey(key);
            orderExtendDTO.setOrderId(storeOrder.getOrderId());
            map.put("result", orderExtendDTO);
            return ApiResult.ok(map, "订单已生成");
        }

        ComputeDTO computeDTO = yxCouponOrderService.computedOrder(uid, key, 0, 0, 2);

        map.put("result", computeDTO);
        map.put("status", "NONE");
        return ApiResult.ok(map);
    }

    /**
     * 添加卡券订单表
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponOrder对象", notes = "添加卡券订单表", response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception {
        boolean flag = yxCouponOrderService.save(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
     * 修改卡券订单表
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponOrder对象", notes = "修改卡券订单表", response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception {
        boolean flag = yxCouponOrderService.updateById(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
     * 删除卡券订单表
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponOrder对象", notes = "删除卡券订单表", response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception {
        boolean flag = yxCouponOrderService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

//    /**
//    * 获取卡券订单表
//    */
//    @PostMapping("/info")
//    @ApiOperation(value = "获取YxCouponOrder对象详情",notes = "查看卡券订单表",response = YxCouponOrderQueryVo.class)
//    public ApiResult<YxCouponOrderQueryVo> getYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception{
//        YxCouponOrderQueryVo yxCouponOrderQueryVo = yxCouponOrderService.getYxCouponOrderById(idParam.getId());
//        return ApiResult.ok(yxCouponOrderQueryVo);
//    }

//    /**
//     * 卡券订单表分页列表
//     */
//    @PostMapping("/getPageList")
//    @ApiOperation(value = "获取YxCouponOrder分页列表",notes = "卡券订单表分页列表",response = YxCouponOrderQueryVo.class)
//    public ApiResult<Paging<YxCouponOrderQueryVo>> getYxCouponOrderPageList(@Valid @RequestBody(required = false) YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception{
//        Paging<YxCouponOrderQueryVo> paging = yxCouponOrderService.getYxCouponOrderPageList(yxCouponOrderQueryParam);
//        return ApiResult.ok(paging);
//    }

    /**
     * 提交订单退款审核
     */
    @Log(value = "提交订单退款", type = 1)
    @PostMapping("/order/refund/verify")
    @ApiOperation(value = "订单退款审核", notes = "订单退款审核")
    public ApiResult<Object> refundVerify(@RequestBody RefundParam param) {
        int uid = SecurityUtils.getUserId().intValue();
        yxCouponOrderService.orderApplyRefund(param, uid);
        return ApiResult.ok("ok");
    }

    /**
     * 个人中心 我的卡券列表
     * 传订单状态 uid
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "个人中心 我的卡券列表", notes = "卡券列表")
    public ApiResult<List<YxCouponOrderQueryVo>> getMyCouponOrderPageList(@Valid @RequestBody(required = false) YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        List<YxCouponOrderQueryVo> paging = yxCouponOrderService.getMyCouponOrderPageList(yxCouponOrderQueryParam, uid);
        return ApiResult.ok(paging);
    }

    /**
     * 获取卡券订单表
     */
    @AnonymousAccess
    @PostMapping("/info")
    @ApiOperation(value = "查看卡券订单详情", notes = "卡券订单详情")
    public ApiResult<YxCouponOrderQueryVo> getMyCouponOrder(@Valid @RequestBody IdParam idParam, @RequestHeader(value = "location", required = false) String location) throws Exception {
        YxCouponOrderQueryVo yxCouponOrderQueryVo = yxCouponOrderService.getYxCouponOrderDetail(idParam.getId(), location);
        return ApiResult.ok(yxCouponOrderQueryVo);
    }

    /**
     * 订单统计数据
     */
    @GetMapping("/couponOrder/data")
    @ApiOperation(value = "订单统计数据", notes = "订单统计数据")
    public ApiResult<Object> orderData() {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(yxCouponOrderService.orderData(uid));
    }

    /**
     * 订单取消
     */
    @PostMapping("/cancel")
    @ApiOperation(value = "取消YxCouponOrder对象", notes = "取消卡券订单表", response = ApiResult.class)
    public ApiResult<Boolean> cancelYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception {
        boolean flag = yxCouponOrderService.updateOrderStatusCancel(idParam.getId());
        return ApiResult.result(flag);
    }

    @GetMapping("/buyCount/{couponId}/{cartNum}")
    @ApiOperation(value = "查询该用户已购买张数", notes = "查询该用户已购买张数")
    public ApiResult<Boolean> buyCount(@PathVariable(value = "couponId") Integer couponId, @PathVariable(value = "cartNum") Integer cartNum) {
        int uid = SecurityUtils.getUserId().intValue();
        Integer count = this.yxCouponOrderService.getBuyCount(uid, couponId);
        Integer totalCount = cartNum + count;
        YxCoupons yxCoupons = this.yxCouponsService.getById(couponId);
        if (yxCoupons.getQuantityLimit() < totalCount) {
            return ApiResult.ok(false);
        }
        return ApiResult.ok(true);
    }


    /**
     * 获取卡券订单表
     */
    @AnonymousAccess
    @PostMapping("/getUsedContactsList")
    @ApiOperation(value = "选择乘客", notes = "选择乘客")
    public ApiResult<Paging<YxUsedContactsQueryVo>> getUsedContactsByUserId(@Valid @RequestBody YxCouponOrderPassengParam yxCouponOrderQueryParam){
        Paging<YxUsedContactsQueryVo> yxUsedContactsOrderQueryVo = yxUsedContactsService.getUsedContactsByUserId(yxCouponOrderQueryParam);
        return ApiResult.ok(yxUsedContactsOrderQueryVo);
    }

    /**
     * 确认乘坐
     * @param param
     * @return
     */
    @PostMapping("/confirmRide")
    @ApiOperation(value = "确认乘坐", notes = "确认乘坐")
    public ApiResult<Boolean> confirmRide(@Valid @RequestBody YxCouponComfirmRideParam param){
        boolean isFlg =yxShipPassengerService.saveComfrieRideInfo(param);
        return ApiResult.ok(isFlg);
    }

    @AnonymousAccess
    @GetMapping("/getBatchPassenger/{orderId}")
    @ApiOperation(value = "查看乘船人", notes = "查看乘船人")
    public ApiResult<List<YxShipPassengerQueryVo>> getBatchPassenger(@PathVariable(value = "orderId") Integer orderId){
        List<YxShipPassengerQueryVo> passengerQueryVoList =yxShipPassengerService.getPassengerByOrderId(orderId);
        return ApiResult.ok(passengerQueryVoList);
    }

}

