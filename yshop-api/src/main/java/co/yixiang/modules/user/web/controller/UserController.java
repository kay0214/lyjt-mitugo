/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.shop.service.YxStoreProductRelationService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxSystemUserLevelService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxUserSignService;
import co.yixiang.modules.user.web.param.UserEditParam;
import co.yixiang.modules.user.web.param.YxUserSignQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户中心", tags = "用户:用户中心", description = "用户中心")
public class UserController extends BaseController {

    private final YxUserService yxUserService;
    private final YxSystemGroupDataService systemGroupDataService;
    private final YxStoreOrderService orderService;
    private final YxStoreProductRelationService relationService;
    private final YxUserSignService userSignService;
    private final YxUserBillService userBillService;
    private final YxSystemUserLevelService systemUserLevelService;

    private static Lock lock = new ReentrantLock(false);

    /**
     * 用户资料
     */
    @GetMapping("/userinfo")
    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",response = YxUserQueryVo.class)
    public ApiResult<Object> userInfo(){
        int uid = SecurityUtils.getUserId().intValue();

        //update count
        yxUserService.setUserSpreadCount(uid);
        return ApiResult.ok(yxUserService.getNewYxUserById(uid));
    }

    /**
     * 获取个人中心菜单
     */
    @Log(value = "进入用户中心",type = 1)
    @GetMapping("/menu/user")
    @ApiOperation(value = "获取个人中心菜单",notes = "获取个人中心菜单")
    public ApiResult<Map<String,Object>> userMenu(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("routine_my_menus",systemGroupDataService.getDatas(ShopConstants.YSHOP_MY_MENUES));
        return ApiResult.ok(map);
    }

    /**
     * 个人中心
     */
    @GetMapping("/user")
    @ApiOperation(value = "个人中心",notes = "个人中心")
    public ApiResult<Object> user(){
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo yxUserQueryVo = yxUserService.getNewYxUserById(uid);


        if(yxUserQueryVo.getLevel() > 0) {
            yxUserQueryVo.setVip(true);
            YxSystemUserLevelQueryVo systemUserLevelQueryVo = systemUserLevelService
                    .getYxSystemUserLevelById(yxUserQueryVo.getLevel());
            yxUserQueryVo.setVipIcon(systemUserLevelQueryVo.getIcon());
            yxUserQueryVo.setVipId(yxUserQueryVo.getLevel());
            yxUserQueryVo.setVipName(systemUserLevelQueryVo.getName());
        }
        return ApiResult.ok(yxUserQueryVo);
    }

    /**
     * 订单统计数据
     */
    @GetMapping("/order/data")
    @ApiOperation(value = "订单统计数据",notes = "订单统计数据")
    public ApiResult<Object> orderData(){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(orderService.orderData(uid));
    }

    /**
     * 获取收藏产品
     */
    @GetMapping("/collect/user")
    @ApiOperation(value = "获取收藏产品",notes = "获取收藏产品")
    public ApiResult<Object> collectUser(@RequestParam(value = "page",defaultValue = "1") int page,
                                         @RequestParam(value = "limit",defaultValue = "10") int limit){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(relationService.userCollectProduct(page,limit,uid));
    }

    /**
     * 用户资金统计
     */
    @GetMapping("/user/balance")
    @ApiOperation(value = "用户资金统计",notes = "用户资金统计")
    public ApiResult<Object> collectUser(){
        int uid = SecurityUtils.getUserId().intValue();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("now_money",yxUserService.getYxUserById(uid).getNowMoney());
        map.put("orderStatusSum",orderService.orderData(uid).getSumPrice());
        map.put("recharge",0);
        return ApiResult.ok(map);
    }

    /**
     * 获取活动状态
     */
    @AnonymousAccess
    @GetMapping("/user/activity")
    @ApiOperation(value = "获取活动状态",notes = "获取活动状态")
    @Deprecated
    public ApiResult<Object> activity(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("is_bargin",false);
        map.put("is_pink",false);
        map.put("is_seckill",false);
        return ApiResult.ok(map);
    }

    /**
     * 签到用户信息
     */
    @PostMapping("/sign/user")
    @ApiOperation(value = "签到用户信息",notes = "签到用户信息")
    public ApiResult<Object> sign(@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = yxUserService.getYxUserById(uid);
        int sumSignDay = userSignService.getSignSumDay(uid);
        boolean isDaySign = userSignService.getToDayIsSign(uid);
        boolean isYesterDaySign = userSignService.getYesterDayIsSign(uid);
        userQueryVo.setSumSignDay(sumSignDay);
        userQueryVo.setIsDaySign(isDaySign);
        userQueryVo.setIsYesterDaySign(isYesterDaySign);
        if(!isDaySign && !isYesterDaySign) userQueryVo.setSignNum(0);
        return ApiResult.ok(userQueryVo);
    }

    /**
     * 签到配置
     */
    @GetMapping("/sign/config")
    @ApiOperation(value = "签到配置",notes = "签到配置")
    public ApiResult<Object> signConfig(){
        return ApiResult.ok(systemGroupDataService.getDatas(ShopConstants.YSHOP_SIGN_DAY_NUM));
    }

    /**
     * 签到列表
     */
    @GetMapping("/sign/list")
    @ApiOperation(value = "签到列表",notes = "签到列表")
    public ApiResult<Object> signList(YxUserSignQueryParam queryParam){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userSignService.getSignList(uid,queryParam.getPage().intValue(),
                queryParam.getLimit().intValue()));
    }

    /**
     * 签到列表（年月）
     */
    @GetMapping("/sign/month")
    @ApiOperation(value = "签到列表（年月）",notes = "签到列表（年月）")
    public ApiResult<Object> signMonthList(YxUserSignQueryParam queryParam){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userBillService.getUserBillList(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(),uid,5));
    }

    /**
     * 开始签到
     */
    @PostMapping("/sign/integral")
    @ApiOperation(value = "开始签到",notes = "开始签到")
    public ApiResult<Object> signIntegral(){
        int uid = SecurityUtils.getUserId().intValue();
        boolean isDaySign = userSignService.getToDayIsSign(uid);
        if(isDaySign) return ApiResult.fail("已签到");
        int integral = 0;
        try {
            lock.lock();
            integral = userSignService.sign(uid);
        }finally {
            lock.unlock();
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("integral",integral);
        return ApiResult.ok(map,"签到获得" + integral + "积分");
    }


    @PostMapping("/user/edit")
    @ApiOperation(value = "用户修改信息",notes = "用修改信息")
    public ApiResult<Object> edit(@Validated @RequestBody UserEditParam param){
        int uid = SecurityUtils.getUserId().intValue();

        YxUser yxUser = new YxUser();
        yxUser.setAvatar(param.getAvatar());
        yxUser.setNickname(param.getNickname());
        yxUser.setUid(uid);

        yxUserService.updateById(yxUser);

        return ApiResult.ok("修改成功");
    }




}

