/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.couponUse.dto.UserBillVo;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易流水
 */
@Slf4j
@RestController
@RequestMapping("/userAccount")
@Api(value = "核销端 交易流水")
public class UserAccountController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private YxUserBillService billService;


    private final IGenerator generator;

    public UserAccountController(IGenerator generator) {
        this.generator = generator;
    }


    @NeedLogin
    @ApiOperation("B端：线下交易流水列表")
    @GetMapping(value = "/getUserAccountList")
    public ResponseEntity<Object> getUserAccountList(@RequestHeader(value = "token") String token, UserAccountQueryParam param) {
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        Paging<UserBillVo> result = billService.getYxUserAccountPageList(param, user.getId());
        return ResponseEntity.ok(result);
    }

    @NeedLogin
    @ApiOperation("B端：线上交易流水列表")
    @GetMapping(value = "/getUserProductAccountList")
    public ResponseEntity<Object> getUserProductAccountList(@RequestHeader(value = "token") String token, UserAccountQueryParam param) {
        // 获取登陆用户的id
        SystemUser user = getRedisUser(token);

        Paging<UserBillVo> result = billService.getUserProductAccountList(param, user.getId());
        return ResponseEntity.ok(result);
    }
}
