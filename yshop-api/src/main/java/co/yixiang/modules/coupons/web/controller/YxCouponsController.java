package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
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
 * 本地生活, 卡券表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCoupons")
@Api("本地生活, 卡券表 API")
public class YxCouponsController extends BaseController {

    @Autowired
    private YxCouponsService yxCouponsService;

    /**
    * 添加本地生活, 卡券表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCoupons对象",notes = "添加本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCoupons(@Valid @RequestBody YxCoupons yxCoupons) throws Exception{
        boolean flag = yxCouponsService.save(yxCoupons);
        return ApiResult.result(flag);
    }

    /**
    * 修改本地生活, 卡券表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCoupons对象",notes = "修改本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCoupons(@Valid @RequestBody YxCoupons yxCoupons) throws Exception{
        boolean flag = yxCouponsService.updateById(yxCoupons);
        return ApiResult.result(flag);
    }

    /**
    * 删除本地生活, 卡券表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCoupons对象",notes = "删除本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCoupons(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponsService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取本地生活, 卡券表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCoupons对象详情",notes = "查看本地生活, 卡券表",response = YxCouponsQueryVo.class)
    public ApiResult<YxCouponsQueryVo> getYxCoupons(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponsQueryVo yxCouponsQueryVo = yxCouponsService.getYxCouponsById(idParam.getId());
        return ApiResult.ok(yxCouponsQueryVo);
    }

    /**
     * 本地生活, 卡券表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCoupons分页列表",notes = "本地生活, 卡券表分页列表",response = YxCouponsQueryVo.class)
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        Paging<YxCouponsQueryVo> paging = yxCouponsService.getYxCouponsPageList(yxCouponsQueryParam);
        return ApiResult.ok(paging);
    }

}

