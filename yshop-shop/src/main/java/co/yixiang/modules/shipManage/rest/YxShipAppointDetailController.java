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
import co.yixiang.modules.shipManage.domain.YxShipAppointDetail;
import co.yixiang.modules.shipManage.service.YxShipAppointDetailService;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDetailQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDetailDto;
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
@Api(tags = "船只预约详情管理")
@RestController
@RequestMapping("/api/yxShipAppointDetail")
public class YxShipAppointDetailController {

    private final YxShipAppointDetailService yxShipAppointDetailService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipAppointDetail:list')")
    public void download(HttpServletResponse response, YxShipAppointDetailQueryCriteria criteria) throws IOException {
        yxShipAppointDetailService.download(generator.convert(yxShipAppointDetailService.queryAll(criteria), YxShipAppointDetailDto.class), response);
    }

    @GetMapping
    @Log("查询船只预约详情")
    @ApiOperation("查询船只预约详情")
    @PreAuthorize("@el.check('admin','yxShipAppointDetail:list')")
    public ResponseEntity<Object> getYxShipAppointDetails(YxShipAppointDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipAppointDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只预约详情")
    @ApiOperation("新增船只预约详情")
    @PreAuthorize("@el.check('admin','yxShipAppointDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipAppointDetail resources){
        return new ResponseEntity<>(yxShipAppointDetailService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只预约详情")
    @ApiOperation("修改船只预约详情")
    @PreAuthorize("@el.check('admin','yxShipAppointDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipAppointDetail resources){
        yxShipAppointDetailService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只预约详情")
    @ApiOperation("删除船只预约详情")
    @PreAuthorize("@el.check('admin','yxShipAppointDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipAppointDetailService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
