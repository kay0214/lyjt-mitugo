package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.modules.ship.service.YxShipOperationDetailService;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationDetailQueryVo;
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
 * 船只运营记录详情 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipOperationDetail")
@Api("船只运营记录详情 API")
public class YxShipOperationDetailController extends BaseController {

    @Autowired
    private YxShipOperationDetailService yxShipOperationDetailService;

    /**
    * 添加船只运营记录详情
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipOperationDetail对象",notes = "添加船只运营记录详情",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipOperationDetail(@Valid @RequestBody YxShipOperationDetail yxShipOperationDetail) throws Exception{
        boolean flag = yxShipOperationDetailService.save(yxShipOperationDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只运营记录详情
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipOperationDetail对象",notes = "修改船只运营记录详情",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipOperationDetail(@Valid @RequestBody YxShipOperationDetail yxShipOperationDetail) throws Exception{
        boolean flag = yxShipOperationDetailService.updateById(yxShipOperationDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只运营记录详情
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipOperationDetail对象",notes = "删除船只运营记录详情",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipOperationDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipOperationDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只运营记录详情
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipOperationDetail对象详情",notes = "查看船只运营记录详情",response = YxShipOperationDetailQueryVo.class)
    public ApiResult<YxShipOperationDetailQueryVo> getYxShipOperationDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipOperationDetailQueryVo yxShipOperationDetailQueryVo = yxShipOperationDetailService.getYxShipOperationDetailById(idParam.getId());
        return ApiResult.ok(yxShipOperationDetailQueryVo);
    }

    /**
     * 船只运营记录详情分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipOperationDetail分页列表",notes = "船只运营记录详情分页列表",response = YxShipOperationDetailQueryVo.class)
    public ApiResult<Paging<YxShipOperationDetailQueryVo>> getYxShipOperationDetailPageList(@Valid @RequestBody(required = false) YxShipOperationDetailQueryParam yxShipOperationDetailQueryParam) throws Exception{
        Paging<YxShipOperationDetailQueryVo> paging = yxShipOperationDetailService.getYxShipOperationDetailPageList(yxShipOperationDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

