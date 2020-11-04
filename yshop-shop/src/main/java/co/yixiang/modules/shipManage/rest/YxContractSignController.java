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
import co.yixiang.modules.shipManage.domain.YxContractSign;
import co.yixiang.modules.shipManage.service.YxContractSignService;
import co.yixiang.modules.shipManage.service.dto.YxContractSignQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxContractSignDto;
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
@Api(tags = "合同签署管理")
@RestController
@RequestMapping("/api/yxContractSign")
public class YxContractSignController {

    private final YxContractSignService yxContractSignService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxContractSign:list')")
    public void download(HttpServletResponse response, YxContractSignQueryCriteria criteria) throws IOException {
        yxContractSignService.download(generator.convert(yxContractSignService.queryAll(criteria), YxContractSignDto.class), response);
    }

    @GetMapping
    @Log("查询合同签署")
    @ApiOperation("查询合同签署")
    @PreAuthorize("@el.check('admin','yxContractSign:list')")
    public ResponseEntity<Object> getYxContractSigns(YxContractSignQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxContractSignService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增合同签署")
    @ApiOperation("新增合同签署")
    @PreAuthorize("@el.check('admin','yxContractSign:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxContractSign resources){
        return new ResponseEntity<>(yxContractSignService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改合同签署")
    @ApiOperation("修改合同签署")
    @PreAuthorize("@el.check('admin','yxContractSign:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxContractSign resources){
        yxContractSignService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除合同签署")
    @ApiOperation("删除合同签署")
    @PreAuthorize("@el.check('admin','yxContractSign:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxContractSignService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
