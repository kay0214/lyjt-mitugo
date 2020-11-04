package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipAppointDetail;
import co.yixiang.modules.ship.service.YxShipAppointDetailService;
import co.yixiang.modules.ship.web.param.YxShipAppointDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointDetailQueryVo;
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
 * 船只预约表详情 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipAppointDetail")
@Api("船只预约表详情 API")
public class YxShipAppointDetailController extends BaseController {

    @Autowired
    private YxShipAppointDetailService yxShipAppointDetailService;

    /**
    * 添加船只预约表详情
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipAppointDetail对象",notes = "添加船只预约表详情",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipAppointDetail(@Valid @RequestBody YxShipAppointDetail yxShipAppointDetail) throws Exception{
        boolean flag = yxShipAppointDetailService.save(yxShipAppointDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只预约表详情
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipAppointDetail对象",notes = "修改船只预约表详情",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipAppointDetail(@Valid @RequestBody YxShipAppointDetail yxShipAppointDetail) throws Exception{
        boolean flag = yxShipAppointDetailService.updateById(yxShipAppointDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只预约表详情
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipAppointDetail对象",notes = "删除船只预约表详情",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipAppointDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipAppointDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只预约表详情
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipAppointDetail对象详情",notes = "查看船只预约表详情",response = YxShipAppointDetailQueryVo.class)
    public ApiResult<YxShipAppointDetailQueryVo> getYxShipAppointDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipAppointDetailQueryVo yxShipAppointDetailQueryVo = yxShipAppointDetailService.getYxShipAppointDetailById(idParam.getId());
        return ApiResult.ok(yxShipAppointDetailQueryVo);
    }

    /**
     * 船只预约表详情分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipAppointDetail分页列表",notes = "船只预约表详情分页列表",response = YxShipAppointDetailQueryVo.class)
    public ApiResult<Paging<YxShipAppointDetailQueryVo>> getYxShipAppointDetailPageList(@Valid @RequestBody(required = false) YxShipAppointDetailQueryParam yxShipAppointDetailQueryParam) throws Exception{
        Paging<YxShipAppointDetailQueryVo> paging = yxShipAppointDetailService.getYxShipAppointDetailPageList(yxShipAppointDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

