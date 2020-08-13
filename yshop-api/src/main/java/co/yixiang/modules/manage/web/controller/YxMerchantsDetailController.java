package co.yixiang.modules.manage.web.controller;

import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.manage.web.param.YxMerchantsDetailQueryParam;
import co.yixiang.modules.manage.web.vo.YxMerchantsDetailQueryVo;
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
 * 商户详情 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxMerchantsDetail")
@Api("商户详情 API")
public class YxMerchantsDetailController extends BaseController {

    @Autowired
    private YxMerchantsDetailService yxMerchantsDetailService;

    /**
    * 添加商户详情
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxMerchantsDetail对象",notes = "添加商户详情",response = ApiResult.class)
    public ApiResult<Boolean> addYxMerchantsDetail(@Valid @RequestBody YxMerchantsDetail yxMerchantsDetail) throws Exception{
        boolean flag = yxMerchantsDetailService.save(yxMerchantsDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改商户详情
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxMerchantsDetail对象",notes = "修改商户详情",response = ApiResult.class)
    public ApiResult<Boolean> updateYxMerchantsDetail(@Valid @RequestBody YxMerchantsDetail yxMerchantsDetail) throws Exception{
        boolean flag = yxMerchantsDetailService.updateById(yxMerchantsDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除商户详情
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxMerchantsDetail对象",notes = "删除商户详情",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxMerchantsDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxMerchantsDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取商户详情
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxMerchantsDetail对象详情",notes = "查看商户详情",response = YxMerchantsDetailQueryVo.class)
    public ApiResult<YxMerchantsDetailQueryVo> getYxMerchantsDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxMerchantsDetailQueryVo yxMerchantsDetailQueryVo = yxMerchantsDetailService.getYxMerchantsDetailById(idParam.getId());
        return ApiResult.ok(yxMerchantsDetailQueryVo);
    }

    /**
     * 商户详情分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxMerchantsDetail分页列表",notes = "商户详情分页列表",response = YxMerchantsDetailQueryVo.class)
    public ApiResult<Paging<YxMerchantsDetailQueryVo>> getYxMerchantsDetailPageList(@Valid @RequestBody(required = false) YxMerchantsDetailQueryParam yxMerchantsDetailQueryParam) throws Exception{
        Paging<YxMerchantsDetailQueryVo> paging = yxMerchantsDetailService.getYxMerchantsDetailPageList(yxMerchantsDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

