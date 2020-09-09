/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.activity.service.YxStoreCouponIssueService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueQueryCriteria;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hupeng
 * @date 2019-11-09
 */
@Api(tags = "商城:优惠券发布管理")
@RestController
@RequestMapping("api")
public class StoreCouponIssueController {

    @Autowired
    private YxStoreCouponIssueService yxStoreCouponIssueService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @ApiOperation(value = "查询已发布")
    @GetMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_SELECT')")
    public ResponseEntity getYxStoreCouponIssues(YxStoreCouponIssueQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
            criteria.setChildStoreId(this.yxStoreInfoService.getStoreIdByMerId(currUser.getChildUser()));
        }
        criteria.setIsDel(0);
        return new ResponseEntity(yxStoreCouponIssueService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("发布优惠券")
    @ApiOperation(value = "发布")
    @PostMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreCouponIssue resources) {
        int uid = SecurityUtils.getUserId().intValue();
        if (ObjectUtil.isNotNull(resources.getStartTimeDate())) {
            resources.setStartTime(OrderUtil.dateToTimestamp(resources.getStartTimeDate()));
        }
        if (ObjectUtil.isNotNull(resources.getEndTimeDate())) {
            resources.setEndTime(OrderUtil.dateToTimestamp(resources.getEndTimeDate()));
        }
        if (resources.getTotalCount() ==null) {
            resources.setTotalCount(0);
        }
        if (resources.getTotalCount() > 0) {
            resources.setRemainCount(resources.getTotalCount());
        }
        YxStoreInfo findStoreInfo = yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", uid).eq("del_flag", 0));
        if (findStoreInfo == null) {
            throw new BadRequestException("当前商户未绑定商铺!");
        }
        resources.setStoreId(findStoreInfo.getId());

        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxStoreCouponIssueService.save(resources), HttpStatus.CREATED);
    }

    @Log("修改状态")
    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreCouponIssue resources) {
        yxStoreCouponIssueService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxStoreCouponIssue/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        YxStoreCouponIssue resources = new YxStoreCouponIssue();
        resources.setId(id);
        resources.setIsDel(1);
        yxStoreCouponIssueService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.OK);
    }
}
