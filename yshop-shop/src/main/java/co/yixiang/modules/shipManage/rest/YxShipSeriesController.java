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
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.service.YxShipSeriesService;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesDto;
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
@Api(tags = "船只系列管理")
@RestController
@RequestMapping("/api/yxShipSeries")
public class YxShipSeriesController {

    private final YxShipSeriesService yxShipSeriesService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipSeries:list')")
    public void download(HttpServletResponse response, YxShipSeriesQueryCriteria criteria) throws IOException {
        yxShipSeriesService.download(generator.convert(yxShipSeriesService.queryAll(criteria), YxShipSeriesDto.class), response);
    }

    @GetMapping
    @Log("查询船只系列")
    @ApiOperation("查询船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:list')")
    public ResponseEntity<Object> getYxShipSeriess(YxShipSeriesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipSeriesService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只系列")
    @ApiOperation("新增船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipSeries resources){
        return new ResponseEntity<>(yxShipSeriesService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只系列")
    @ApiOperation("修改船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipSeries resources){
        yxShipSeriesService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只系列")
    @ApiOperation("删除船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipSeriesService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
