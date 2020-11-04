package co.yixiang.modules.system.web.controller;

import co.yixiang.modules.system.entity.YxHotConfig;
import co.yixiang.modules.system.service.YxHotConfigService;
import co.yixiang.modules.system.web.param.YxHotConfigQueryParam;
import co.yixiang.modules.system.web.vo.YxHotConfigQueryVo;
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
 * HOT配置表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxHotConfig")
@Api("HOT配置表 API")
public class YxHotConfigController extends BaseController {

    @Autowired
    private YxHotConfigService yxHotConfigService;

    /**
    * 添加HOT配置表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxHotConfig对象",notes = "添加HOT配置表",response = ApiResult.class)
    public ApiResult<Boolean> addYxHotConfig(@Valid @RequestBody YxHotConfig yxHotConfig) throws Exception{
        boolean flag = yxHotConfigService.save(yxHotConfig);
        return ApiResult.result(flag);
    }

    /**
    * 修改HOT配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxHotConfig对象",notes = "修改HOT配置表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxHotConfig(@Valid @RequestBody YxHotConfig yxHotConfig) throws Exception{
        boolean flag = yxHotConfigService.updateById(yxHotConfig);
        return ApiResult.result(flag);
    }

    /**
    * 删除HOT配置表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxHotConfig对象",notes = "删除HOT配置表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxHotConfig(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxHotConfigService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取HOT配置表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxHotConfig对象详情",notes = "查看HOT配置表",response = YxHotConfigQueryVo.class)
    public ApiResult<YxHotConfigQueryVo> getYxHotConfig(@Valid @RequestBody IdParam idParam) throws Exception{
        YxHotConfigQueryVo yxHotConfigQueryVo = yxHotConfigService.getYxHotConfigById(idParam.getId());
        return ApiResult.ok(yxHotConfigQueryVo);
    }

    /**
     * HOT配置表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxHotConfig分页列表",notes = "HOT配置表分页列表",response = YxHotConfigQueryVo.class)
    public ApiResult<Paging<YxHotConfigQueryVo>> getYxHotConfigPageList(@Valid @RequestBody(required = false) YxHotConfigQueryParam yxHotConfigQueryParam) throws Exception{
        Paging<YxHotConfigQueryVo> paging = yxHotConfigService.getYxHotConfigPageList(yxHotConfigQueryParam);
        return ApiResult.ok(paging);
    }

}

