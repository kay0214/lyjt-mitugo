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
import co.yixiang.modules.shipManage.domain.YxUsedContacts;
import co.yixiang.modules.shipManage.service.YxUsedContactsService;
import co.yixiang.modules.shipManage.service.dto.YxUsedContactsQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxUsedContactsDto;
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
@Api(tags = "常用联系人管理")
@RestController
@RequestMapping("/api/yxUsedContacts")
public class YxUsedContactsController {

    private final YxUsedContactsService yxUsedContactsService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxUsedContacts:list')")
    public void download(HttpServletResponse response, YxUsedContactsQueryCriteria criteria) throws IOException {
        yxUsedContactsService.download(generator.convert(yxUsedContactsService.queryAll(criteria), YxUsedContactsDto.class), response);
    }

    @GetMapping
    @Log("查询常用联系人")
    @ApiOperation("查询常用联系人")
    @PreAuthorize("@el.check('admin','yxUsedContacts:list')")
    public ResponseEntity<Object> getYxUsedContactss(YxUsedContactsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxUsedContactsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增常用联系人")
    @ApiOperation("新增常用联系人")
    @PreAuthorize("@el.check('admin','yxUsedContacts:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxUsedContacts resources){
        return new ResponseEntity<>(yxUsedContactsService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改常用联系人")
    @ApiOperation("修改常用联系人")
    @PreAuthorize("@el.check('admin','yxUsedContacts:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxUsedContacts resources){
        yxUsedContactsService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除常用联系人")
    @ApiOperation("删除常用联系人")
    @PreAuthorize("@el.check('admin','yxUsedContacts:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxUsedContactsService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
