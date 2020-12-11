package co.yixiang.modules.coupons.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
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
 * 本地生活, 卡券表 前端控制器
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
 */
@Slf4j
@RestController
@RequestMapping("/yxCoupons")
@Api("本地生活, 卡券表 API")
public class YxCouponsController extends BaseController {

    @Autowired
    private YxCouponsService yxCouponsService;

    /**
     * 本地生活, 卡券表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCoupons分页列表",notes = "本地生活, 卡券表分页列表",response = YxCouponsQueryVo.class)
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageList(@RequestBody @Valid YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        log.info("获取商品信息接口",yxCouponsQueryParam);
        ApiResult apiResult = yxCouponsService.selectYxCouponsPageList(yxCouponsQueryParam);

        return apiResult;
    }

}

