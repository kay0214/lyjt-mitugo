package co.yixiang.modules.bank.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.bank.service.BankCnapsService;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
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
     * 银行机构编码分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取BankCnaps分页列表",notes = "银行机构编码分页列表",response = BankCnapsQueryVo.class)
    public ApiResult<Paging<BankCnapsQueryVo>> getBankCnapsPageList(@Valid @RequestBody(required = false) BankCnapsQueryParam bankCnapsQueryParam) throws Exception{
        Paging<BankCnapsQueryVo> paging = bankCnapsService.getBankCnapsPageList(bankCnapsQueryParam);
        return ApiResult.ok(paging);
    }

}

