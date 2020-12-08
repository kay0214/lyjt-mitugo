/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.rest;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxCustomizeRate;
import co.yixiang.modules.shop.service.YxCustomizeRateService;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateDto;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateQueryCriteria;
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
@Api(tags = "商品&卡券费率配置管理")
@RestController
@RequestMapping("/api/yxCustomizeRate")
public class YxCustomizeRateController {

    private final YxCustomizeRateService yxCustomizeRateService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCustomizeRate:list')")
    public void download(HttpServletResponse response, YxCustomizeRateQueryCriteria criteria) throws IOException {
        yxCustomizeRateService.download(generator.convert(yxCustomizeRateService.queryAll(criteria), YxCustomizeRateDto.class), response);
    }

    @GetMapping
    @Log("查询商品&卡券费率配置")
    @ApiOperation("查询商品&卡券费率配置")
    @PreAuthorize("@el.check('admin','yxCustomizeRate:list')")
    public ResponseEntity<Object> getYxCustomizeRates(YxCustomizeRateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCustomizeRateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增商品&卡券费率配置")
    @ApiOperation("新增商品&卡券费率配置")
    @PreAuthorize("@el.check('admin','yxCustomizeRate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCustomizeRate resources){
        return new ResponseEntity<>(yxCustomizeRateService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改商品&卡券费率配置")
    @ApiOperation("修改商品&卡券费率配置")
    @PreAuthorize("@el.check('admin','yxCustomizeRate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCustomizeRate resources){
        yxCustomizeRateService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品&卡券费率配置")
    @ApiOperation("删除商品&卡券费率配置")
    @PreAuthorize("@el.check('admin','yxCustomizeRate:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCustomizeRateService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
