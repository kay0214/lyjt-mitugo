/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shopConfig.domain.YxNotice;
import co.yixiang.modules.shopConfig.service.YxNoticeService;
import co.yixiang.modules.shopConfig.service.dto.YxNoticeQueryCriteria;
import co.yixiang.modules.shopConfig.service.dto.YxNoticeDto;
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
@Api(tags = "公告管理")
@RestController
@RequestMapping("/api/yxNotice")
public class YxNoticeController {

    private final YxNoticeService yxNoticeService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxNotice:list')")
    public void download(HttpServletResponse response, YxNoticeQueryCriteria criteria) throws IOException {
        yxNoticeService.download(generator.convert(yxNoticeService.queryAll(criteria), YxNoticeDto.class), response);
    }

    @GetMapping
    @Log("查询公告")
    @ApiOperation("查询公告")
    @PreAuthorize("@el.check('admin','yxNotice:list')")
    public ResponseEntity<Object> getYxNotices(YxNoticeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxNoticeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增公告")
    @ApiOperation("新增公告")
    @PreAuthorize("@el.check('admin','yxNotice:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxNotice resources){
        return new ResponseEntity<>(yxNoticeService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改公告")
    @ApiOperation("修改公告")
    @PreAuthorize("@el.check('admin','yxNotice:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxNotice resources){
        yxNoticeService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除公告")
    @ApiOperation("删除公告")
    @PreAuthorize("@el.check('admin','yxNotice:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxNoticeService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
