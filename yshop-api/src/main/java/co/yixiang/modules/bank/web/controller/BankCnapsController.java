package co.yixiang.modules.bank.web.controller;

import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.modules.bank.service.BankCnapsService;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
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
 * 银行机构编码 前端控制器
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@RestController
@RequestMapping("/bankCnaps")
@Api("银行机构编码 API")
public class BankCnapsController extends BaseController {

    @Autowired
    private BankCnapsService bankCnapsService;

    /**
    * 添加银行机构编码
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加BankCnaps对象",notes = "添加银行机构编码",response = ApiResult.class)
    public ApiResult<Boolean> addBankCnaps(@Valid @RequestBody BankCnaps bankCnaps) throws Exception{
        boolean flag = bankCnapsService.save(bankCnaps);
        return ApiResult.result(flag);
    }

    /**
    * 修改银行机构编码
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改BankCnaps对象",notes = "修改银行机构编码",response = ApiResult.class)
    public ApiResult<Boolean> updateBankCnaps(@Valid @RequestBody BankCnaps bankCnaps) throws Exception{
        boolean flag = bankCnapsService.updateById(bankCnaps);
        return ApiResult.result(flag);
    }

    /**
    * 删除银行机构编码
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除BankCnaps对象",notes = "删除银行机构编码",response = ApiResult.class)
    public ApiResult<Boolean> deleteBankCnaps(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = bankCnapsService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取银行机构编码
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取BankCnaps对象详情",notes = "查看银行机构编码",response = BankCnapsQueryVo.class)
    public ApiResult<BankCnapsQueryVo> getBankCnaps(@Valid @RequestBody IdParam idParam) throws Exception{
        BankCnapsQueryVo bankCnapsQueryVo = bankCnapsService.getBankCnapsById(idParam.getId());
        return ApiResult.ok(bankCnapsQueryVo);
    }

    /**
     * 银行机构编码分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取BankCnaps分页列表",notes = "银行机构编码分页列表",response = BankCnapsQueryVo.class)
    public ApiResult<Paging<BankCnapsQueryVo>> getBankCnapsPageList(@Valid @RequestBody(required = false) BankCnapsQueryParam bankCnapsQueryParam) throws Exception{
        Paging<BankCnapsQueryVo> paging = bankCnapsService.getBankCnapsPageList(bankCnapsQueryParam);
        return ApiResult.ok(paging);
    }

}

