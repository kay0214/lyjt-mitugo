package co.yixiang.modules.offpay.web.controller;

import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.service.YxOffPayOrderService;
import co.yixiang.modules.offpay.web.param.YxOffPayOrderQueryParam;
import co.yixiang.modules.offpay.web.vo.YxOffPayOrderQueryVo;
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
 * 线下支付订单表 前端控制器
 * </p>
 *
 * @author sss
 * @since 2020-09-05
 */
@Slf4j
@RestController
@RequestMapping("/yxOffPayOrder")
@Api("线下支付订单表 API")
public class YxOffPayOrderController extends BaseController {

    @Autowired
    private YxOffPayOrderService yxOffPayOrderService;

    /**
    * 添加线下支付订单表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxOffPayOrder对象",notes = "添加线下支付订单表",response = ApiResult.class)
    public ApiResult<Boolean> addYxOffPayOrder(@Valid @RequestBody YxOffPayOrder yxOffPayOrder) throws Exception{
        boolean flag = yxOffPayOrderService.save(yxOffPayOrder);
        return ApiResult.result(flag);
    }

    /**
    * 修改线下支付订单表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxOffPayOrder对象",notes = "修改线下支付订单表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxOffPayOrder(@Valid @RequestBody YxOffPayOrder yxOffPayOrder) throws Exception{
        boolean flag = yxOffPayOrderService.updateById(yxOffPayOrder);
        return ApiResult.result(flag);
    }

    /**
    * 删除线下支付订单表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxOffPayOrder对象",notes = "删除线下支付订单表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxOffPayOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxOffPayOrderService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取线下支付订单表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxOffPayOrder对象详情",notes = "查看线下支付订单表",response = YxOffPayOrderQueryVo.class)
    public ApiResult<YxOffPayOrderQueryVo> getYxOffPayOrder(@Valid @RequestBody IdParam idParam) throws Exception{
        YxOffPayOrderQueryVo yxOffPayOrderQueryVo = yxOffPayOrderService.getYxOffPayOrderById(idParam.getId());
        return ApiResult.ok(yxOffPayOrderQueryVo);
    }

    /**
     * 线下支付订单表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxOffPayOrder分页列表",notes = "线下支付订单表分页列表",response = YxOffPayOrderQueryVo.class)
    public ApiResult<Paging<YxOffPayOrderQueryVo>> getYxOffPayOrderPageList(@Valid @RequestBody(required = false) YxOffPayOrderQueryParam yxOffPayOrderQueryParam) throws Exception{
        Paging<YxOffPayOrderQueryVo> paging = yxOffPayOrderService.getYxOffPayOrderPageList(yxOffPayOrderQueryParam);
        return ApiResult.ok(paging);
    }

}

