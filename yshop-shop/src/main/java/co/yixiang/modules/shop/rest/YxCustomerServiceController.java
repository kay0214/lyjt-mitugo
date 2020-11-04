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
import co.yixiang.modules.shop.domain.YxCustomerService;
import co.yixiang.modules.shop.service.YxCustomerServiceService;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceDto;
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
@Api(tags = "机器人客服管理")
@RestController
@RequestMapping("/api/yxCustomerService")
public class YxCustomerServiceController {

    private final YxCustomerServiceService yxCustomerServiceService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCustomerService:list')")
    public void download(HttpServletResponse response, YxCustomerServiceQueryCriteria criteria) throws IOException {
        yxCustomerServiceService.download(generator.convert(yxCustomerServiceService.queryAll(criteria), YxCustomerServiceDto.class), response);
    }

    @GetMapping
    @Log("查询机器人客服")
    @ApiOperation("查询机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:list')")
    public ResponseEntity<Object> getYxCustomerServices(YxCustomerServiceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCustomerServiceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增机器人客服")
    @ApiOperation("新增机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCustomerService resources){
        return new ResponseEntity<>(yxCustomerServiceService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改机器人客服")
    @ApiOperation("修改机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCustomerService resources){
        yxCustomerServiceService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除机器人客服")
    @ApiOperation("删除机器人客服")
    @PreAuthorize("@el.check('admin','yxCustomerService:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCustomerServiceService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
