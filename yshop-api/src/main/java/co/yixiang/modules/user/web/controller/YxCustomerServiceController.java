package co.yixiang.modules.user.web.controller;

import co.yixiang.modules.user.entity.YxCustomerService;
import co.yixiang.modules.user.service.YxCustomerServiceService;
import co.yixiang.modules.user.web.param.YxCustomerServiceQueryParam;
import co.yixiang.modules.user.web.vo.YxCustomerServiceQueryVo;
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
 * 机器人客服表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCustomerService")
@Api("机器人客服表 API")
public class YxCustomerServiceController extends BaseController {

    @Autowired
    private YxCustomerServiceService yxCustomerServiceService;

    /**
    * 添加机器人客服表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCustomerService对象",notes = "添加机器人客服表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCustomerService(@Valid @RequestBody YxCustomerService yxCustomerService) throws Exception{
        boolean flag = yxCustomerServiceService.save(yxCustomerService);
        return ApiResult.result(flag);
    }

    /**
    * 修改机器人客服表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCustomerService对象",notes = "修改机器人客服表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCustomerService(@Valid @RequestBody YxCustomerService yxCustomerService) throws Exception{
        boolean flag = yxCustomerServiceService.updateById(yxCustomerService);
        return ApiResult.result(flag);
    }

    /**
    * 删除机器人客服表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCustomerService对象",notes = "删除机器人客服表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCustomerService(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCustomerServiceService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取机器人客服表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCustomerService对象详情",notes = "查看机器人客服表",response = YxCustomerServiceQueryVo.class)
    public ApiResult<YxCustomerServiceQueryVo> getYxCustomerService(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCustomerServiceQueryVo yxCustomerServiceQueryVo = yxCustomerServiceService.getYxCustomerServiceById(idParam.getId());
        return ApiResult.ok(yxCustomerServiceQueryVo);
    }

    /**
     * 机器人客服表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCustomerService分页列表",notes = "机器人客服表分页列表",response = YxCustomerServiceQueryVo.class)
    public ApiResult<Paging<YxCustomerServiceQueryVo>> getYxCustomerServicePageList(@Valid @RequestBody(required = false) YxCustomerServiceQueryParam yxCustomerServiceQueryParam) throws Exception{
        Paging<YxCustomerServiceQueryVo> paging = yxCustomerServiceService.getYxCustomerServicePageList(yxCustomerServiceQueryParam);
        return ApiResult.ok(paging);
    }

}

