/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.rest;
import java.util.Arrays;

import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryDto;
import co.yixiang.logging.aop.log.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huiy
 * @date 2020-08-14
 */
@AllArgsConstructor
@Api(tags = "卡券分类表管理")
@RestController
@RequestMapping("/api/yxCouponsCategory")
public class YxCouponsCategoryController {

    private final YxCouponsCategoryService yxCouponsCategoryService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponsCategory:list')")
    public void download(HttpServletResponse response, YxCouponsCategoryQueryCriteria criteria) throws IOException {
        yxCouponsCategoryService.download(generator.convert(yxCouponsCategoryService.queryAll(criteria), YxCouponsCategoryDto.class), response);
    }

    @GetMapping
    @Log("查询卡券分类表")
    @ApiOperation("查询卡券分类表")
    @PreAuthorize("@el.check('admin','yxCouponsCategory:list')")
    public ResponseEntity<Object> getYxCouponsCategorys(YxCouponsCategoryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponsCategoryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券分类表")
    @ApiOperation("新增卡券分类表")
    @PreAuthorize("@el.check('admin','yxCouponsCategory:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponsCategory resources){
        return new ResponseEntity<>(yxCouponsCategoryService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券分类表")
    @ApiOperation("修改卡券分类表")
    @PreAuthorize("@el.check('admin','yxCouponsCategory:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponsCategory resources){
        yxCouponsCategoryService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券分类表")
    @ApiOperation("删除卡券分类表")
    @PreAuthorize("@el.check('admin','yxCouponsCategory:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCouponsCategoryService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
