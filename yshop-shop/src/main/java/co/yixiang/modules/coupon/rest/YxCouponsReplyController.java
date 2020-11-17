/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.YxCouponsReply;
import co.yixiang.modules.coupon.domain.YxCouponsReplyRequest;
import co.yixiang.modules.coupon.service.YxCouponsReplyService;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nxl
 * @date 2020-11-04
 */
@AllArgsConstructor
@Api(tags = "本地生活评论管理")
@RestController
@RequestMapping("/api/yxCouponsReply")
public class YxCouponsReplyController {

    private final YxCouponsReplyService yxCouponsReplyService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponsReply:list')")
    public void download(HttpServletResponse response, YxCouponsReplyQueryCriteria criteria) throws IOException {
        // 处理查询角色
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        yxCouponsReplyService.download(yxCouponsReplyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询本地生活评论")
    @ApiOperation("查询本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:list')")
    public ResponseEntity<Object> getYxCouponsReplys(YxCouponsReplyQueryCriteria criteria, Pageable pageable) {
        // 处理查询角色
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }

        return new ResponseEntity<>(yxCouponsReplyService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PutMapping
    @Log("修改本地生活评论")
    @ApiOperation("修改本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponsReply resources) {
        yxCouponsReplyService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/updateMerchantReply")
    @Log("本地生活评论回复")
    @ApiOperation("本地生活评论回复")
    @PreAuthorize("@el.check('admin','yxCouponsReply:reply')")
    public ResponseEntity<Object> updateMerchantReply(@Validated @RequestBody YxCouponsReplyRequest resources) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCouponsReply yxCouponsReply = this.yxCouponsReplyService.getById(resources.getId());
        if (null == yxCouponsReply) {
            throw new BadRequestException("当前数据不存在");
        }
        YxCouponsReply updateReply = new YxCouponsReply();
        updateReply.setId(resources.getId());
        updateReply.setMerchantReplyContent(resources.getMerchantReplyContent());
        updateReply.setIsReply(1);
        updateReply.setMerchantReplyTime(DateUtils.getNowTime());
        updateReply.setUpdateUserId(loginUserId);
        yxCouponsReplyService.updateById(updateReply);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除本地生活评论")
    @ApiOperation("删除本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer id) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCouponsReply yxCouponsReply = this.yxCouponsReplyService.getById(id);
        if (null == yxCouponsReply) {
            throw new BadRequestException("当前数据不存在");
        }
        if (1 == yxCouponsReply.getDelFlag()) {
            throw new BadRequestException("当前数据已被删除");
        }
        YxCouponsReply updateReply = new YxCouponsReply();
        updateReply.setId(id);
        updateReply.setDelFlag(1);
        updateReply.setUpdateUserId(loginUserId);
        updateReply.setUpdateTime(DateTime.now().toTimestamp());
        this.yxCouponsReplyService.updateById(updateReply);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
