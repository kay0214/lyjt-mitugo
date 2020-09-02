/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.service.YxUserExtractService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.param.UserExtParam;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.SecurityUtils;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 用户提现 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户提现", tags = "用户:用户提现", description = "用户提现")
public class UserExtractController extends BaseController {

    @Autowired
    private YxUserExtractService userExtractService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxUserExtractService yxUserExtractService;

    /**
     * 提现参数
     */
    @GetMapping("/extract/bank")
    @ApiOperation(value = "提现参数", notes = "提现参数")
    public ApiResult<Object> bank() {
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userInfo = userService.getYxUserById(uid);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("commissionCount", userInfo.getBrokeragePrice());
        map.put("minPrice", systemConfigService.getData(SystemConfigConstants.USER_EXTRACT_MIN_PRICE));
        return ApiResult.ok(map);
    }


    /**
     * 用户提现
     */
    @PostMapping("/extract/cash")
    @ApiOperation(value = "用户提现", notes = "用户提现")
    public ApiResult<String> addYxUserExtract(@Valid @RequestBody UserExtParam param) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        userExtractService.userExtract(uid, param);

        return ApiResult.ok("申请提现成功");
    }


    /**
     * 用户提现表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxUserExtract分页列表", notes = "用户提现表分页列表", response = YxUserExtractQueryVo.class)
    public ApiResult<Paging<YxUserExtractQueryVo>> getYxUserExtractPageList(@Valid @RequestBody(required = false) YxUserExtractQueryParam yxUserExtractQueryParam) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        yxUserExtractQueryParam.setUid(uid);
        // 查询前台用户
        yxUserExtractQueryParam.setUserType(0);
        // 获取提现分页数据
        Paging<YxUserExtractQueryVo> paging = userExtractService.getYxUserExtractPageList(yxUserExtractQueryParam);
        // 获取总累计提现金额
        BigDecimal extractCount = yxUserExtractService.extractSum(uid);
        paging.setSum(extractCount.toString());
        return ApiResult.ok(paging);
    }

}

