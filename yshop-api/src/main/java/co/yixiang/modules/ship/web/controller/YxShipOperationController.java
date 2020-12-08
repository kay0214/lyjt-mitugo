package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.service.YxShipOperationService;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationQueryVo;
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
 * 船只运营记录 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipOperation")
@Api("船只运营记录 API")
public class YxShipOperationController extends BaseController {

    @Autowired
    private YxShipOperationService yxShipOperationService;

    /**
    * 添加船只运营记录
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipOperation对象",notes = "添加船只运营记录",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipOperation(@Valid @RequestBody YxShipOperation yxShipOperation) throws Exception{
        boolean flag = yxShipOperationService.save(yxShipOperation);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只运营记录
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipOperation对象",notes = "修改船只运营记录",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipOperation(@Valid @RequestBody YxShipOperation yxShipOperation) throws Exception{
        boolean flag = yxShipOperationService.updateById(yxShipOperation);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只运营记录
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipOperation对象",notes = "删除船只运营记录",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipOperation(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipOperationService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只运营记录
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipOperation对象详情",notes = "查看船只运营记录",response = YxShipOperationQueryVo.class)
    public ApiResult<YxShipOperationQueryVo> getYxShipOperation(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipOperationQueryVo yxShipOperationQueryVo = yxShipOperationService.getYxShipOperationById(idParam.getId());
        return ApiResult.ok(yxShipOperationQueryVo);
    }

    /**
     * 船只运营记录分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipOperation分页列表",notes = "船只运营记录分页列表",response = YxShipOperationQueryVo.class)
    public ApiResult<Paging<YxShipOperationQueryVo>> getYxShipOperationPageList(@Valid @RequestBody(required = false) YxShipOperationQueryParam yxShipOperationQueryParam) throws Exception{
        Paging<YxShipOperationQueryVo> paging = yxShipOperationService.getYxShipOperationPageList(yxShipOperationQueryParam);
        return ApiResult.ok(paging);
    }

}

