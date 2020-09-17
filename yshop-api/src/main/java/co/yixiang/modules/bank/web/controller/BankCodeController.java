package co.yixiang.modules.bank.web.controller;

import co.yixiang.modules.bank.entity.BankCode;
import co.yixiang.modules.bank.service.BankCodeService;
import co.yixiang.modules.bank.web.param.BankCodeQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeQueryVo;
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
 * 联行号表 前端控制器
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@RestController
@RequestMapping("/bankCode")
@Api("联行号表 API")
public class BankCodeController extends BaseController {

    @Autowired
    private BankCodeService bankCodeService;

    /**
    * 添加联行号表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加BankCode对象",notes = "添加联行号表",response = ApiResult.class)
    public ApiResult<Boolean> addBankCode(@Valid @RequestBody BankCode bankCode) throws Exception{
        boolean flag = bankCodeService.save(bankCode);
        return ApiResult.result(flag);
    }

    /**
    * 修改联行号表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改BankCode对象",notes = "修改联行号表",response = ApiResult.class)
    public ApiResult<Boolean> updateBankCode(@Valid @RequestBody BankCode bankCode) throws Exception{
        boolean flag = bankCodeService.updateById(bankCode);
        return ApiResult.result(flag);
    }

    /**
    * 删除联行号表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除BankCode对象",notes = "删除联行号表",response = ApiResult.class)
    public ApiResult<Boolean> deleteBankCode(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = bankCodeService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取联行号表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取BankCode对象详情",notes = "查看联行号表",response = BankCodeQueryVo.class)
    public ApiResult<BankCodeQueryVo> getBankCode(@Valid @RequestBody IdParam idParam) throws Exception{
        BankCodeQueryVo bankCodeQueryVo = bankCodeService.getBankCodeById(idParam.getId());
        return ApiResult.ok(bankCodeQueryVo);
    }

    /**
     * 联行号表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取BankCode分页列表",notes = "联行号表分页列表",response = BankCodeQueryVo.class)
    public ApiResult<Paging<BankCodeQueryVo>> getBankCodePageList(@Valid @RequestBody(required = false) BankCodeQueryParam bankCodeQueryParam) throws Exception{
        Paging<BankCodeQueryVo> paging = bankCodeService.getBankCodePageList(bankCodeQueryParam);
        return ApiResult.ok(paging);
    }

}

