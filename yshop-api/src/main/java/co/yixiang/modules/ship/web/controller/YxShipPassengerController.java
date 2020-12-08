package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
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
 * 本地生活帆船订单乘客表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipPassenger")
@Api("本地生活帆船订单乘客表 API")
public class YxShipPassengerController extends BaseController {

    @Autowired
    private YxShipPassengerService yxShipPassengerService;

    /**
    * 添加本地生活帆船订单乘客表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipPassenger对象",notes = "添加本地生活帆船订单乘客表",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipPassenger(@Valid @RequestBody YxShipPassenger yxShipPassenger) throws Exception{
        boolean flag = yxShipPassengerService.save(yxShipPassenger);
        return ApiResult.result(flag);
    }

    /**
    * 修改本地生活帆船订单乘客表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipPassenger对象",notes = "修改本地生活帆船订单乘客表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipPassenger(@Valid @RequestBody YxShipPassenger yxShipPassenger) throws Exception{
        boolean flag = yxShipPassengerService.updateById(yxShipPassenger);
        return ApiResult.result(flag);
    }

    /**
    * 删除本地生活帆船订单乘客表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipPassenger对象",notes = "删除本地生活帆船订单乘客表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipPassenger(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipPassengerService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取本地生活帆船订单乘客表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipPassenger对象详情",notes = "查看本地生活帆船订单乘客表",response = YxShipPassengerQueryVo.class)
    public ApiResult<YxShipPassengerQueryVo> getYxShipPassenger(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipPassengerQueryVo yxShipPassengerQueryVo = yxShipPassengerService.getYxShipPassengerById(idParam.getId());
        return ApiResult.ok(yxShipPassengerQueryVo);
    }

    /**
     * 本地生活帆船订单乘客表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipPassenger分页列表",notes = "本地生活帆船订单乘客表分页列表",response = YxShipPassengerQueryVo.class)
    public ApiResult<Paging<YxShipPassengerQueryVo>> getYxShipPassengerPageList(@Valid @RequestBody(required = false) YxShipPassengerQueryParam yxShipPassengerQueryParam) throws Exception{
        Paging<YxShipPassengerQueryVo> paging = yxShipPassengerService.getYxShipPassengerPageList(yxShipPassengerQueryParam);
        return ApiResult.ok(paging);
    }

}

