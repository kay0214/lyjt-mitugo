package co.yixiang.modules.user.web.controller;

import co.yixiang.modules.user.entity.YxMerchantsSettlement;
import co.yixiang.modules.user.service.YxMerchantsSettlementService;
import co.yixiang.modules.user.web.param.YxMerchantsSettlementQueryParam;
import co.yixiang.modules.user.web.vo.YxMerchantsSettlementQueryVo;
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
 * 商家入驻表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxMerchantsSettlement")
@Api("商家入驻表 API")
public class YxMerchantsSettlementController extends BaseController {

    @Autowired
    private YxMerchantsSettlementService yxMerchantsSettlementService;

    /**
    * 添加商家入驻表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxMerchantsSettlement对象",notes = "添加商家入驻表",response = ApiResult.class)
    public ApiResult<Boolean> addYxMerchantsSettlement(@Valid @RequestBody YxMerchantsSettlement yxMerchantsSettlement) throws Exception{
        boolean flag = yxMerchantsSettlementService.save(yxMerchantsSettlement);
        return ApiResult.result(flag);
    }

    /**
    * 修改商家入驻表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxMerchantsSettlement对象",notes = "修改商家入驻表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxMerchantsSettlement(@Valid @RequestBody YxMerchantsSettlement yxMerchantsSettlement) throws Exception{
        boolean flag = yxMerchantsSettlementService.updateById(yxMerchantsSettlement);
        return ApiResult.result(flag);
    }

    /**
    * 删除商家入驻表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxMerchantsSettlement对象",notes = "删除商家入驻表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxMerchantsSettlement(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxMerchantsSettlementService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取商家入驻表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxMerchantsSettlement对象详情",notes = "查看商家入驻表",response = YxMerchantsSettlementQueryVo.class)
    public ApiResult<YxMerchantsSettlementQueryVo> getYxMerchantsSettlement(@Valid @RequestBody IdParam idParam) throws Exception{
        YxMerchantsSettlementQueryVo yxMerchantsSettlementQueryVo = yxMerchantsSettlementService.getYxMerchantsSettlementById(idParam.getId());
        return ApiResult.ok(yxMerchantsSettlementQueryVo);
    }

    /**
     * 商家入驻表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxMerchantsSettlement分页列表",notes = "商家入驻表分页列表",response = YxMerchantsSettlementQueryVo.class)
    public ApiResult<Paging<YxMerchantsSettlementQueryVo>> getYxMerchantsSettlementPageList(@Valid @RequestBody(required = false) YxMerchantsSettlementQueryParam yxMerchantsSettlementQueryParam) throws Exception{
        Paging<YxMerchantsSettlementQueryVo> paging = yxMerchantsSettlementService.getYxMerchantsSettlementPageList(yxMerchantsSettlementQueryParam);
        return ApiResult.ok(paging);
    }

}

