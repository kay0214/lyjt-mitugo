package co.yixiang.modules.shop.web.controller;

import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.modules.shop.service.YxStoreAttributeService;
import co.yixiang.modules.shop.web.param.YxStoreAttributeQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreAttributeQueryVo;
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
 * 店铺属性表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxStoreAttribute")
@Api("店铺属性表 API")
public class YxStoreAttributeController extends BaseController {

    @Autowired
    private YxStoreAttributeService yxStoreAttributeService;

    /**
    * 添加店铺属性表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxStoreAttribute对象",notes = "添加店铺属性表",response = ApiResult.class)
    public ApiResult<Boolean> addYxStoreAttribute(@Valid @RequestBody YxStoreAttribute yxStoreAttribute) throws Exception{
        boolean flag = yxStoreAttributeService.save(yxStoreAttribute);
        return ApiResult.result(flag);
    }

    /**
    * 修改店铺属性表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxStoreAttribute对象",notes = "修改店铺属性表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxStoreAttribute(@Valid @RequestBody YxStoreAttribute yxStoreAttribute) throws Exception{
        boolean flag = yxStoreAttributeService.updateById(yxStoreAttribute);
        return ApiResult.result(flag);
    }

    /**
    * 删除店铺属性表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxStoreAttribute对象",notes = "删除店铺属性表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxStoreAttribute(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxStoreAttributeService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取店铺属性表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxStoreAttribute对象详情",notes = "查看店铺属性表",response = YxStoreAttributeQueryVo.class)
    public ApiResult<YxStoreAttributeQueryVo> getYxStoreAttribute(@Valid @RequestBody IdParam idParam) throws Exception{
        YxStoreAttributeQueryVo yxStoreAttributeQueryVo = yxStoreAttributeService.getYxStoreAttributeById(idParam.getId());
        return ApiResult.ok(yxStoreAttributeQueryVo);
    }

    /**
     * 店铺属性表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxStoreAttribute分页列表",notes = "店铺属性表分页列表",response = YxStoreAttributeQueryVo.class)
    public ApiResult<Paging<YxStoreAttributeQueryVo>> getYxStoreAttributePageList(@Valid @RequestBody(required = false) YxStoreAttributeQueryParam yxStoreAttributeQueryParam) throws Exception{
        Paging<YxStoreAttributeQueryVo> paging = yxStoreAttributeService.getYxStoreAttributePageList(yxStoreAttributeQueryParam);
        return ApiResult.ok(paging);
    }

}

