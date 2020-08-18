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
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
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
@Api(tags = "卡券订单表管理")
@RestController
@RequestMapping("/api/yxCouponOrder")
public class YxCouponOrderController {

    private final YxCouponOrderService yxCouponOrderService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponOrder:list')")
    public void download(HttpServletResponse response, YxCouponOrderQueryCriteria criteria) throws IOException {
        yxCouponOrderService.download(generator.convert(yxCouponOrderService.queryAll(criteria), YxCouponOrderDto.class), response);
    }

    @GetMapping
    @Log("查询卡券订单表")
    @ApiOperation("查询卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:list')")
    public ResponseEntity<Object> getYxCouponOrders(YxCouponOrderQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券订单表")
    @ApiOperation("新增卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponOrder resources){
        return new ResponseEntity<>(yxCouponOrderService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券订单表")
    @ApiOperation("修改卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponOrder resources){
        yxCouponOrderService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券订单表")
    @ApiOperation("删除卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCouponOrderService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
