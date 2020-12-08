package co.yixiang.modules.manage.web.controller;

import co.yixiang.modules.manage.entity.UserAvatar;
import co.yixiang.modules.manage.service.UserAvatarService;
import co.yixiang.modules.manage.web.param.UserAvatarQueryParam;
import co.yixiang.modules.manage.web.vo.UserAvatarQueryVo;
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
 * 系统用户头像 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-18
 */
@Slf4j
@RestController
@RequestMapping("/userAvatar")
@Api("系统用户头像 API")
public class UserAvatarController extends BaseController {

    @Autowired
    private UserAvatarService userAvatarService;

    /**
    * 添加系统用户头像
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加UserAvatar对象",notes = "添加系统用户头像",response = ApiResult.class)
    public ApiResult<Boolean> addUserAvatar(@Valid @RequestBody UserAvatar userAvatar) throws Exception{
        boolean flag = userAvatarService.save(userAvatar);
        return ApiResult.result(flag);
    }

    /**
    * 修改系统用户头像
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改UserAvatar对象",notes = "修改系统用户头像",response = ApiResult.class)
    public ApiResult<Boolean> updateUserAvatar(@Valid @RequestBody UserAvatar userAvatar) throws Exception{
        boolean flag = userAvatarService.updateById(userAvatar);
        return ApiResult.result(flag);
    }

    /**
    * 删除系统用户头像
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除UserAvatar对象",notes = "删除系统用户头像",response = ApiResult.class)
    public ApiResult<Boolean> deleteUserAvatar(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = userAvatarService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取系统用户头像
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取UserAvatar对象详情",notes = "查看系统用户头像",response = UserAvatarQueryVo.class)
    public ApiResult<UserAvatarQueryVo> getUserAvatar(@Valid @RequestBody IdParam idParam) throws Exception{
        UserAvatarQueryVo userAvatarQueryVo = userAvatarService.getUserAvatarById(idParam.getId());
        return ApiResult.ok(userAvatarQueryVo);
    }

    /**
     * 系统用户头像分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取UserAvatar分页列表",notes = "系统用户头像分页列表",response = UserAvatarQueryVo.class)
    public ApiResult<Paging<UserAvatarQueryVo>> getUserAvatarPageList(@Valid @RequestBody(required = false) UserAvatarQueryParam userAvatarQueryParam) throws Exception{
        Paging<UserAvatarQueryVo> paging = userAvatarService.getUserAvatarPageList(userAvatarQueryParam);
        return ApiResult.ok(paging);
    }

}

