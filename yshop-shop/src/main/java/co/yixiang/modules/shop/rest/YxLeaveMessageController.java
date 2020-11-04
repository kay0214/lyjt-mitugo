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
import co.yixiang.modules.shop.domain.YxLeaveMessage;
import co.yixiang.modules.shop.service.YxLeaveMessageService;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageDto;
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
@Api(tags = "留言管理管理")
@RestController
@RequestMapping("/api/yxLeaveMessage")
public class YxLeaveMessageController {

    private final YxLeaveMessageService yxLeaveMessageService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxLeaveMessage:list')")
    public void download(HttpServletResponse response, YxLeaveMessageQueryCriteria criteria) throws IOException {
        yxLeaveMessageService.download(generator.convert(yxLeaveMessageService.queryAll(criteria), YxLeaveMessageDto.class), response);
    }

    @GetMapping
    @Log("查询留言管理")
    @ApiOperation("查询留言管理")
    @PreAuthorize("@el.check('admin','yxLeaveMessage:list')")
    public ResponseEntity<Object> getYxLeaveMessages(YxLeaveMessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxLeaveMessageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增留言管理")
    @ApiOperation("新增留言管理")
    @PreAuthorize("@el.check('admin','yxLeaveMessage:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxLeaveMessage resources){
        return new ResponseEntity<>(yxLeaveMessageService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改留言管理")
    @ApiOperation("修改留言管理")
    @PreAuthorize("@el.check('admin','yxLeaveMessage:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxLeaveMessage resources){
        yxLeaveMessageService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除留言管理")
    @ApiOperation("删除留言管理")
    @PreAuthorize("@el.check('admin','yxLeaveMessage:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxLeaveMessageService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
