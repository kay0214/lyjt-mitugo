/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.service.YxMerchantsDetailService;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailDto;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailQueryCriteria;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author liusy
* @date 2020-08-19
*/
@AllArgsConstructor
@Api(tags = "商户详情表管理")
@RestController
@RequestMapping("/api/yxMerchantsDetail")
public class YxMerchantsDetailController {

    @Autowired
    private YxMerchantsDetailService yxMerchantsDetailService;
    private final IGenerator generator;


    @GetMapping("/getYxMerchantsDetailsList")
    @Log("查询商户详情表")
    @ApiOperation("查询商户详情表")
    @PreAuthorize("@el.check('admin','yxMerchantsDetail:list')")
    public ResponseEntity<Object> getYxMerchantsDetailsList(YxMerchantsDetailQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxMerchantsDetailService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/getYxMerchantsDetailsInfo/{id}")
    @Log("查询商户详情信息")
    @ApiOperation("查询商户详情信息")
    @PreAuthorize("@el.check('admin','yxMerchantsDetail:info')")
    public ResponseEntity<Object> getYxMerchantsDetailsInfo(@PathVariable Integer id) {
        YxMerchantsDetailDto result = yxMerchantsDetailService.queryById(id);
        if (null == result) {
            return new ResponseEntity<>(new YxMerchantsDetailDto(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Log("新增商户详情表")
    @ApiOperation("新增商户详情")
    @PreAuthorize("@el.check('admin','yxMerchantsDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxMerchantsDetailDto resources) {
        // 获取登陆用户的id
        int uid = SecurityUtils.getUserId().intValue();
        resources.setUid(uid);
        boolean result = yxMerchantsDetailService.createOrUpdate(resources);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(value = "/examine")
    @Log("审核接口")
    @ApiOperation("审核接口")
    @PreAuthorize("@el.check('admin','yxMerchantsDetail:examine')")
    public ResponseEntity<Object> updateExamineStatus(@Validated @RequestBody YxMerchantsDetailDto resources) {

        // 获取登陆用户的id
        int uid = SecurityUtils.getUserId().intValue();
        resources.setUid(uid);
        boolean result = yxMerchantsDetailService.updateExamineStatus(resources);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
