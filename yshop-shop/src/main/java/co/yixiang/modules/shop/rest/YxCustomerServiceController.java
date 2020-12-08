/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxCustomerService;
import co.yixiang.modules.shop.service.YxCustomerServiceService;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceDto;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceQueryCriteria;
import co.yixiang.utils.CurrUser;
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
@Api(tags = "机器人客服管理")
@RestController
@RequestMapping("/api/yxCustomerService")
public class YxCustomerServiceController {

    private final YxCustomerServiceService yxCustomerServiceService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCustomerService:list')")
    public void download(HttpServletResponse response, YxCustomerServiceQueryCriteria criteria) throws IOException {
        yxCustomerServiceService.download(generator.convert(yxCustomerServiceService.queryAll(criteria), YxCustomerServiceDto.class), response);
    }

    @GetMapping
    @Log("查询机器人客服")
    @ApiOperation("查询机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:list')")
    public ResponseEntity<Object> getYxCustomerServices(YxCustomerServiceQueryCriteria criteria, Pageable pageable) {
        // 处理查询角色
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }

        return new ResponseEntity<>(yxCustomerServiceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增机器人客服")
    @ApiOperation("新增机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCustomerService resources) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        CurrUser currUser = SecurityUtils.getCurrUser();
        if (0 != currUser.getUserRole()) {
            resources.setMerId(loginUserId);
        }
        resources.setUserRole(currUser.getUserRole());
        resources.setDelFlag(0);
        resources.setCreateUserId(loginUserId);
        resources.setCreateTime(DateTime.now().toTimestamp());
        return new ResponseEntity<>(yxCustomerServiceService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改机器人客服")
    @ApiOperation("修改机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCustomerService resources) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCustomerService yxCustomerService = this.yxCustomerServiceService.getById(resources.getId());
        if (null == yxCustomerService || 1 == yxCustomerService.getDelFlag()) {
            throw new BadRequestException("当前数据不存在");
        }
        YxCustomerService update = new YxCustomerService();
        update.setId(resources.getId());
        update.setQuestion(resources.getQuestion());
        update.setAnswer(resources.getAnswer());
        update.setSort(resources.getSort());
        update.setStatus(resources.getStatus());
        update.setUpdateUserId(loginUserId);
        update.setUpdateTime(DateTime.now().toTimestamp());
        yxCustomerServiceService.updateById(update);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/updateStatus")
    @Log("机器人客服启用/禁用")
    @ApiOperation("机器人客服启用/禁用")
    @PreAuthorize("@el.check('admin','yxCustomerService:switch')")
    public ResponseEntity<Object> updateStatus(@Validated @RequestBody YxCustomerService resources) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCustomerService yxCustomerService = this.yxCustomerServiceService.getById(resources.getId());
        if (null == yxCustomerService || 1 == yxCustomerService.getDelFlag()) {
            throw new BadRequestException("当前数据不存在");
        }
        YxCustomerService update = new YxCustomerService();
        update.setId(resources.getId());
        update.setStatus(resources.getStatus());
        update.setUpdateUserId(loginUserId);
        update.setUpdateTime(DateTime.now().toTimestamp());
        yxCustomerServiceService.updateById(update);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除机器人客服")
    @ApiOperation("删除机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer id) {
        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCustomerService yxCustomerService = this.yxCustomerServiceService.getById(id);
        if (null == yxCustomerService || 1 == yxCustomerService.getDelFlag()) {
            throw new BadRequestException("当前数据不存在或已被删除");
        }
        YxCustomerService delete = new YxCustomerService();
        delete.setId(id);
        delete.setDelFlag(1);
        delete.setUpdateUserId(loginUserId);
        delete.setUpdateTime(DateTime.now().toTimestamp());
        this.yxCustomerServiceService.updateById(delete);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
