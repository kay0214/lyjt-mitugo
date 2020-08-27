package co.yixiang.modules.coupons.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
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
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
@Api("卡券订单表 API")
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

    private static Lock lock = new ReentrantLock(false);


    /**
     * 通过卡券ID, 获取卡券和卡券所属商户信息
     * @param id
     * @return
     */
    @AnonymousAccess
    @GetMapping("/order/couponInfo/{id}")
    @ApiOperation(value = "订单确认",notes = "订单确认")
    public ApiResult<CouponInfoQueryVo> getCouponInfo(@PathVariable Integer id){
        if (id == null){
            return ApiResult.fail("请传入正确的卡券ID");
        }
        if (id <= 0){
            return ApiResult.fail("请传入正确的卡券ID");
        }
        CouponInfoQueryVo couponInfoQueryVo = yxCouponOrderService.getCouponInfo(id);
        return ApiResult.ok(couponInfoQueryVo);
    }

    /**
     * 卡券订单确认
     * @param jsonStr
     * @return
     */
    @PostMapping("/order/confirm")
    @ApiOperation(value = "订单确认",notes = "订单确认")
    public ApiResult<ConfirmOrderDTO> confirm(@RequestBody String jsonStr){
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
        if (StringUtils.isEmpty(couponId)){
            return ApiResult.fail("请传入正确的卡券ID");
        }

        // 查询当前卡券是否存在.
        YxCoupons yxCoupons = yxCouponsService.getById(Integer.valueOf(couponId));
        if (yxCoupons == null){
            return ApiResult.fail("请传入正确的卡券ID!");
        }
        if (yxCoupons.getInventory() < Integer.valueOf(quantity)){
            return ApiResult.fail("卡券库存不足,无法购买!");
        }

        // 购买卡券信息
        List<YxCoupons> couponsList = yxCouponsService.list(new QueryWrapper<YxCoupons>().eq("id", Integer.valueOf(couponId)).eq("del_flag", 0));
        if (couponsList.size() <= 0){
            return ApiResult.fail("未查询到卡券信息,请确认!");
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
        confirmOrderDTO.setOrderKey(yxCouponOrderService.cacheOrderInfo(uid, queryVoList, priceGroup));

        confirmOrderDTO.setUserInfo(userService.getYxUserById(uid));

        //门店
        confirmOrderDTO.setSystemStore(systemStoreService.getStoreInfo("",""));

        return ApiResult.ok(confirmOrderDTO);
    }

    /**
     * 创建订单
     * @param param
     * @param key
     * @return
     */
    @PostMapping("/order/create/{key}")
    @ApiOperation(value = "订单创建",notes = "订单创建")
    public ApiResult<Map<String,Object>> create(@Valid @RequestBody OrderParam param, @PathVariable String key){

        Map<String,Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if(StrUtil.isEmpty(key)) return ApiResult.fail("参数错误");

        // 校验订单是否已存在
        YxCouponOrder couponOrder = yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("`unique`", key).eq("del_flag", 0));
        if(ObjectUtil.isNotNull(couponOrder)){
            map.put("status","EXTEND_ORDER");
            OrderExtendDTO orderExtendDTO = new OrderExtendDTO();
            orderExtendDTO.setKey(key);
            orderExtendDTO.setOrderId(couponOrder.getOrderId());
            map.put("result",orderExtendDTO);
            return ApiResult.ok(map,"订单已生成");
        }
        if(param.getFrom().equals("weixin"))  param.setIsChannel(0);

        //创建订单
        YxCouponOrder order = null;
        try{
            lock.lock();
            order = yxCouponOrderService.createOrder(uid, key, param);
        }finally {
            lock.unlock();
        }


        if(ObjectUtil.isNull(order)) throw new ErrorRequestException("订单生成失败");

        String orderId = order.getOrderId();

        OrderExtendDTO orderDTO = new OrderExtendDTO();
        orderDTO.setKey(key);
        orderDTO.setOrderId(orderId);
        map.put("status","SUCCESS");
        map.put("result",orderDTO);
        //开始处理支付
        if(StrUtil.isNotEmpty(orderId)){
            //处理金额为0的情况
            if(order.getTotalPrice().doubleValue() <= 0){
                yxCouponOrderService.yuePay(orderId,uid);
                return ApiResult.ok(map,"支付成功");
            }
        }
        return ApiResult.ok(map,"订单创建成功");
    }

    /**
     * 计算订单金额
     */
    @PostMapping("/order/computed/{key}")
    @ApiOperation(value = "计算订单金额",notes = "计算订单金额")
    public ApiResult<Map<String,Object>> computedOrder(@RequestBody String jsonStr,
                                                       @PathVariable String key){

        Map<String,Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if(StrUtil.isEmpty(key)){ return ApiResult.fail("参数错误");}

        YxCouponOrder storeOrder = yxCouponOrderService.getOrderInfo(key,uid);
        if(ObjectUtil.isNotNull(storeOrder)){
            map.put("status","EXTEND_ORDER");
            OrderExtendDTO orderExtendDTO = new OrderExtendDTO();
            orderExtendDTO.setKey(key);
            orderExtendDTO.setOrderId(storeOrder.getOrderId());
            map.put("result",orderExtendDTO);
            return ApiResult.ok(map,"订单已生成");
        }

        ComputeDTO computeDTO = yxCouponOrderService.computedOrder(uid, key, 0, 0, 2);

        map.put("result",computeDTO);
        map.put("status","NONE");
        return ApiResult.ok(map);
    }

    /**
    * 添加卡券订单表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponOrder对象",notes = "添加卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception{
        boolean flag = yxCouponOrderService.save(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
    * 修改卡券订单表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponOrder对象",notes = "修改卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception{
        boolean flag = yxCouponOrderService.updateById(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
    * 删除卡券订单表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponOrder对象",notes = "删除卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponOrderService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取卡券订单表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponOrder对象详情",notes = "查看卡券订单表",response = YxCouponOrderQueryVo.class)
    public ApiResult<YxCouponOrderQueryVo> getYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponOrderQueryVo yxCouponOrderQueryVo = yxCouponOrderService.getYxCouponOrderById(idParam.getId());
        return ApiResult.ok(yxCouponOrderQueryVo);
    }

    /**
     * 卡券订单表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponOrder分页列表",notes = "卡券订单表分页列表",response = YxCouponOrderQueryVo.class)
    public ApiResult<Paging<YxCouponOrderQueryVo>> getYxCouponOrderPageList(@Valid @RequestBody(required = false) YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception{
        Paging<YxCouponOrderQueryVo> paging = yxCouponOrderService.getYxCouponOrderPageList(yxCouponOrderQueryParam);
        return ApiResult.ok(paging);
    }

}

