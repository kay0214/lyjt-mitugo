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
import co.yixiang.modules.shop.domain.YxNowRate;
import co.yixiang.modules.shop.service.YxNowRateService;
import co.yixiang.modules.shop.service.dto.YxNowRateQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxNowRateDto;
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
@Api(tags = "购买时费率管理")
@RestController
@RequestMapping("/api/yxNowRate")
public class YxNowRateController {

    private final YxNowRateService yxNowRateService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxNowRate:list')")
    public void download(HttpServletResponse response, YxNowRateQueryCriteria criteria) throws IOException {
        yxNowRateService.download(generator.convert(yxNowRateService.queryAll(criteria), YxNowRateDto.class), response);
    }

    @GetMapping
    @Log("查询购买时费率")
    @ApiOperation("查询购买时费率")
    @PreAuthorize("@el.check('admin','yxNowRate:list')")
    public ResponseEntity<Object> getYxNowRates(YxNowRateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxNowRateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增购买时费率")
    @ApiOperation("新增购买时费率")
    @PreAuthorize("@el.check('admin','yxNowRate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxNowRate resources){
        return new ResponseEntity<>(yxNowRateService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改购买时费率")
    @ApiOperation("修改购买时费率")
    @PreAuthorize("@el.check('admin','yxNowRate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxNowRate resources){
        yxNowRateService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除购买时费率")
    @ApiOperation("删除购买时费率")
    @PreAuthorize("@el.check('admin','yxNowRate:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxNowRateService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
