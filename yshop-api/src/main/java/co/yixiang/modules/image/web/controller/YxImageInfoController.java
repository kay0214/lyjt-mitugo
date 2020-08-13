package co.yixiang.modules.image.web.controller;

import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.image.web.param.YxImageInfoQueryParam;
import co.yixiang.modules.image.web.vo.YxImageInfoQueryVo;
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
 * 图片表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxImageInfo")
@Api("图片表 API")
public class YxImageInfoController extends BaseController {

    @Autowired
    private YxImageInfoService yxImageInfoService;

    /**
    * 添加图片表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxImageInfo对象",notes = "添加图片表",response = ApiResult.class)
    public ApiResult<Boolean> addYxImageInfo(@Valid @RequestBody YxImageInfo yxImageInfo) throws Exception{
        boolean flag = yxImageInfoService.save(yxImageInfo);
        return ApiResult.result(flag);
    }

    /**
    * 修改图片表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxImageInfo对象",notes = "修改图片表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxImageInfo(@Valid @RequestBody YxImageInfo yxImageInfo) throws Exception{
        boolean flag = yxImageInfoService.updateById(yxImageInfo);
        return ApiResult.result(flag);
    }

    /**
    * 删除图片表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxImageInfo对象",notes = "删除图片表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxImageInfo(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxImageInfoService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取图片表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxImageInfo对象详情",notes = "查看图片表",response = YxImageInfoQueryVo.class)
    public ApiResult<YxImageInfoQueryVo> getYxImageInfo(@Valid @RequestBody IdParam idParam) throws Exception{
        YxImageInfoQueryVo yxImageInfoQueryVo = yxImageInfoService.getYxImageInfoById(idParam.getId());
        return ApiResult.ok(yxImageInfoQueryVo);
    }

    /**
     * 图片表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxImageInfo分页列表",notes = "图片表分页列表",response = YxImageInfoQueryVo.class)
    public ApiResult<Paging<YxImageInfoQueryVo>> getYxImageInfoPageList(@Valid @RequestBody(required = false) YxImageInfoQueryParam yxImageInfoQueryParam) throws Exception{
        Paging<YxImageInfoQueryVo> paging = yxImageInfoService.getYxImageInfoPageList(yxImageInfoQueryParam);
        return ApiResult.ok(paging);
    }

}

