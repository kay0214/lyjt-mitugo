/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.couponUse.dto.UserInfoVo;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.user.service.YxUserService;
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
@RequestMapping("/userInfo")
@Api(value = "核销端：用户信息", tags = "核销端：用户信息")
public class UserInfoController extends BaseController {

    @Autowired
    private YxUserService yxUserService;


    private final IGenerator generator;

    public UserInfoController(IGenerator generator) {
        this.generator = generator;
    }


    @NeedLogin
    @AnonymousAccess
    @ApiOperation("核销端：用户信息")
    @GetMapping(value = "/getUserInfo")
    public ResponseEntity<Object> getUserInfo(@RequestHeader(value = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setId(user.getId().intValue());
        userInfoVo.setPhone(user.getPhone());
        userInfoVo.setUserName(user.getNickName());
        String strRole = "";
        UsersRoles usersRoles = yxUserService.getUserRolesByUserId(user.getId().intValue());
        if (null == usersRoles) {
            throw new BadRequestException("此用户未配置角色，请先到平台分配角色");
        }
        int roleId = usersRoles.getRoleId().intValue();
        switch (roleId) {
            case 7:
                strRole = "核销人员";
                break;
            case 8:
                strRole = "船只核销人员";
                break;
            case 9:
                strRole = "船长";
                break;
            case 10:
                strRole = "景区推广";
                break;
        }
        userInfoVo.setUserRole(strRole);
        map.put("status", "1");
        map.put("statusDesc", "操作成功");
        map.put("data",userInfoVo);
        return ResponseEntity.ok(map);
    }
}
