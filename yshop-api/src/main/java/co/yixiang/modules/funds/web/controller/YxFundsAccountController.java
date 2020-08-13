package co.yixiang.modules.funds.web.controller;

import co.yixiang.modules.funds.entity.YxFundsAccount;
import co.yixiang.modules.funds.service.YxFundsAccountService;
import co.yixiang.modules.funds.web.param.YxFundsAccountQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsAccountQueryVo;
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
 * 平台账户表 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxFundsAccount")
@Api("平台账户表 API")
public class YxFundsAccountController extends BaseController {

    @Autowired
    private YxFundsAccountService yxFundsAccountService;

    /**
    * 添加平台账户表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxFundsAccount对象",notes = "添加平台账户表",response = ApiResult.class)
    public ApiResult<Boolean> addYxFundsAccount(@Valid @RequestBody YxFundsAccount yxFundsAccount) throws Exception{
        boolean flag = yxFundsAccountService.save(yxFundsAccount);
        return ApiResult.result(flag);
    }

    /**
    * 修改平台账户表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxFundsAccount对象",notes = "修改平台账户表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxFundsAccount(@Valid @RequestBody YxFundsAccount yxFundsAccount) throws Exception{
        boolean flag = yxFundsAccountService.updateById(yxFundsAccount);
        return ApiResult.result(flag);
    }

    /**
    * 删除平台账户表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxFundsAccount对象",notes = "删除平台账户表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxFundsAccount(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxFundsAccountService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取平台账户表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxFundsAccount对象详情",notes = "查看平台账户表",response = YxFundsAccountQueryVo.class)
    public ApiResult<YxFundsAccountQueryVo> getYxFundsAccount(@Valid @RequestBody IdParam idParam) throws Exception{
        YxFundsAccountQueryVo yxFundsAccountQueryVo = yxFundsAccountService.getYxFundsAccountById(idParam.getId());
        return ApiResult.ok(yxFundsAccountQueryVo);
    }

    /**
     * 平台账户表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxFundsAccount分页列表",notes = "平台账户表分页列表",response = YxFundsAccountQueryVo.class)
    public ApiResult<Paging<YxFundsAccountQueryVo>> getYxFundsAccountPageList(@Valid @RequestBody(required = false) YxFundsAccountQueryParam yxFundsAccountQueryParam) throws Exception{
        Paging<YxFundsAccountQueryVo> paging = yxFundsAccountService.getYxFundsAccountPageList(yxFundsAccountQueryParam);
        return ApiResult.ok(paging);
    }

}

