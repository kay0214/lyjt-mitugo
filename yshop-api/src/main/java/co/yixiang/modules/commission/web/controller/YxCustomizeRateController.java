package co.yixiang.modules.commission.web.controller;

import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.service.YxCustomizeRateService;
import co.yixiang.modules.commission.web.param.YxCustomizeRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCustomizeRateQueryVo;
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
 * 自定义分佣配置表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCustomizeRate")
@Api("自定义分佣配置表 API")
public class YxCustomizeRateController extends BaseController {

    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;

    /**
    * 添加自定义分佣配置表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCustomizeRate对象",notes = "添加自定义分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCustomizeRate(@Valid @RequestBody YxCustomizeRate yxCustomizeRate) throws Exception{
        boolean flag = yxCustomizeRateService.save(yxCustomizeRate);
        return ApiResult.result(flag);
    }

    /**
    * 修改自定义分佣配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCustomizeRate对象",notes = "修改自定义分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCustomizeRate(@Valid @RequestBody YxCustomizeRate yxCustomizeRate) throws Exception{
        boolean flag = yxCustomizeRateService.updateById(yxCustomizeRate);
        return ApiResult.result(flag);
    }

    /**
    * 删除自定义分佣配置表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCustomizeRate对象",notes = "删除自定义分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCustomizeRate(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCustomizeRateService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取自定义分佣配置表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCustomizeRate对象详情",notes = "查看自定义分佣配置表",response = YxCustomizeRateQueryVo.class)
    public ApiResult<YxCustomizeRateQueryVo> getYxCustomizeRate(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCustomizeRateQueryVo yxCustomizeRateQueryVo = yxCustomizeRateService.getYxCustomizeRateById(idParam.getId());
        return ApiResult.ok(yxCustomizeRateQueryVo);
    }

    /**
     * 自定义分佣配置表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCustomizeRate分页列表",notes = "自定义分佣配置表分页列表",response = YxCustomizeRateQueryVo.class)
    public ApiResult<Paging<YxCustomizeRateQueryVo>> getYxCustomizeRatePageList(@Valid @RequestBody(required = false) YxCustomizeRateQueryParam yxCustomizeRateQueryParam) throws Exception{
        Paging<YxCustomizeRateQueryVo> paging = yxCustomizeRateService.getYxCustomizeRatePageList(yxCustomizeRateQueryParam);
        return ApiResult.ok(paging);
    }

}

