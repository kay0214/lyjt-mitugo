package co.yixiang.modules.bill.web.controller;

import co.yixiang.modules.bill.entity.YxWxUserBill;
import co.yixiang.modules.bill.service.YxWxUserBillService;
import co.yixiang.modules.bill.web.param.YxWxUserBillQueryParam;
import co.yixiang.modules.bill.web.vo.YxWxUserBillQueryVo;
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
 * 用户账单明细表 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-12-10
 */
@Slf4j
@RestController
@RequestMapping("/yxWxUserBill")
@Api("用户账单明细表 API")
public class YxWxUserBillController extends BaseController {

    @Autowired
    private YxWxUserBillService yxWxUserBillService;

    /**
    * 添加用户账单明细表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxWxUserBill对象",notes = "添加用户账单明细表",response = ApiResult.class)
    public ApiResult<Boolean> addYxWxUserBill(@Valid @RequestBody YxWxUserBill yxWxUserBill) throws Exception{
        boolean flag = yxWxUserBillService.save(yxWxUserBill);
        return ApiResult.result(flag);
    }

    /**
    * 修改用户账单明细表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxWxUserBill对象",notes = "修改用户账单明细表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxWxUserBill(@Valid @RequestBody YxWxUserBill yxWxUserBill) throws Exception{
        boolean flag = yxWxUserBillService.updateById(yxWxUserBill);
        return ApiResult.result(flag);
    }

    /**
    * 删除用户账单明细表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxWxUserBill对象",notes = "删除用户账单明细表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxWxUserBill(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxWxUserBillService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取用户账单明细表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxWxUserBill对象详情",notes = "查看用户账单明细表",response = YxWxUserBillQueryVo.class)
    public ApiResult<YxWxUserBillQueryVo> getYxWxUserBill(@Valid @RequestBody IdParam idParam) throws Exception{
        YxWxUserBillQueryVo yxWxUserBillQueryVo = yxWxUserBillService.getYxWxUserBillById(idParam.getId());
        return ApiResult.ok(yxWxUserBillQueryVo);
    }

    /**
     * 用户账单明细表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxWxUserBill分页列表",notes = "用户账单明细表分页列表",response = YxWxUserBillQueryVo.class)
    public ApiResult<Paging<YxWxUserBillQueryVo>> getYxWxUserBillPageList(@Valid @RequestBody(required = false) YxWxUserBillQueryParam yxWxUserBillQueryParam) throws Exception{
        Paging<YxWxUserBillQueryVo> paging = yxWxUserBillService.getYxWxUserBillPageList(yxWxUserBillQueryParam);
        return ApiResult.ok(paging);
    }

}

