/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.rest;

import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxMaterialGroup;
import co.yixiang.modules.shop.service.YxMaterialGroupService;
import co.yixiang.modules.shop.service.dto.YxMaterialGroupQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hupeng
 * @date 2020-01-09
 */
@Api(tags = "商城:素材分组管理")
@RestController
@RequestMapping("/api/materialgroup")
public class MaterialGroupController {

    private final YxMaterialGroupService yxMaterialGroupService;

    public MaterialGroupController(YxMaterialGroupService yxMaterialGroupService) {
        this.yxMaterialGroupService = yxMaterialGroupService;
    }


    @GetMapping(value = "/page")
    @ApiOperation("查询素材分组")
    public ResponseEntity<Object> getYxMaterialGroups(YxMaterialGroupQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        // 非平台管理员只能查看自己上传的图片
        if (0 != currUser.getUserRole()) {
            criteria.setCreateId(SecurityUtils.getUsername());
        }
        return new ResponseEntity<>(yxMaterialGroupService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增素材分组")
    @ApiOperation("新增素材分组")
    public ResponseEntity<Object> create(@Validated @RequestBody YxMaterialGroup resources) {
        resources.setCreateId(SecurityUtils.getUsername());
        return new ResponseEntity<>(yxMaterialGroupService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改素材分组")
    @ApiOperation("修改素材分组")
    public ResponseEntity<Object> update(@Validated @RequestBody YxMaterialGroup resources) {
        yxMaterialGroupService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除素材分组")
    @ApiOperation("删除素材分组")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAll(@PathVariable String id) {
        yxMaterialGroupService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
