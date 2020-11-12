package co.yixiang.modules.manage.web.controller;

import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.manage.service.UsersRolesService;
import co.yixiang.modules.manage.web.param.UsersRolesQueryParam;
import co.yixiang.modules.manage.web.vo.UsersRolesQueryVo;
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
 * 用户角色关联 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-12
 */
@Slf4j
@RestController
@RequestMapping("/usersRoles")
@Api("用户角色关联 API")
public class UsersRolesController extends BaseController {

    @Autowired
    private UsersRolesService usersRolesService;

    /**
    * 添加用户角色关联
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加UsersRoles对象",notes = "添加用户角色关联",response = ApiResult.class)
    public ApiResult<Boolean> addUsersRoles(@Valid @RequestBody UsersRoles usersRoles) throws Exception{
        boolean flag = usersRolesService.save(usersRoles);
        return ApiResult.result(flag);
    }

    /**
    * 修改用户角色关联
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改UsersRoles对象",notes = "修改用户角色关联",response = ApiResult.class)
    public ApiResult<Boolean> updateUsersRoles(@Valid @RequestBody UsersRoles usersRoles) throws Exception{
        boolean flag = usersRolesService.updateById(usersRoles);
        return ApiResult.result(flag);
    }

    /**
    * 删除用户角色关联
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除UsersRoles对象",notes = "删除用户角色关联",response = ApiResult.class)
    public ApiResult<Boolean> deleteUsersRoles(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = usersRolesService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取用户角色关联
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取UsersRoles对象详情",notes = "查看用户角色关联",response = UsersRolesQueryVo.class)
    public ApiResult<UsersRolesQueryVo> getUsersRoles(@Valid @RequestBody IdParam idParam) throws Exception{
        UsersRolesQueryVo usersRolesQueryVo = usersRolesService.getUsersRolesById(idParam.getId());
        return ApiResult.ok(usersRolesQueryVo);
    }

    /**
     * 用户角色关联分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取UsersRoles分页列表",notes = "用户角色关联分页列表",response = UsersRolesQueryVo.class)
    public ApiResult<Paging<UsersRolesQueryVo>> getUsersRolesPageList(@Valid @RequestBody(required = false) UsersRolesQueryParam usersRolesQueryParam) throws Exception{
        Paging<UsersRolesQueryVo> paging = usersRolesService.getUsersRolesPageList(usersRolesQueryParam);
        return ApiResult.ok(paging);
    }

}

