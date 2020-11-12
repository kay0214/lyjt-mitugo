package co.yixiang.modules.coupons.web.controller;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.service.YxCouponsReplyService;
import co.yixiang.modules.coupons.web.vo.couponReply.addReply.YxCouponsAddReplyRequest;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @ApiOperation(value = "添加YxCouponsReply对象", notes = "添加本地生活评论表", response = ApiResult.class)
    public ApiResult<Boolean> addYxCouponsReply(@Valid @RequestBody YxCouponsAddReplyRequest request) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        request.setUid(uid);
        boolean flag = yxCouponsReplyService.createReply(request);
        return ApiResult.result(flag);
    }

    /**
     * 删除本地生活评论表
     */
    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除YxCouponsReply对象", notes = "删除本地生活评论表", response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCouponsReply(@RequestParam("id") Integer id) throws Exception {
        YxCouponsReply findReply = this.yxCouponsReplyService.getById(id);
        if (null == findReply || 1 == findReply.getDelFlag()) {
            throw new BadRequestException("当前评价不存在");
        }
        int uid = SecurityUtils.getUserId().intValue();
        YxCouponsReply deleteReply = new YxCouponsReply();
        deleteReply.setId(id);
        deleteReply.setDelFlag(1);
        deleteReply.setUpdateUserId(uid);
        deleteReply.setUpdateTime(DateTime.now().toTimestamp());
        this.yxCouponsReplyService.updateById(deleteReply);
        return ApiResult.result(true);
    }
}

