package co.yixiang.modules.manage.web.controller;

import co.yixiang.modules.manage.entity.User;
import co.yixiang.modules.manage.service.UserService;
import co.yixiang.modules.manage.web.param.UserQueryParam;
import co.yixiang.modules.manage.web.vo.UserQueryVo;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.api.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import co.yixiang.common.web.vo.Paging;
import co.yixiang.common.web.param.IdParam;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("系统用户 API")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
    * 添加系统用户
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加User对象",notes = "添加系统用户",response = ApiResult.class)
    public ApiResult<Boolean> addUser(@Valid @RequestBody User user) throws Exception{
        boolean flag = userService.save(user);
        return ApiResult.result(flag);
    }

    /**
    * 修改系统用户
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改User对象",notes = "修改系统用户",response = ApiResult.class)
    public ApiResult<Boolean> updateUser(@Valid @RequestBody User user) throws Exception{
        boolean flag = userService.updateById(user);
        return ApiResult.result(flag);
    }

    /**
    * 删除系统用户
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除User对象",notes = "删除系统用户",response = ApiResult.class)
    public ApiResult<Boolean> deleteUser(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = userService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取系统用户
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取User对象详情",notes = "查看系统用户",response = UserQueryVo.class)
    public ApiResult<UserQueryVo> getUser(@Valid @RequestBody IdParam idParam) throws Exception{
        UserQueryVo userQueryVo = userService.getUserById(idParam.getId());
        return ApiResult.ok(userQueryVo);
    }

    /**
     * 系统用户分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取User分页列表",notes = "系统用户分页列表",response = UserQueryVo.class)
    public ApiResult<Paging<UserQueryVo>> getUserPageList(@Valid @RequestBody(required = false) UserQueryParam userQueryParam) throws Exception{
        Paging<UserQueryVo> paging = userService.getUserPageList(userQueryParam);
        return ApiResult.ok(paging);
    }

}

