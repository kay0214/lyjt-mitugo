package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.param.YxShipSeriesQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
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
 * 船只系列表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipSeries")
@Api("船只系列表 API")
public class YxShipSeriesController extends BaseController {

    @Autowired
    private YxShipSeriesService yxShipSeriesService;

    /**
    * 添加船只系列表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipSeries对象",notes = "添加船只系列表",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipSeries(@Valid @RequestBody YxShipSeries yxShipSeries) throws Exception{
        boolean flag = yxShipSeriesService.save(yxShipSeries);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只系列表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipSeries对象",notes = "修改船只系列表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipSeries(@Valid @RequestBody YxShipSeries yxShipSeries) throws Exception{
        boolean flag = yxShipSeriesService.updateById(yxShipSeries);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只系列表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipSeries对象",notes = "删除船只系列表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipSeries(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipSeriesService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只系列表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipSeries对象详情",notes = "查看船只系列表",response = YxShipSeriesQueryVo.class)
    public ApiResult<YxShipSeriesQueryVo> getYxShipSeries(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipSeriesQueryVo yxShipSeriesQueryVo = yxShipSeriesService.getYxShipSeriesById(idParam.getId());
        return ApiResult.ok(yxShipSeriesQueryVo);
    }

    /**
     * 船只系列表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipSeries分页列表",notes = "船只系列表分页列表",response = YxShipSeriesQueryVo.class)
    public ApiResult<Paging<YxShipSeriesQueryVo>> getYxShipSeriesPageList(@Valid @RequestBody(required = false) YxShipSeriesQueryParam yxShipSeriesQueryParam) throws Exception{
        Paging<YxShipSeriesQueryVo> paging = yxShipSeriesService.getYxShipSeriesPageList(yxShipSeriesQueryParam);
        return ApiResult.ok(paging);
    }

}

