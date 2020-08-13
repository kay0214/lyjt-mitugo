package co.yixiang.modules.commission.web.controller;

import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.commission.web.param.YxCommissionRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCommissionRateQueryVo;
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
 * 分佣配置表 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCommissionRate")
@Api("分佣配置表 API")
public class YxCommissionRateController extends BaseController {

    @Autowired
    private YxCommissionRateService yxCommissionRateService;

    /**
    * 添加分佣配置表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCommissionRate对象",notes = "添加分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCommissionRate(@Valid @RequestBody YxCommissionRate yxCommissionRate) throws Exception{
        boolean flag = yxCommissionRateService.save(yxCommissionRate);
        return ApiResult.result(flag);
    }

    /**
    * 修改分佣配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCommissionRate对象",notes = "修改分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCommissionRate(@Valid @RequestBody YxCommissionRate yxCommissionRate) throws Exception{
        boolean flag = yxCommissionRateService.updateById(yxCommissionRate);
        return ApiResult.result(flag);
    }

    /**
    * 删除分佣配置表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCommissionRate对象",notes = "删除分佣配置表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCommissionRate(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCommissionRateService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取分佣配置表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCommissionRate对象详情",notes = "查看分佣配置表",response = YxCommissionRateQueryVo.class)
    public ApiResult<YxCommissionRateQueryVo> getYxCommissionRate(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCommissionRateQueryVo yxCommissionRateQueryVo = yxCommissionRateService.getYxCommissionRateById(idParam.getId());
        return ApiResult.ok(yxCommissionRateQueryVo);
    }

    /**
     * 分佣配置表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCommissionRate分页列表",notes = "分佣配置表分页列表",response = YxCommissionRateQueryVo.class)
    public ApiResult<Paging<YxCommissionRateQueryVo>> getYxCommissionRatePageList(@Valid @RequestBody(required = false) YxCommissionRateQueryParam yxCommissionRateQueryParam) throws Exception{
        Paging<YxCommissionRateQueryVo> paging = yxCommissionRateService.getYxCommissionRatePageList(yxCommissionRateQueryParam);
        return ApiResult.ok(paging);
    }

}

