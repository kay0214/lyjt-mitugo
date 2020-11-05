/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shipManage.domain.YxShipOperationDetail;
import co.yixiang.modules.shipManage.service.YxShipOperationDetailService;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDetailQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDetailDto;
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
* @date 2020-11-05
*/
@AllArgsConstructor
@Api(tags = "运营记录详情管理")
@RestController
@RequestMapping("/api/yxShipOperationDetail")
public class YxShipOperationDetailController {

    private final YxShipOperationDetailService yxShipOperationDetailService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipOperationDetail:list')")
    public void download(HttpServletResponse response, YxShipOperationDetailQueryCriteria criteria) throws IOException {
        yxShipOperationDetailService.download(generator.convert(yxShipOperationDetailService.queryAll(criteria), YxShipOperationDetailDto.class), response);
    }

    @GetMapping
    @Log("查询运营记录详情")
    @ApiOperation("查询运营记录详情")
    @PreAuthorize("@el.check('admin','yxShipOperationDetail:list')")
    public ResponseEntity<Object> getYxShipOperationDetails(YxShipOperationDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipOperationDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增运营记录详情")
    @ApiOperation("新增运营记录详情")
    @PreAuthorize("@el.check('admin','yxShipOperationDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipOperationDetail resources){
        return new ResponseEntity<>(yxShipOperationDetailService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改运营记录详情")
    @ApiOperation("修改运营记录详情")
    @PreAuthorize("@el.check('admin','yxShipOperationDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipOperationDetail resources){
        yxShipOperationDetailService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除运营记录详情")
    @ApiOperation("删除运营记录详情")
    @PreAuthorize("@el.check('admin','yxShipOperationDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipOperationDetailService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
