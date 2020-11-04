package co.yixiang.modules.coupons.web.controller;

import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.service.YxCouponsReplyService;
import co.yixiang.modules.coupons.web.param.YxCouponsReplyQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsReplyQueryVo;
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
 * 本地生活评论表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponsReply")
@Api("本地生活评论表 API")
public class YxCouponsReplyController extends BaseController {

    @Autowired
    private YxCouponsReplyService yxCouponsReplyService;

    /**
    * 添加本地生活评论表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCouponsReply对象",notes = "添加本地生活评论表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponsReply(@Valid @RequestBody YxCouponsReply yxCouponsReply) throws Exception{
        boolean flag = yxCouponsReplyService.save(yxCouponsReply);
        return ApiResult.result(flag);
    }

    /**
    * 修改本地生活评论表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCouponsReply对象",notes = "修改本地生活评论表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCouponsReply(@Valid @RequestBody YxCouponsReply yxCouponsReply) throws Exception{
        boolean flag = yxCouponsReplyService.updateById(yxCouponsReply);
        return ApiResult.result(flag);
    }

    /**
    * 删除本地生活评论表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCouponsReply对象",notes = "删除本地生活评论表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponsReply(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponsReplyService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取本地生活评论表
    */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCouponsReply对象详情",notes = "查看本地生活评论表",response = YxCouponsReplyQueryVo.class)
    public ApiResult<YxCouponsReplyQueryVo> getYxCouponsReply(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCouponsReplyQueryVo yxCouponsReplyQueryVo = yxCouponsReplyService.getYxCouponsReplyById(idParam.getId());
        return ApiResult.ok(yxCouponsReplyQueryVo);
    }

    /**
     * 本地生活评论表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCouponsReply分页列表",notes = "本地生活评论表分页列表",response = YxCouponsReplyQueryVo.class)
    public ApiResult<Paging<YxCouponsReplyQueryVo>> getYxCouponsReplyPageList(@Valid @RequestBody(required = false) YxCouponsReplyQueryParam yxCouponsReplyQueryParam) throws Exception{
        Paging<YxCouponsReplyQueryVo> paging = yxCouponsReplyService.getYxCouponsReplyPageList(yxCouponsReplyQueryParam);
        return ApiResult.ok(paging);
    }

}

