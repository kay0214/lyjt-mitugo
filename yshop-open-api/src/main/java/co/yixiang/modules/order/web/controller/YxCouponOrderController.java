package co.yixiang.modules.order.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.order.service.YxCouponOrderService;
import co.yixiang.modules.order.web.param.OrderStatusQueryParam;
import co.yixiang.modules.order.web.vo.YxCouponOrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 卡券订单表 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponOrder")
@Api("卡券订单表 API")
public class YxCouponOrderController extends BaseController {

    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    /**
     * 获取卡券订单表
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponOrder对象详情", notes = "查看卡券订单表", response = YxCouponOrderQueryVo.class)
    public ApiResult<YxCouponOrderQueryVo> getYxCouponOrder(@Valid @RequestBody OrderStatusQueryParam idParam) throws Exception {
        log.info("订单状态查询接口:{}", idParam);
        ApiResult apiResult = yxCouponOrderService.selectByOrderId(idParam);
        return apiResult;
    }

}

