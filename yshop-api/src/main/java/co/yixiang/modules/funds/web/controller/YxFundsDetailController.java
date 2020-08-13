package co.yixiang.modules.funds.web.controller;

import co.yixiang.modules.funds.entity.YxFundsDetail;
import co.yixiang.modules.funds.service.YxFundsDetailService;
import co.yixiang.modules.funds.web.param.YxFundsDetailQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsDetailQueryVo;
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
 * 平台资金明细 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxFundsDetail")
@Api("平台资金明细 API")
public class YxFundsDetailController extends BaseController {

    @Autowired
    private YxFundsDetailService yxFundsDetailService;

    /**
    * 添加平台资金明细
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxFundsDetail对象",notes = "添加平台资金明细",response = ApiResult.class)
    public ApiResult<Boolean> addYxFundsDetail(@Valid @RequestBody YxFundsDetail yxFundsDetail) throws Exception{
        boolean flag = yxFundsDetailService.save(yxFundsDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改平台资金明细
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxFundsDetail对象",notes = "修改平台资金明细",response = ApiResult.class)
    public ApiResult<Boolean> updateYxFundsDetail(@Valid @RequestBody YxFundsDetail yxFundsDetail) throws Exception{
        boolean flag = yxFundsDetailService.updateById(yxFundsDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除平台资金明细
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxFundsDetail对象",notes = "删除平台资金明细",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxFundsDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxFundsDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取平台资金明细
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxFundsDetail对象详情",notes = "查看平台资金明细",response = YxFundsDetailQueryVo.class)
    public ApiResult<YxFundsDetailQueryVo> getYxFundsDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxFundsDetailQueryVo yxFundsDetailQueryVo = yxFundsDetailService.getYxFundsDetailById(idParam.getId());
        return ApiResult.ok(yxFundsDetailQueryVo);
    }

    /**
     * 平台资金明细分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxFundsDetail分页列表",notes = "平台资金明细分页列表",response = YxFundsDetailQueryVo.class)
    public ApiResult<Paging<YxFundsDetailQueryVo>> getYxFundsDetailPageList(@Valid @RequestBody(required = false) YxFundsDetailQueryParam yxFundsDetailQueryParam) throws Exception{
        Paging<YxFundsDetailQueryVo> paging = yxFundsDetailService.getYxFundsDetailPageList(yxFundsDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

