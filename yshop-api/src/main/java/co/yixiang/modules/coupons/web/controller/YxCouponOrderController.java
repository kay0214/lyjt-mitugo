package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
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
 * 卡券订单表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponOrder")
@Api("卡券订单表 API")
public class YxCouponOrderController extends BaseController {

    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    /**
    * 添加卡券订单表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponOrder对象",notes = "添加卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception{
        boolean flag = yxCouponOrderService.save(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
    * 修改卡券订单表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponOrder对象",notes = "修改卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponOrder(@Valid @RequestBody YxCouponOrder yxCouponOrder) throws Exception{
        boolean flag = yxCouponOrderService.updateById(yxCouponOrder);
        return ApiResult.result(flag);
    }

    /**
    * 删除卡券订单表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponOrder对象",notes = "删除卡券订单表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponOrderService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取卡券订单表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponOrder对象详情",notes = "查看卡券订单表",response = YxCouponOrderQueryVo.class)
    public ApiResult<YxCouponOrderQueryVo> getYxCouponOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponOrderQueryVo yxCouponOrderQueryVo = yxCouponOrderService.getYxCouponOrderById(idParam.getId());
        return ApiResult.ok(yxCouponOrderQueryVo);
    }

    /**
     * 卡券订单表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponOrder分页列表",notes = "卡券订单表分页列表",response = YxCouponOrderQueryVo.class)
    public ApiResult<Paging<YxCouponOrderQueryVo>> getYxCouponOrderPageList(@Valid @RequestBody(required = false) YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception{
        Paging<YxCouponOrderQueryVo> paging = yxCouponOrderService.getYxCouponOrderPageList(yxCouponOrderQueryParam);
        return ApiResult.ok(paging);
    }

}

