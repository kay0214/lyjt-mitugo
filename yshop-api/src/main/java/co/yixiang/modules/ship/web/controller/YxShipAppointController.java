package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.modules.ship.service.YxShipAppointService;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;
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
 * 船只预约表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipAppoint")
@Api("船只预约表 API")
public class YxShipAppointController extends BaseController {

    @Autowired
    private YxShipAppointService yxShipAppointService;

    /**
    * 添加船只预约表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipAppoint对象",notes = "添加船只预约表",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipAppoint(@Valid @RequestBody YxShipAppoint yxShipAppoint) throws Exception{
        boolean flag = yxShipAppointService.save(yxShipAppoint);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只预约表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipAppoint对象",notes = "修改船只预约表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipAppoint(@Valid @RequestBody YxShipAppoint yxShipAppoint) throws Exception{
        boolean flag = yxShipAppointService.updateById(yxShipAppoint);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只预约表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipAppoint对象",notes = "删除船只预约表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipAppoint(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipAppointService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只预约表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipAppoint对象详情",notes = "查看船只预约表",response = YxShipAppointQueryVo.class)
    public ApiResult<YxShipAppointQueryVo> getYxShipAppoint(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipAppointQueryVo yxShipAppointQueryVo = yxShipAppointService.getYxShipAppointById(idParam.getId());
        return ApiResult.ok(yxShipAppointQueryVo);
    }

    /**
     * 船只预约表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipAppoint分页列表",notes = "船只预约表分页列表",response = YxShipAppointQueryVo.class)
    public ApiResult<Paging<YxShipAppointQueryVo>> getYxShipAppointPageList(@Valid @RequestBody(required = false) YxShipAppointQueryParam yxShipAppointQueryParam) throws Exception{
        Paging<YxShipAppointQueryVo> paging = yxShipAppointService.getYxShipAppointPageList(yxShipAppointQueryParam);
        return ApiResult.ok(paging);
    }

}

