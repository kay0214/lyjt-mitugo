package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;
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
 * 用户地址表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponOrderUse")
@Api("用户地址表 API")
public class YxCouponOrderUseController extends BaseController {

    @Autowired
    private YxCouponOrderUseService yxCouponOrderUseService;

    /**
    * 添加用户地址表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponOrderUse对象",notes = "添加用户地址表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponOrderUse(@Valid @RequestBody YxCouponOrderUse yxCouponOrderUse) throws Exception{
        boolean flag = yxCouponOrderUseService.save(yxCouponOrderUse);
        return ApiResult.result(flag);
    }

    /**
    * 修改用户地址表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponOrderUse对象",notes = "修改用户地址表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponOrderUse(@Valid @RequestBody YxCouponOrderUse yxCouponOrderUse) throws Exception{
        boolean flag = yxCouponOrderUseService.updateById(yxCouponOrderUse);
        return ApiResult.result(flag);
    }

    /**
    * 删除用户地址表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponOrderUse对象",notes = "删除用户地址表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponOrderUse(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponOrderUseService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取用户地址表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponOrderUse对象详情",notes = "查看用户地址表",response = YxCouponOrderUseQueryVo.class)
    public ApiResult<YxCouponOrderUseQueryVo> getYxCouponOrderUse(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponOrderUseQueryVo yxCouponOrderUseQueryVo = yxCouponOrderUseService.getYxCouponOrderUseById(idParam.getId());
        return ApiResult.ok(yxCouponOrderUseQueryVo);
    }

    /**
     * 用户地址表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponOrderUse分页列表",notes = "用户地址表分页列表",response = YxCouponOrderUseQueryVo.class)
    public ApiResult<Paging<YxCouponOrderUseQueryVo>> getYxCouponOrderUsePageList(@Valid @RequestBody(required = false) YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam) throws Exception{
        Paging<YxCouponOrderUseQueryVo> paging = yxCouponOrderUseService.getYxCouponOrderUsePageList(yxCouponOrderUseQueryParam);
        return ApiResult.ok(paging);
    }

}

