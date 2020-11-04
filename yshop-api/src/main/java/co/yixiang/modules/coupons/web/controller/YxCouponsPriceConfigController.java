package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.service.YxCouponsPriceConfigService;
import co.yixiang.modules.coupons.web.param.YxCouponsPriceConfigQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsPriceConfigQueryVo;
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
 * 价格配置 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponsPriceConfig")
@Api("价格配置 API")
public class YxCouponsPriceConfigController extends BaseController {

    @Autowired
    private YxCouponsPriceConfigService yxCouponsPriceConfigService;

    /**
    * 添加价格配置
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponsPriceConfig对象",notes = "添加价格配置",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponsPriceConfig(@Valid @RequestBody YxCouponsPriceConfig yxCouponsPriceConfig) throws Exception{
        boolean flag = yxCouponsPriceConfigService.save(yxCouponsPriceConfig);
        return ApiResult.result(flag);
    }

    /**
    * 修改价格配置
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponsPriceConfig对象",notes = "修改价格配置",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponsPriceConfig(@Valid @RequestBody YxCouponsPriceConfig yxCouponsPriceConfig) throws Exception{
        boolean flag = yxCouponsPriceConfigService.updateById(yxCouponsPriceConfig);
        return ApiResult.result(flag);
    }

    /**
    * 删除价格配置
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponsPriceConfig对象",notes = "删除价格配置",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponsPriceConfig(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponsPriceConfigService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取价格配置
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponsPriceConfig对象详情",notes = "查看价格配置",response = YxCouponsPriceConfigQueryVo.class)
    public ApiResult<YxCouponsPriceConfigQueryVo> getYxCouponsPriceConfig(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponsPriceConfigQueryVo yxCouponsPriceConfigQueryVo = yxCouponsPriceConfigService.getYxCouponsPriceConfigById(idParam.getId());
        return ApiResult.ok(yxCouponsPriceConfigQueryVo);
    }

    /**
     * 价格配置分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponsPriceConfig分页列表",notes = "价格配置分页列表",response = YxCouponsPriceConfigQueryVo.class)
    public ApiResult<Paging<YxCouponsPriceConfigQueryVo>> getYxCouponsPriceConfigPageList(@Valid @RequestBody(required = false) YxCouponsPriceConfigQueryParam yxCouponsPriceConfigQueryParam) throws Exception{
        Paging<YxCouponsPriceConfigQueryVo> paging = yxCouponsPriceConfigService.getYxCouponsPriceConfigPageList(yxCouponsPriceConfigQueryParam);
        return ApiResult.ok(paging);
    }

}

