/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.shop.service.YxStoreCartService;
import co.yixiang.modules.shop.web.param.CartIdsParm;
import co.yixiang.modules.shop.web.param.YxStoreCartQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreStoreCartVo;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 购物车控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
@Slf4j
@RestController
@Api(value = "购物车", tags = "商城:购物车", description = "购物车")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreCartController extends BaseController {

    private final YxStoreCartService storeCartService;

    /**
     * 购物车 获取数量
     */
    @GetMapping("/cart/count")
    @ApiOperation(value = "获取数量", notes = "获取数量")
    public ApiResult<Map<String, Object>> count(YxStoreCartQueryParam queryParam) {
        Map<String, Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        map.put("count", storeCartService.getUserCartNum(uid, "product", queryParam.getNumType().intValue()));
        return ApiResult.ok(map);
    }

    /**
     * 购物车 添加
     */
    @PostMapping("/cart/add")
    @ApiOperation(value = "添加购物车", notes = "添加购物车")
    public ApiResult<Map<String, Object>> add(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Map<String, Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
//        int uid = 27;
        if (ObjectUtil.isNull(jsonObject.get("cartNum")) || ObjectUtil.isNull(jsonObject.get("productId"))) {
            return ApiResult.fail("参数有误");
        }
        String shareUserId = "";
        int spreadId = 0;
        log.info("加入购物车，推荐人id：{}",jsonObject.get("spread"));
        if (ObjectUtil.isNotNull(jsonObject.get("spread"))) {
            //分享人id
            shareUserId = jsonObject.getString("spread");
            spreadId = Integer.parseInt(shareUserId);
        }
        /*BigDecimal bigDecimalComm = BigDecimal.ZERO;
        if(ObjectUtil.isNotNull(jsonObject.get("commission"))){
            //佣金
            bigDecimalComm=jsonObject.getBigDecimal("commission");
        }*/
        Integer cartNum = jsonObject.getInteger("cartNum");
        if (ObjectUtil.isNull(cartNum)) {
            return ApiResult.fail("购物车数量参数有误");
        }
        if (cartNum <= 0) {
            return ApiResult.fail("购物车数量必须大于0");
        }
        int isNew = 1;
        if (ObjectUtil.isNotNull(jsonObject.get("new"))) {
            isNew = jsonObject.getInteger("new");
        }
        Integer productId = jsonObject.getInteger("productId");
        if (ObjectUtil.isNull(productId)) {
            return ApiResult.fail("产品参数有误");
        }
        String uniqueId = jsonObject.get("uniqueId").toString();

        //拼团
        int combinationId = 0;
        if (ObjectUtil.isNotNull(jsonObject.get("combinationId"))) {
            combinationId = jsonObject.getInteger("combinationId");
        }

        //秒杀
        int seckillId = 0;
        if (ObjectUtil.isNotNull(jsonObject.get("secKillId"))) {
            seckillId = jsonObject.getInteger("secKillId");
        }

        //秒杀
        int bargainId = 0;
        if (ObjectUtil.isNotNull(jsonObject.get("bargainId"))) {
            bargainId = jsonObject.getInteger("bargainId");
        }

        /*map.put("cartId",storeCartService.addCart(uid,productId,cartNum,uniqueId
                ,"product",isNew,combinationId,seckillId,bargainId));*/
        map.put("cartId", storeCartService.addCartShareId(uid, productId, cartNum, uniqueId
                , "product", isNew, combinationId, seckillId, bargainId, spreadId));
        return ApiResult.ok(map);
    }


    /**
     * 购物车列表
     */
    @GetMapping("/cart/list")
    @ApiOperation(value = "购物车列表", notes = "购物车列表")
    public ApiResult<Map<String, Object>> getList() {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(storeCartService.getUserProductCartList(uid, "", 0));
    }

    /**
     * 修改产品数量
     */
    @PostMapping("/cart/num")
    @ApiOperation(value = "修改产品数量", notes = "修改产品数量")
    public ApiResult<Object> cartNum(@RequestBody String jsonStr) {
        int uid = SecurityUtils.getUserId().intValue();
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (ObjectUtil.isNull(jsonObject.get("id")) || ObjectUtil.isNull(jsonObject.get("number"))) {
            ApiResult.fail("参数错误");
        }
        storeCartService.changeUserCartNum(jsonObject.getInteger("id"),
                jsonObject.getInteger("number"), uid);
        return ApiResult.ok("ok");
    }

    /**
     * 购物车删除产品
     */
    @PostMapping("/cart/del")
    @ApiOperation(value = "购物车删除产品", notes = "购物车删除产品")
    public ApiResult<Object> cartDel(@Validated @RequestBody CartIdsParm parm) {
        int uid = SecurityUtils.getUserId().intValue();
        storeCartService.removeUserCart(uid, parm.getIds());

        return ApiResult.ok("ok");
    }


    /**
     * 购物车列表
     */
    @GetMapping("/cart/listNew")
    @ApiOperation(value = "购物车列表（带店铺）", notes = "购物车列表（带店铺）")
    public ApiResult<YxStoreStoreCartVo> getCartList() {
        int uid = SecurityUtils.getUserId().intValue();
//        int uid = 27;
        return ApiResult.ok(storeCartService.getUserStoreCartList(uid, "", 0));
    }


}

