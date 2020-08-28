package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderDetailQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderDetailQueryVo;
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
 * 卡券订单详情表 前端控制器
 * </p>
 *
 * @author liusy
 * @since 2020-08-28
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponOrderDetail")
@Api("卡券订单详情表 API")
public class YxCouponOrderDetailController extends BaseController {

    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;

    /**
    * 添加卡券订单详情表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponOrderDetail对象",notes = "添加卡券订单详情表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponOrderDetail(@Valid @RequestBody YxCouponOrderDetail yxCouponOrderDetail) throws Exception{
        boolean flag = yxCouponOrderDetailService.save(yxCouponOrderDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改卡券订单详情表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponOrderDetail对象",notes = "修改卡券订单详情表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponOrderDetail(@Valid @RequestBody YxCouponOrderDetail yxCouponOrderDetail) throws Exception{
        boolean flag = yxCouponOrderDetailService.updateById(yxCouponOrderDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除卡券订单详情表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponOrderDetail对象",notes = "删除卡券订单详情表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponOrderDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponOrderDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取卡券订单详情表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponOrderDetail对象详情",notes = "查看卡券订单详情表",response = YxCouponOrderDetailQueryVo.class)
    public ApiResult<YxCouponOrderDetailQueryVo> getYxCouponOrderDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponOrderDetailQueryVo yxCouponOrderDetailQueryVo = yxCouponOrderDetailService.getYxCouponOrderDetailById(idParam.getId());
        return ApiResult.ok(yxCouponOrderDetailQueryVo);
    }

    /**
     * 卡券订单详情表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponOrderDetail分页列表",notes = "卡券订单详情表分页列表",response = YxCouponOrderDetailQueryVo.class)
    public ApiResult<Paging<YxCouponOrderDetailQueryVo>> getYxCouponOrderDetailPageList(@Valid @RequestBody(required = false) YxCouponOrderDetailQueryParam yxCouponOrderDetailQueryParam) throws Exception{
        Paging<YxCouponOrderDetailQueryVo> paging = yxCouponOrderDetailService.getYxCouponOrderDetailPageList(yxCouponOrderDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

