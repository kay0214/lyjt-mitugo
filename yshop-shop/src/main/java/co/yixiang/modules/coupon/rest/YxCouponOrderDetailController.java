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
import co.yixiang.modules.coupon.domain.YxCouponOrderDetail;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDetailQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDetailDto;
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
* @author liusy
* @date 2020-08-26
*/
@AllArgsConstructor
@Api(tags = "卡券订单详情表管理")
@RestController
@RequestMapping("/api/yxCouponOrderDetail")
public class YxCouponOrderDetailController {

    private final YxCouponOrderDetailService yxCouponOrderDetailService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponOrderDetail:list')")
    public void download(HttpServletResponse response, YxCouponOrderDetailQueryCriteria criteria) throws IOException {
        yxCouponOrderDetailService.download(generator.convert(yxCouponOrderDetailService.queryAll(criteria), YxCouponOrderDetailDto.class), response);
    }

    @GetMapping
    @Log("查询卡券订单详情表")
    @ApiOperation("查询卡券订单详情表")
    @PreAuthorize("@el.check('admin','yxCouponOrderDetail:list')")
    public ResponseEntity<Object> getYxCouponOrderDetails(YxCouponOrderDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponOrderDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券订单详情表")
    @ApiOperation("新增卡券订单详情表")
    @PreAuthorize("@el.check('admin','yxCouponOrderDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponOrderDetail resources){
        return new ResponseEntity<>(yxCouponOrderDetailService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券订单详情表")
    @ApiOperation("修改卡券订单详情表")
    @PreAuthorize("@el.check('admin','yxCouponOrderDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponOrderDetail resources){
        yxCouponOrderDetailService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券订单详情表")
    @ApiOperation("删除卡券订单详情表")
    @PreAuthorize("@el.check('admin','yxCouponOrderDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCouponOrderDetailService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
