/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxRefundReason;
import co.yixiang.modules.shop.service.YxRefundReasonService;
import co.yixiang.modules.shop.service.dto.YxRefundReasonQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxRefundReasonDto;
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
* @author nxl
* @date 2020-11-04
*/
@AllArgsConstructor
@Api(tags = "退款理由管理")
@RestController
@RequestMapping("/api/yxRefundReason")
public class YxRefundReasonController {

    private final YxRefundReasonService yxRefundReasonService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxRefundReason:list')")
    public void download(HttpServletResponse response, YxRefundReasonQueryCriteria criteria) throws IOException {
        yxRefundReasonService.download(generator.convert(yxRefundReasonService.queryAll(criteria), YxRefundReasonDto.class), response);
    }

    @GetMapping
    @Log("查询退款理由")
    @ApiOperation("查询退款理由")
    @PreAuthorize("@el.check('admin','yxRefundReason:list')")
    public ResponseEntity<Object> getYxRefundReasons(YxRefundReasonQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxRefundReasonService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增退款理由")
    @ApiOperation("新增退款理由")
    @PreAuthorize("@el.check('admin','yxRefundReason:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxRefundReason resources){
        return new ResponseEntity<>(yxRefundReasonService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改退款理由")
    @ApiOperation("修改退款理由")
    @PreAuthorize("@el.check('admin','yxRefundReason:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxRefundReason resources){
        yxRefundReasonService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除退款理由")
    @ApiOperation("删除退款理由")
    @PreAuthorize("@el.check('admin','yxRefundReason:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxRefundReasonService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
