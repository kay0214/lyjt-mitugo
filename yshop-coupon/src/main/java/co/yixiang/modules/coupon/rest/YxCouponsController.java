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
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
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
@Api(tags = "卡券表管理")
@RestController
@RequestMapping("/api/yxCoupons")
public class YxCouponsController {

    private final YxCouponsService yxCouponsService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCoupons:list')")
    public void download(HttpServletResponse response, YxCouponsQueryCriteria criteria) throws IOException {
        yxCouponsService.download(generator.convert(yxCouponsService.queryAll(criteria), YxCouponsDto.class), response);
    }

    @GetMapping
    @Log("查询卡券表")
    @ApiOperation("查询卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:list')")
    public ResponseEntity<Object> getYxCouponss(YxCouponsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券表")
    @ApiOperation("新增卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCoupons resources){
        return new ResponseEntity<>(yxCouponsService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券表")
    @ApiOperation("修改卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCoupons resources){
        yxCouponsService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券表")
    @ApiOperation("删除卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCouponsService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
