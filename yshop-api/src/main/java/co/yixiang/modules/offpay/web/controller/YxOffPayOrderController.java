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


}

