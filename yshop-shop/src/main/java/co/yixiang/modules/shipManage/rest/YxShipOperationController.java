/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shipManage.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shipManage.domain.YxShipOperation;
import co.yixiang.modules.shipManage.service.YxShipOperationService;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDto;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationQueryCriteria;
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
 * @author nxl
 * @date 2020-11-05
 */
@AllArgsConstructor
@Api(tags = "船只运营记录管理")
@RestController
@RequestMapping("/api/yxShipOperation")
public class YxShipOperationController {

    private final YxShipOperationService yxShipOperationService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipOperation:list')")
    public void download(HttpServletResponse response, YxShipOperationQueryCriteria criteria) throws IOException {
        yxShipOperationService.download(generator.convert(yxShipOperationService.queryAll(criteria), YxShipOperationDto.class), response);
    }

    @GetMapping
    @Log("查询船只运营记录")
    @ApiOperation("查询船只运营记录")
    @PreAuthorize("@el.check('admin','yxShipOperation:list')")
    public ResponseEntity<Object> getYxShipOperations(YxShipOperationQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxShipOperationService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只运营记录")
    @ApiOperation("新增船只运营记录")
    @PreAuthorize("@el.check('admin','yxShipOperation:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipOperation resources) {
        return new ResponseEntity<>(yxShipOperationService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只运营记录")
    @ApiOperation("修改船只运营记录")
    @PreAuthorize("@el.check('admin','yxShipOperation:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipOperation resources) {
        yxShipOperationService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只运营记录")
    @ApiOperation("删除船只运营记录")
    @PreAuthorize("@el.check('admin','yxShipOperation:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id -> {
            yxShipOperationService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("海安支队大屏")
    @ApiOperation("海安支队大屏")
    @PreAuthorize("@el.check('admin','yxShipOperation:list')")
    @PostMapping(value = "/hazdShowList")
    public ResponseEntity<Object> hazdShowList(YxShipOperationQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxShipOperationService.findOperationList(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/getOperationDetailInfo/{id}")
    @Log("获取运营记录详情")
    @ApiOperation("获取运营记录详情")
    @PreAuthorize("@el.check('admin','yxShipOperation:list')")
    public ResponseEntity<Object> getOperationDetailInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(yxShipOperationService.getOperationDetailInfo(id), HttpStatus.OK);

    }

}
