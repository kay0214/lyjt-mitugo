/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.rest;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shopConfig.domain.YxHotConfig;
import co.yixiang.modules.shopConfig.service.YxHotConfigService;
import co.yixiang.modules.shopConfig.service.dto.YxHotConfigDto;
import co.yixiang.modules.shopConfig.service.dto.YxHotConfigQueryCriteria;
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
* @date 2020-11-04
*/
@AllArgsConstructor
@Api(tags = "hot配置管理")
@RestController
@RequestMapping("/api/yxHotConfig")
public class YxHotConfigController {

    private final YxHotConfigService yxHotConfigService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxHotConfig:list')")
    public void download(HttpServletResponse response, YxHotConfigQueryCriteria criteria) throws IOException {
        yxHotConfigService.download(generator.convert(yxHotConfigService.queryAll(criteria), YxHotConfigDto.class), response);
    }

    @GetMapping
    @Log("查询hot配置")
    @ApiOperation("查询hot配置")
    @PreAuthorize("@el.check('admin','yxHotConfig:list')")
    public ResponseEntity<Object> getYxHotConfigs(YxHotConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxHotConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增hot配置")
    @ApiOperation("新增hot配置")
    @PreAuthorize("@el.check('admin','yxHotConfig:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxHotConfig resources){
        return new ResponseEntity<>(yxHotConfigService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改hot配置")
    @ApiOperation("修改hot配置")
    @PreAuthorize("@el.check('admin','yxHotConfig:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxHotConfig resources){
        yxHotConfigService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除hot配置")
    @ApiOperation("删除hot配置")
    @PreAuthorize("@el.check('admin','yxHotConfig:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxHotConfigService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
