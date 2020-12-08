package co.yixiang.modules.ship.web.controller;

import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
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
 * 船只表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxShipInfo")
@Api("船只表 API")
public class YxShipInfoController extends BaseController {

    @Autowired
    private YxShipInfoService yxShipInfoService;

    /**
    * 添加船只表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxShipInfo对象",notes = "添加船只表",response = ApiResult.class)
    public ApiResult<Boolean> addYxShipInfo(@Valid @RequestBody YxShipInfo yxShipInfo) throws Exception{
        boolean flag = yxShipInfoService.save(yxShipInfo);
        return ApiResult.result(flag);
    }

    /**
    * 修改船只表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxShipInfo对象",notes = "修改船只表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxShipInfo(@Valid @RequestBody YxShipInfo yxShipInfo) throws Exception{
        boolean flag = yxShipInfoService.updateById(yxShipInfo);
        return ApiResult.result(flag);
    }

    /**
    * 删除船只表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxShipInfo对象",notes = "删除船只表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxShipInfo(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxShipInfoService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船只表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxShipInfo对象详情",notes = "查看船只表",response = YxShipInfoQueryVo.class)
    public ApiResult<YxShipInfoQueryVo> getYxShipInfo(@Valid @RequestBody IdParam idParam) throws Exception{
        YxShipInfoQueryVo yxShipInfoQueryVo = yxShipInfoService.getYxShipInfoById(idParam.getId());
        return ApiResult.ok(yxShipInfoQueryVo);
    }

    /**
     * 船只表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxShipInfo分页列表",notes = "船只表分页列表",response = YxShipInfoQueryVo.class)
    public ApiResult<Paging<YxShipInfoQueryVo>> getYxShipInfoPageList(@Valid @RequestBody(required = false) YxShipInfoQueryParam yxShipInfoQueryParam) throws Exception{
        Paging<YxShipInfoQueryVo> paging = yxShipInfoService.getYxShipInfoPageList(yxShipInfoQueryParam);
        return ApiResult.ok(paging);
    }

}

