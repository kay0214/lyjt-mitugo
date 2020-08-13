package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponsCategory;
import co.yixiang.modules.coupons.service.YxCouponsCategoryService;
import co.yixiang.modules.coupons.web.param.YxCouponsCategoryQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsCategoryQueryVo;
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
 * 本地生活, 卡券分类表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponsCategory")
@Api("本地生活, 卡券分类表 API")
public class YxCouponsCategoryController extends BaseController {

    @Autowired
    private YxCouponsCategoryService yxCouponsCategoryService;

    /**
    * 添加本地生活, 卡券分类表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponsCategory对象",notes = "添加本地生活, 卡券分类表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponsCategory(@Valid @RequestBody YxCouponsCategory yxCouponsCategory) throws Exception{
        boolean flag = yxCouponsCategoryService.save(yxCouponsCategory);
        return ApiResult.result(flag);
    }

    /**
    * 修改本地生活, 卡券分类表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponsCategory对象",notes = "修改本地生活, 卡券分类表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponsCategory(@Valid @RequestBody YxCouponsCategory yxCouponsCategory) throws Exception{
        boolean flag = yxCouponsCategoryService.updateById(yxCouponsCategory);
        return ApiResult.result(flag);
    }

    /**
    * 删除本地生活, 卡券分类表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponsCategory对象",notes = "删除本地生活, 卡券分类表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponsCategory(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponsCategoryService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取本地生活, 卡券分类表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponsCategory对象详情",notes = "查看本地生活, 卡券分类表",response = YxCouponsCategoryQueryVo.class)
    public ApiResult<YxCouponsCategoryQueryVo> getYxCouponsCategory(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponsCategoryQueryVo yxCouponsCategoryQueryVo = yxCouponsCategoryService.getYxCouponsCategoryById(idParam.getId());
        return ApiResult.ok(yxCouponsCategoryQueryVo);
    }

    /**
     * 本地生活, 卡券分类表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponsCategory分页列表",notes = "本地生活, 卡券分类表分页列表",response = YxCouponsCategoryQueryVo.class)
    public ApiResult<Paging<YxCouponsCategoryQueryVo>> getYxCouponsCategoryPageList(@Valid @RequestBody(required = false) YxCouponsCategoryQueryParam yxCouponsCategoryQueryParam) throws Exception{
        Paging<YxCouponsCategoryQueryVo> paging = yxCouponsCategoryService.getYxCouponsCategoryPageList(yxCouponsCategoryQueryParam);
        return ApiResult.ok(paging);
    }

}

