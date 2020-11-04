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
import co.yixiang.modules.shipManage.domain.YxShipPassenger;
import co.yixiang.modules.shipManage.service.YxShipPassengerService;
import co.yixiang.modules.shipManage.service.dto.YxShipPassengerQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipPassengerDto;
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
@Api(tags = "船只乘客管理")
@RestController
@RequestMapping("/api/yxShipPassenger")
public class YxShipPassengerController {

    private final YxShipPassengerService yxShipPassengerService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipPassenger:list')")
    public void download(HttpServletResponse response, YxShipPassengerQueryCriteria criteria) throws IOException {
        yxShipPassengerService.download(generator.convert(yxShipPassengerService.queryAll(criteria), YxShipPassengerDto.class), response);
    }

    @GetMapping
    @Log("查询船只乘客")
    @ApiOperation("查询船只乘客")
    @PreAuthorize("@el.check('admin','yxShipPassenger:list')")
    public ResponseEntity<Object> getYxShipPassengers(YxShipPassengerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipPassengerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只乘客")
    @ApiOperation("新增船只乘客")
    @PreAuthorize("@el.check('admin','yxShipPassenger:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipPassenger resources){
        return new ResponseEntity<>(yxShipPassengerService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只乘客")
    @ApiOperation("修改船只乘客")
    @PreAuthorize("@el.check('admin','yxShipPassenger:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipPassenger resources){
        yxShipPassengerService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只乘客")
    @ApiOperation("删除船只乘客")
    @PreAuthorize("@el.check('admin','yxShipPassenger:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipPassengerService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
