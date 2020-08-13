package co.yixiang.modules.point.web.controller;

import co.yixiang.modules.point.entity.YxPointDetail;
import co.yixiang.modules.point.service.YxPointDetailService;
import co.yixiang.modules.point.web.param.YxPointDetailQueryParam;
import co.yixiang.modules.point.web.vo.YxPointDetailQueryVo;
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
 * 积分获取明细 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxPointDetail")
@Api("积分获取明细 API")
public class YxPointDetailController extends BaseController {

    @Autowired
    private YxPointDetailService yxPointDetailService;

    /**
    * 添加积分获取明细
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxPointDetail对象",notes = "添加积分获取明细",response = ApiResult.class)
    public ApiResult<Boolean> addYxPointDetail(@Valid @RequestBody YxPointDetail yxPointDetail) throws Exception{
        boolean flag = yxPointDetailService.save(yxPointDetail);
        return ApiResult.result(flag);
    }

    /**
    * 修改积分获取明细
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxPointDetail对象",notes = "修改积分获取明细",response = ApiResult.class)
    public ApiResult<Boolean> updateYxPointDetail(@Valid @RequestBody YxPointDetail yxPointDetail) throws Exception{
        boolean flag = yxPointDetailService.updateById(yxPointDetail);
        return ApiResult.result(flag);
    }

    /**
    * 删除积分获取明细
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxPointDetail对象",notes = "删除积分获取明细",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxPointDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxPointDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取积分获取明细
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxPointDetail对象详情",notes = "查看积分获取明细",response = YxPointDetailQueryVo.class)
    public ApiResult<YxPointDetailQueryVo> getYxPointDetail(@Valid @RequestBody IdParam idParam) throws Exception{
        YxPointDetailQueryVo yxPointDetailQueryVo = yxPointDetailService.getYxPointDetailById(idParam.getId());
        return ApiResult.ok(yxPointDetailQueryVo);
    }

    /**
     * 积分获取明细分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxPointDetail分页列表",notes = "积分获取明细分页列表",response = YxPointDetailQueryVo.class)
    public ApiResult<Paging<YxPointDetailQueryVo>> getYxPointDetailPageList(@Valid @RequestBody(required = false) YxPointDetailQueryParam yxPointDetailQueryParam) throws Exception{
        Paging<YxPointDetailQueryVo> paging = yxPointDetailService.getYxPointDetailPageList(yxPointDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

