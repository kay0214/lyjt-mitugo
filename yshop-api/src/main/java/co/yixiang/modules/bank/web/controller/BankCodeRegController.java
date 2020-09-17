package co.yixiang.modules.bank.web.controller;

import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.modules.bank.service.BankCodeRegService;
import co.yixiang.modules.bank.web.param.BankCodeRegQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeRegQueryVo;
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
 * 联行号地区代码 前端控制器
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@RestController
@RequestMapping("/bankCodeReg")
@Api("联行号地区代码 API")
public class BankCodeRegController extends BaseController {

    @Autowired
    private BankCodeRegService bankCodeRegService;

    /**
    * 添加联行号地区代码
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加BankCodeReg对象",notes = "添加联行号地区代码",response = ApiResult.class)
    public ApiResult<Boolean> addBankCodeReg(@Valid @RequestBody BankCodeReg bankCodeReg) throws Exception{
        boolean flag = bankCodeRegService.save(bankCodeReg);
        return ApiResult.result(flag);
    }

    /**
    * 修改联行号地区代码
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改BankCodeReg对象",notes = "修改联行号地区代码",response = ApiResult.class)
    public ApiResult<Boolean> updateBankCodeReg(@Valid @RequestBody BankCodeReg bankCodeReg) throws Exception{
        boolean flag = bankCodeRegService.updateById(bankCodeReg);
        return ApiResult.result(flag);
    }

    /**
    * 删除联行号地区代码
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除BankCodeReg对象",notes = "删除联行号地区代码",response = ApiResult.class)
    public ApiResult<Boolean> deleteBankCodeReg(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = bankCodeRegService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取联行号地区代码
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取BankCodeReg对象详情",notes = "查看联行号地区代码",response = BankCodeRegQueryVo.class)
    public ApiResult<BankCodeRegQueryVo> getBankCodeReg(@Valid @RequestBody IdParam idParam) throws Exception{
        BankCodeRegQueryVo bankCodeRegQueryVo = bankCodeRegService.getBankCodeRegById(idParam.getId());
        return ApiResult.ok(bankCodeRegQueryVo);
    }

    /**
     * 联行号地区代码分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取BankCodeReg分页列表",notes = "联行号地区代码分页列表",response = BankCodeRegQueryVo.class)
    public ApiResult<Paging<BankCodeRegQueryVo>> getBankCodeRegPageList(@Valid @RequestBody(required = false) BankCodeRegQueryParam bankCodeRegQueryParam) throws Exception{
        Paging<BankCodeRegQueryVo> paging = bankCodeRegService.getBankCodeRegPageList(bankCodeRegQueryParam);
        return ApiResult.ok(paging);
    }

}

