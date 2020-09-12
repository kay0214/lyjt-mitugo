/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.pay;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.pay.param.PaySeachParam;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.service.YxUserExtractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "中行相关", tags = "中行相关", description = "中行相关")
public class PayController extends BaseController {



    private final YxUserExtractService userExtractService;
    @Autowired
    private YxStoreProductService yxStoreProductService;


    /**
     * 提现订单确认
     */
    @AnonymousAccess
    @PostMapping("/pay/confirmOrder")
    @ApiOperation(value = "提现订单确认", notes = "提现订单确认")
    public ApiResult confirmOrder(@RequestBody PaySeachParam param) {
        YxUserExtract userExtract = userExtractService.getConfirmOrder(param);

        if(userExtract!=null){
            return ApiResult.ok();
        }

        return ApiResult.fail("不允许提现");
    }

}

