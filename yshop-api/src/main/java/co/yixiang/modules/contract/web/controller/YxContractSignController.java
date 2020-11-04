package co.yixiang.modules.contract.web.controller;

import co.yixiang.modules.contract.entity.YxContractSign;
import co.yixiang.modules.contract.service.YxContractSignService;
import co.yixiang.modules.contract.web.param.YxContractSignQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractSignQueryVo;
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
 * 合同签署表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxContractSign")
@Api("合同签署表 API")
public class YxContractSignController extends BaseController {

    @Autowired
    private YxContractSignService yxContractSignService;

    /**
    * 添加合同签署表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxContractSign对象",notes = "添加合同签署表",response = ApiResult.class)
    public ApiResult<Boolean> addYxContractSign(@Valid @RequestBody YxContractSign yxContractSign) throws Exception{
        boolean flag = yxContractSignService.save(yxContractSign);
        return ApiResult.result(flag);
    }

    /**
    * 修改合同签署表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxContractSign对象",notes = "修改合同签署表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxContractSign(@Valid @RequestBody YxContractSign yxContractSign) throws Exception{
        boolean flag = yxContractSignService.updateById(yxContractSign);
        return ApiResult.result(flag);
    }

    /**
    * 删除合同签署表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxContractSign对象",notes = "删除合同签署表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxContractSign(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxContractSignService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取合同签署表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxContractSign对象详情",notes = "查看合同签署表",response = YxContractSignQueryVo.class)
    public ApiResult<YxContractSignQueryVo> getYxContractSign(@Valid @RequestBody IdParam idParam) throws Exception{
        YxContractSignQueryVo yxContractSignQueryVo = yxContractSignService.getYxContractSignById(idParam.getId());
        return ApiResult.ok(yxContractSignQueryVo);
    }

    /**
     * 合同签署表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxContractSign分页列表",notes = "合同签署表分页列表",response = YxContractSignQueryVo.class)
    public ApiResult<Paging<YxContractSignQueryVo>> getYxContractSignPageList(@Valid @RequestBody(required = false) YxContractSignQueryParam yxContractSignQueryParam) throws Exception{
        Paging<YxContractSignQueryVo> paging = yxContractSignService.getYxContractSignPageList(yxContractSignQueryParam);
        return ApiResult.ok(paging);
    }

}

