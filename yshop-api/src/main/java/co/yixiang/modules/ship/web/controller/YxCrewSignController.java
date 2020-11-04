package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxCrewSign;
import co.yixiang.modules.ship.service.YxCrewSignService;
import co.yixiang.modules.ship.web.param.YxCrewSignQueryParam;
import co.yixiang.modules.ship.web.vo.YxCrewSignQueryVo;
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
 * 船员签到表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCrewSign")
@Api("船员签到表 API")
public class YxCrewSignController extends BaseController {

    @Autowired
    private YxCrewSignService yxCrewSignService;

    /**
    * 添加船员签到表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCrewSign对象",notes = "添加船员签到表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCrewSign(@Valid @RequestBody YxCrewSign yxCrewSign) throws Exception{
        boolean flag = yxCrewSignService.save(yxCrewSign);
        return ApiResult.result(flag);
    }

    /**
    * 修改船员签到表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCrewSign对象",notes = "修改船员签到表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCrewSign(@Valid @RequestBody YxCrewSign yxCrewSign) throws Exception{
        boolean flag = yxCrewSignService.updateById(yxCrewSign);
        return ApiResult.result(flag);
    }

    /**
    * 删除船员签到表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCrewSign对象",notes = "删除船员签到表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCrewSign(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCrewSignService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船员签到表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCrewSign对象详情",notes = "查看船员签到表",response = YxCrewSignQueryVo.class)
    public ApiResult<YxCrewSignQueryVo> getYxCrewSign(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCrewSignQueryVo yxCrewSignQueryVo = yxCrewSignService.getYxCrewSignById(idParam.getId());
        return ApiResult.ok(yxCrewSignQueryVo);
    }

    /**
     * 船员签到表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCrewSign分页列表",notes = "船员签到表分页列表",response = YxCrewSignQueryVo.class)
    public ApiResult<Paging<YxCrewSignQueryVo>> getYxCrewSignPageList(@Valid @RequestBody(required = false) YxCrewSignQueryParam yxCrewSignQueryParam) throws Exception{
        Paging<YxCrewSignQueryVo> paging = yxCrewSignService.getYxCrewSignPageList(yxCrewSignQueryParam);
        return ApiResult.ok(paging);
    }

}

