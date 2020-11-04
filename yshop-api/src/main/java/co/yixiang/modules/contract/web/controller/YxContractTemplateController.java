package co.yixiang.modules.contract.web.controller;

import co.yixiang.modules.contract.entity.YxContractTemplate;
import co.yixiang.modules.contract.service.YxContractTemplateService;
import co.yixiang.modules.contract.web.param.YxContractTemplateQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
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
 * 合同模板 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxContractTemplate")
@Api("合同模板 API")
public class YxContractTemplateController extends BaseController {

    @Autowired
    private YxContractTemplateService yxContractTemplateService;

    /**
    * 添加合同模板
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxContractTemplate对象",notes = "添加合同模板",response = ApiResult.class)
    public ApiResult<Boolean> addYxContractTemplate(@Valid @RequestBody YxContractTemplate yxContractTemplate) throws Exception{
        boolean flag = yxContractTemplateService.save(yxContractTemplate);
        return ApiResult.result(flag);
    }

    /**
    * 修改合同模板
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxContractTemplate对象",notes = "修改合同模板",response = ApiResult.class)
    public ApiResult<Boolean> updateYxContractTemplate(@Valid @RequestBody YxContractTemplate yxContractTemplate) throws Exception{
        boolean flag = yxContractTemplateService.updateById(yxContractTemplate);
        return ApiResult.result(flag);
    }

    /**
    * 删除合同模板
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxContractTemplate对象",notes = "删除合同模板",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxContractTemplate(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxContractTemplateService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取合同模板
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxContractTemplate对象详情",notes = "查看合同模板",response = YxContractTemplateQueryVo.class)
    public ApiResult<YxContractTemplateQueryVo> getYxContractTemplate(@Valid @RequestBody IdParam idParam) throws Exception{
        YxContractTemplateQueryVo yxContractTemplateQueryVo = yxContractTemplateService.getYxContractTemplateById(idParam.getId());
        return ApiResult.ok(yxContractTemplateQueryVo);
    }

    /**
     * 合同模板分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxContractTemplate分页列表",notes = "合同模板分页列表",response = YxContractTemplateQueryVo.class)
    public ApiResult<Paging<YxContractTemplateQueryVo>> getYxContractTemplatePageList(@Valid @RequestBody(required = false) YxContractTemplateQueryParam yxContractTemplateQueryParam) throws Exception{
        Paging<YxContractTemplateQueryVo> paging = yxContractTemplateService.getYxContractTemplatePageList(yxContractTemplateQueryParam);
        return ApiResult.ok(paging);
    }

}

