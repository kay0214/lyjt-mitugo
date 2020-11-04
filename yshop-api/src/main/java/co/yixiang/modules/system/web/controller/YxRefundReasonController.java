package co.yixiang.modules.system.web.controller;

import co.yixiang.modules.system.entity.YxRefundReason;
import co.yixiang.modules.system.service.YxRefundReasonService;
import co.yixiang.modules.system.web.param.YxRefundReasonQueryParam;
import co.yixiang.modules.system.web.vo.YxRefundReasonQueryVo;
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
 * 退款理由配置表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxRefundReason")
@Api("退款理由配置表 API")
public class YxRefundReasonController extends BaseController {

    @Autowired
    private YxRefundReasonService yxRefundReasonService;

    /**
    * 添加退款理由配置表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxRefundReason对象",notes = "添加退款理由配置表",response = ApiResult.class)
    public ApiResult<Boolean> addYxRefundReason(@Valid @RequestBody YxRefundReason yxRefundReason) throws Exception{
        boolean flag = yxRefundReasonService.save(yxRefundReason);
        return ApiResult.result(flag);
    }

    /**
    * 修改退款理由配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxRefundReason对象",notes = "修改退款理由配置表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxRefundReason(@Valid @RequestBody YxRefundReason yxRefundReason) throws Exception{
        boolean flag = yxRefundReasonService.updateById(yxRefundReason);
        return ApiResult.result(flag);
    }

    /**
    * 删除退款理由配置表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxRefundReason对象",notes = "删除退款理由配置表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxRefundReason(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxRefundReasonService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取退款理由配置表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxRefundReason对象详情",notes = "查看退款理由配置表",response = YxRefundReasonQueryVo.class)
    public ApiResult<YxRefundReasonQueryVo> getYxRefundReason(@Valid @RequestBody IdParam idParam) throws Exception{
        YxRefundReasonQueryVo yxRefundReasonQueryVo = yxRefundReasonService.getYxRefundReasonById(idParam.getId());
        return ApiResult.ok(yxRefundReasonQueryVo);
    }

    /**
     * 退款理由配置表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxRefundReason分页列表",notes = "退款理由配置表分页列表",response = YxRefundReasonQueryVo.class)
    public ApiResult<Paging<YxRefundReasonQueryVo>> getYxRefundReasonPageList(@Valid @RequestBody(required = false) YxRefundReasonQueryParam yxRefundReasonQueryParam) throws Exception{
        Paging<YxRefundReasonQueryVo> paging = yxRefundReasonService.getYxRefundReasonPageList(yxRefundReasonQueryParam);
        return ApiResult.ok(paging);
    }

}

