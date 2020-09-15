/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.StrUtil;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.activity.service.YxUserExtractService;
import co.yixiang.modules.activity.service.dto.YxUserExtractQueryCriteria;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.modules.shop.service.YxWechatUserService;
import co.yixiang.mp.service.YxPayService;
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
 * @date 2019-11-14
 */
@Api(tags = "商城:提现管理")
@RestController
@RequestMapping("api")
public class UserExtractController {

    @Autowired
    private YxUserExtractService yxUserExtractService;

    @ApiOperation(value = "查询提现记录")
    @GetMapping(value = "/yxUserExtract")
    @PreAuthorize("hasAnyRole('admin','YXUSEREXTRACT_ALL','YXUSEREXTRACT_SELECT')")
    public ResponseEntity getYxUserExtracts(YxUserExtractQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxUserExtractService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @Log("提现审核")
    @ApiOperation(value = "提现审核")
    @PutMapping(value = "/yxUserExtract")
    @PreAuthorize("hasAnyRole('admin','YXUSEREXTRACT_ALL','YXUSEREXTRACT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxUserExtract resources) {
        if (StrUtil.isEmpty(resources.getStatus().toString())) {
            throw new BadRequestException("请选择审核状态");
        }
        if (null == resources.getId()) {
            throw new BadRequestException("缺少主键id");
        }
        if (resources.getStatus() != -1 && resources.getStatus() != 1) {
            throw new BadRequestException("请选择审核状态");
        }
        boolean result = this.yxUserExtractService.updateExtractStatus(resources);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
