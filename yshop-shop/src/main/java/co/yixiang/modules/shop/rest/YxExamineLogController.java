/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.dto.YxExamineLogDto;
import co.yixiang.modules.shop.service.dto.YxExamineLogQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liusy
 * @date 2020-08-19
 */
@AllArgsConstructor
@Api(tags = "审核记录管理")
@RestController
@RequestMapping("/api/yxExamineLog")
public class YxExamineLogController {

    @Autowired
    private YxExamineLogService yxExamineLogService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxExamineLog:list')")
    public void download(HttpServletResponse response, YxExamineLogQueryCriteria criteria) throws IOException {
        yxExamineLogService.download(generator.convert(yxExamineLogService.queryAll(criteria), YxExamineLogDto.class), response);
    }

    @GetMapping
    @Log("查询审核记录")
    @ApiOperation("查询审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:list')")
    public ResponseEntity<Object> getYxExamineLogs(YxExamineLogQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        criteria.setType(2);
        return new ResponseEntity<>(yxExamineLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/extract")
    @Log("查询审核记录")
    @ApiOperation("查询审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:list')")
    public ResponseEntity<Object> getExtractYxExamineLogs(YxExamineLogQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        criteria.setType(1);
        return new ResponseEntity<>(yxExamineLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }
}
