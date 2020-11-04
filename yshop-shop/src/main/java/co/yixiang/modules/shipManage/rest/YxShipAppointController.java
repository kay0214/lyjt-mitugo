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
import co.yixiang.modules.shipManage.domain.YxShipAppoint;
import co.yixiang.modules.shipManage.service.YxShipAppointService;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDto;
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
@Api(tags = "船只预约管理")
@RestController
@RequestMapping("/api/yxShipAppoint")
public class YxShipAppointController {

    private final YxShipAppointService yxShipAppointService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipAppoint:list')")
    public void download(HttpServletResponse response, YxShipAppointQueryCriteria criteria) throws IOException {
        yxShipAppointService.download(generator.convert(yxShipAppointService.queryAll(criteria), YxShipAppointDto.class), response);
    }

    @GetMapping
    @Log("查询船只预约")
    @ApiOperation("查询船只预约")
    @PreAuthorize("@el.check('admin','yxShipAppoint:list')")
    public ResponseEntity<Object> getYxShipAppoints(YxShipAppointQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipAppointService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只预约")
    @ApiOperation("新增船只预约")
    @PreAuthorize("@el.check('admin','yxShipAppoint:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipAppoint resources){
        return new ResponseEntity<>(yxShipAppointService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只预约")
    @ApiOperation("修改船只预约")
    @PreAuthorize("@el.check('admin','yxShipAppoint:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipAppoint resources){
        yxShipAppointService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只预约")
    @ApiOperation("删除船只预约")
    @PreAuthorize("@el.check('admin','yxShipAppoint:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipAppointService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
