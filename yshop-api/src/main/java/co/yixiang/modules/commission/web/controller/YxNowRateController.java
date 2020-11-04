package co.yixiang.modules.commission.web.controller;

import co.yixiang.modules.commission.entity.YxNowRate;
import co.yixiang.modules.commission.service.YxNowRateService;
import co.yixiang.modules.commission.web.param.YxNowRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxNowRateQueryVo;
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
 * 购买时费率记录表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxNowRate")
@Api("购买时费率记录表 API")
public class YxNowRateController extends BaseController {

    @Autowired
    private YxNowRateService yxNowRateService;

    /**
    * 添加购买时费率记录表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxNowRate对象",notes = "添加购买时费率记录表",response = ApiResult.class)
    public ApiResult<Boolean> addYxNowRate(@Valid @RequestBody YxNowRate yxNowRate) throws Exception{
        boolean flag = yxNowRateService.save(yxNowRate);
        return ApiResult.result(flag);
    }

    /**
    * 修改购买时费率记录表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxNowRate对象",notes = "修改购买时费率记录表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxNowRate(@Valid @RequestBody YxNowRate yxNowRate) throws Exception{
        boolean flag = yxNowRateService.updateById(yxNowRate);
        return ApiResult.result(flag);
    }

    /**
    * 删除购买时费率记录表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxNowRate对象",notes = "删除购买时费率记录表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxNowRate(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxNowRateService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取购买时费率记录表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxNowRate对象详情",notes = "查看购买时费率记录表",response = YxNowRateQueryVo.class)
    public ApiResult<YxNowRateQueryVo> getYxNowRate(@Valid @RequestBody IdParam idParam) throws Exception{
        YxNowRateQueryVo yxNowRateQueryVo = yxNowRateService.getYxNowRateById(idParam.getId());
        return ApiResult.ok(yxNowRateQueryVo);
    }

    /**
     * 购买时费率记录表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxNowRate分页列表",notes = "购买时费率记录表分页列表",response = YxNowRateQueryVo.class)
    public ApiResult<Paging<YxNowRateQueryVo>> getYxNowRatePageList(@Valid @RequestBody(required = false) YxNowRateQueryParam yxNowRateQueryParam) throws Exception{
        Paging<YxNowRateQueryVo> paging = yxNowRateService.getYxNowRatePageList(yxNowRateQueryParam);
        return ApiResult.ok(paging);
    }

}

