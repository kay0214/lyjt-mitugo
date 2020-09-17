package co.yixiang.modules.bank.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.modules.bank.service.BankCnapsService;
import co.yixiang.modules.bank.service.BankCodeRegService;
import co.yixiang.modules.bank.service.BankCodeService;
import co.yixiang.modules.bank.web.param.BankCodeQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeQueryVo;
import co.yixiang.modules.bank.web.vo.BankSelectVo;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private BankCodeRegService regService;

    @Autowired
    private BankCnapsService cnapsService;



    /**
     * 联行号表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取BankCode分页列表",notes = "联行号表分页列表",response = BankCodeQueryVo.class)
    public ApiResult<Paging<BankCodeQueryVo>> getBankCodePageList(@Valid @RequestBody(required = false) BankCodeQueryParam bankCodeQueryParam) throws Exception{
        Paging<BankCodeQueryVo> paging = bankCodeService.getBankCodePageList(bankCodeQueryParam);
        return ApiResult.ok(paging);
    }

    @Cached(name="getAllBankProvince-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.BOTH)
    @PostMapping("/getAllBankProvince")
    @ApiOperation(value = "联行号查询： 查询所有银行和省份",notes = "查询所有银行和省份",response = BankSelectVo.class)
    public ApiResult<BankSelectVo> getAllBank() {

        List<String> provinces = regService.getAllProvinces();
        List<BankCnaps> cnaps = cnapsService.getAllCnaps();
        BankSelectVo result = new BankSelectVo();
        result.setCnaps(cnaps);
        result.setProvinces(provinces);
        return ApiResult.ok(result);

    }


    @PostMapping("/getCitys")
    @ApiOperation(value = "查询所有市 传 name",notes = "查询所有市")
    public ApiResult<Object> getCitys(@RequestBody BankCodeReg param) throws Exception{

        List<String> citys = regService.getAllCitys(param.getName());


        return ApiResult.ok(null);
    }

    @PostMapping("/getBanks")
    @ApiOperation(value = "查询联行号",notes = "查询联行号",response = BankCodeQueryVo.class)
    public ApiResult<Paging<BankCodeQueryVo>> getBanks() throws Exception{
        return ApiResult.ok(null);
    }

}

