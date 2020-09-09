/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import co.yixiang.modules.coupon.service.YxCouponOrderUseService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseQueryCriteria;
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
import java.util.Arrays;

/**
 * @author huiy
 * @date 2020-08-14
 */
@AllArgsConstructor
@Api(tags = "卡券核销表管理")
@RestController
@RequestMapping("/api/yxCouponOrderUse")
public class YxCouponOrderUseController {

    private final YxCouponOrderUseService yxCouponOrderUseService;
    private final IGenerator generator;


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponOrderUse:list')")
    public void download(HttpServletResponse response, YxCouponOrderUseQueryCriteria criteria) throws IOException {
        yxCouponOrderUseService.download(generator.convert(yxCouponOrderUseService.queryAll(criteria), YxCouponOrderUseDto.class), response);
    }

    @GetMapping
    @ApiOperation("查询卡券核销表")
    @PreAuthorize("@el.check('admin','yxCouponOrderUse:list')")
    public ResponseEntity<Object> getYxCouponOrderUses(YxCouponOrderUseQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxCouponOrderUseService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券核销表")
    @ApiOperation("新增卡券核销表")
    @PreAuthorize("@el.check('admin','yxCouponOrderUse:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponOrderUse resources) {
        return new ResponseEntity<>(yxCouponOrderUseService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券核销表")
    @ApiOperation("修改卡券核销表")
    @PreAuthorize("@el.check('admin','yxCouponOrderUse:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponOrderUse resources) {
        yxCouponOrderUseService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券核销表")
    @ApiOperation("删除卡券核销表")
    @PreAuthorize("@el.check('admin','yxCouponOrderUse:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id -> {
            yxCouponOrderUseService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("B端：查询核销记录")
    @PostMapping("/getOrderUseList")
    public ResponseEntity<Object> getOrderUseList(YxCouponOrderUseQueryCriteria criteria, Pageable pageable) {
        int uid = SecurityUtils.getUserId().intValue();
        criteria.setCreateUserId(uid);
        return new ResponseEntity<>(yxCouponOrderUseService.queryAll(criteria, pageable), HttpStatus.OK);
    }
}
