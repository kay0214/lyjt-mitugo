/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.web.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserExtractService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName UserBillController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/10
 **/
@Slf4j
@RestController
@Api(value = "用户分销", tags = "用户:用户分销", description = "用户分销")
public class UserBillController extends BaseController {

    @Autowired
    YxUserBillService userBillService;
    @Autowired
    YxUserExtractService extractService;
    @Autowired
    YxSystemConfigService systemConfigService;
    @Autowired
    YxUserService yxUserService;




    /**
     * 推广数据    昨天的佣金   累计提现金额  当前佣金
     */
    @GetMapping("/commission")
    @ApiOperation(value = "推广数据", notes = "推广数据")
    public ApiResult<Object> commission() {
        int uid = SecurityUtils.getUserId().intValue();

        //判断分销类型
        String statu = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_STATU);
        YxUserQueryVo userQueryVo = yxUserService.getYxUserById(uid);
        if (StrUtil.isNotEmpty(statu)) {
            if (Integer.valueOf(statu) == 1) {
                if (userQueryVo.getIsPromoter() == 0) {
                    return ApiResult.fail("你不是推广员哦!");
                }
            }
        }

        //昨天的佣金
        BigDecimal lastDayCount = userBillService.yesterdayCommissionSum(uid);
        BigDecimal todayCommission = userBillService.todayCommissionSum(uid);
        //累计提现金额
        BigDecimal extractCount = extractService.extractSum(uid);
        // 累计佣金
        YxUser yxUser = this.yxUserService.getById(uid);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("lastDayCount", lastDayCount);
        map.put("todayCommission", todayCommission);
        map.put("extractCount", extractCount);
        map.put("commissionCount", yxUser.getBrokeragePrice());

        return ApiResult.ok(map);
    }

    /**
     * 积分记录
     */
    @GetMapping("/integral/list")
    @ApiOperation(value = "积分记录", notes = "积分记录")
    public ApiResult<Object> userInfo(YxUserBillQueryParam queryParam) {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userBillService.userBillList(uid, queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(), "integral"));
    }

    /**
     * 分销二维码海报生成
     */
    @GetMapping("/spread/banner")
    @ApiOperation(value = "分销二维码海报生成", notes = "分销二维码海报生成")
    public ApiResult<Object> spreadBanner() throws IOException {
        ApiResult result =userBillService.spreadBanner();
        return result;
    }

    /**
     * 推荐用户
     * grade == 0  获取一级推荐人
     * grade == 其他  获取二级推荐人
     * keyword 会员名称查询
     * sort  childCount ASC/DESC  团队排序   numberCount ASC/DESC
     * 金额排序  orderCount  ASC/DESC  订单排序
     */
    @PostMapping("/spread/people")
    @ApiOperation(value = "推荐用户", notes = "推荐用户")
    public ApiResult<Object> spreadPeople(@Valid @RequestBody PromParam param) {
        int uid = SecurityUtils.getUserId().intValue();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("list", yxUserService.getUserSpreadGrade(param, uid));
        map.put("total", yxUserService.getSpreadCount(uid, 1));
        map.put("totalLevel", yxUserService.getSpreadCount(uid, 2));

        return ApiResult.ok(map);
    }

    /**
     * 推广佣金明细
     * type  0 全部  1 消费  2 充值  3 返佣  4 提现
     *
     * @return mixed
     */
    @GetMapping("/spread/commission/{type}")
    @ApiOperation(value = "推广佣金明细", notes = "推广佣金明细")
    public ApiResult<Object> spreadCommission(YxUserBillQueryParam queryParam,
                                              @PathVariable String type) {
        int newType = 0;
        if (NumberUtil.isNumber(type)) {
            newType = Integer.valueOf(type);
        }
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userBillService.getUserBillList(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(), uid, newType));
    }


    /**
     * 推广订单
     */
    @PostMapping("/spread/order")
    @ApiOperation(value = "推广订单", notes = "推广订单")
    public ApiResult<Object> spreadOrder(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (ObjectUtil.isNull(jsonObject.get("page")) || ObjectUtil.isNull(jsonObject.get("limit"))) {
            return ApiResult.fail("参数错误");
        }
        int uid = SecurityUtils.getUserId().intValue();
        Map<String, Object> map = userBillService.spreadOrder(uid, Integer.valueOf(jsonObject.get("page").toString())
                , Integer.valueOf(jsonObject.get("limit").toString()));
        return ApiResult.ok(map);
    }


}
