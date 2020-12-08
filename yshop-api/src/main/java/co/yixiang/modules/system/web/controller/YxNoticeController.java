package co.yixiang.modules.system.web.controller;

import co.yixiang.modules.system.entity.YxNotice;
import co.yixiang.modules.system.service.YxNoticeService;
import co.yixiang.modules.system.web.param.YxNoticeQueryParam;
import co.yixiang.modules.system.web.vo.YxNoticeQueryVo;
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
 * 公告表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxNotice")
@Api("公告表 API")
public class YxNoticeController extends BaseController {

    @Autowired
    private YxNoticeService yxNoticeService;

    /**
    * 添加公告表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxNotice对象",notes = "添加公告表",response = ApiResult.class)
    public ApiResult<Boolean> addYxNotice(@Valid @RequestBody YxNotice yxNotice) throws Exception{
        boolean flag = yxNoticeService.save(yxNotice);
        return ApiResult.result(flag);
    }

    /**
    * 修改公告表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxNotice对象",notes = "修改公告表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxNotice(@Valid @RequestBody YxNotice yxNotice) throws Exception{
        boolean flag = yxNoticeService.updateById(yxNotice);
        return ApiResult.result(flag);
    }

    /**
    * 删除公告表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxNotice对象",notes = "删除公告表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxNotice(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxNoticeService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取公告表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxNotice对象详情",notes = "查看公告表",response = YxNoticeQueryVo.class)
    public ApiResult<YxNoticeQueryVo> getYxNotice(@Valid @RequestBody IdParam idParam) throws Exception{
        YxNoticeQueryVo yxNoticeQueryVo = yxNoticeService.getYxNoticeById(idParam.getId());
        return ApiResult.ok(yxNoticeQueryVo);
    }

    /**
     * 公告表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxNotice分页列表",notes = "公告表分页列表",response = YxNoticeQueryVo.class)
    public ApiResult<Paging<YxNoticeQueryVo>> getYxNoticePageList(@Valid @RequestBody(required = false) YxNoticeQueryParam yxNoticeQueryParam) throws Exception{
        Paging<YxNoticeQueryVo> paging = yxNoticeService.getYxNoticePageList(yxNoticeQueryParam);
        return ApiResult.ok(paging);
    }

}

