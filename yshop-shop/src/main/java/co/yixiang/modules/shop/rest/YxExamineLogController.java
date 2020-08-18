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
import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.dto.YxExamineLogQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxExamineLogDto;
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
* @author liusy
* @date 2020-08-17
*/
@AllArgsConstructor
@Api(tags = "审核记录管理")
@RestController
@RequestMapping("/api/yxExamineLog")
public class YxExamineLogController {

    private final YxExamineLogService yxExamineLogService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxExamineLog:list')")
    public void download(HttpServletResponse response, YxExamineLogQueryCriteria criteria) throws IOException {
        yxExamineLogService.download(generator.convert(yxExamineLogService.queryAll(criteria), YxExamineLogDto.class), response);
    }

    @GetMapping
    @Log("查询审核记录")
    @ApiOperation("查询审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:list')")
    public ResponseEntity<Object> getYxExamineLogs(YxExamineLogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxExamineLogService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增审核记录")
    @ApiOperation("新增审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxExamineLog resources){
        return new ResponseEntity<>(yxExamineLogService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改审核记录")
    @ApiOperation("修改审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxExamineLog resources){
        yxExamineLogService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除审核记录")
    @ApiOperation("删除审核记录")
    @PreAuthorize("@el.check('admin','yxExamineLog:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxExamineLogService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
