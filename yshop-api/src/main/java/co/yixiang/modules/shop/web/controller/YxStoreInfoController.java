package co.yixiang.modules.shop.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Slf4j
@RestController
@RequestMapping("/yxStoreInfo")
@Api(value = "商铺模块", tags = "商城：商铺信息", description = "商铺模块")
public class YxStoreInfoController extends BaseController {

    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    /**
    * 添加店铺表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxStoreInfo对象",notes = "添加店铺表",response = ApiResult.class)
    public ApiResult<Boolean> addYxStoreInfo(@Valid @RequestBody YxStoreInfo yxStoreInfo) throws Exception{
        boolean flag = yxStoreInfoService.save(yxStoreInfo);
        return ApiResult.result(flag);
    }

    /**
    * 修改店铺表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxStoreInfo对象",notes = "修改店铺表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxStoreInfo(@Valid @RequestBody YxStoreInfo yxStoreInfo) throws Exception{
        boolean flag = yxStoreInfoService.updateById(yxStoreInfo);
        return ApiResult.result(flag);
    }

    /**
    * 删除店铺表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxStoreInfo对象",notes = "删除店铺表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxStoreInfo(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxStoreInfoService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取店铺表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxStoreInfo对象详情",notes = "查看店铺表",response = YxStoreInfoQueryVo.class)
    public ApiResult<YxStoreInfoQueryVo> getYxStoreInfo(@Valid @RequestBody IdParam idParam) {
        YxStoreInfoQueryVo yxStoreInfoQueryVo = yxStoreInfoService.getYxStoreInfoById(idParam.getId());
        return ApiResult.ok(yxStoreInfoQueryVo);
    }

    /**
     * 店铺表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxStoreInfo分页列表",notes = "店铺表分页列表",response = YxStoreInfoQueryVo.class)
    public ApiResult<Paging<YxStoreInfoQueryVo>> getYxStoreInfoPageList(@RequestBody YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception{
        Paging<YxStoreInfoQueryVo> paging = yxStoreInfoService.getYxStoreInfoPageList(yxStoreInfoQueryParam);
        return ApiResult.ok(paging);
    }
    @AnonymousAccess
    @PostMapping("/getStoreInfoList")
    @ApiOperation(value = "查询店铺列表信息",notes = "查询店铺列表信息")
    public ApiResult<List<YxStoreInfoQueryVo>> productsAndStore(@RequestBody YxStoreInfoQueryParam yxStoreInfoQueryParam){
        List<YxStoreInfoQueryVo> yxStoreInfoQueryVoList = yxStoreInfoService.getStoreInfoList(yxStoreInfoQueryParam);
        return ApiResult.ok(yxStoreInfoQueryVoList);
    }

}

