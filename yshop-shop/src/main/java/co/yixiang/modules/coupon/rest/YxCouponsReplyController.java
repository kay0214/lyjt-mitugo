/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.YxCouponsReply;
import co.yixiang.modules.coupon.service.YxCouponsReplyService;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyDto;
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
@Api(tags = "本地生活评论管理")
@RestController
@RequestMapping("/api/yxCouponsReply")
public class YxCouponsReplyController {

    private final YxCouponsReplyService yxCouponsReplyService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponsReply:list')")
    public void download(HttpServletResponse response, YxCouponsReplyQueryCriteria criteria) throws IOException {
        yxCouponsReplyService.download(generator.convert(yxCouponsReplyService.queryAll(criteria), YxCouponsReplyDto.class), response);
    }

    @GetMapping
    @Log("查询本地生活评论")
    @ApiOperation("查询本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:list')")
    public ResponseEntity<Object> getYxCouponsReplys(YxCouponsReplyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponsReplyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增本地生活评论")
    @ApiOperation("新增本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponsReply resources){
        return new ResponseEntity<>(yxCouponsReplyService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改本地生活评论")
    @ApiOperation("修改本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponsReply resources){
        yxCouponsReplyService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除本地生活评论")
    @ApiOperation("删除本地生活评论")
    @PreAuthorize("@el.check('admin','yxCouponsReply:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxCouponsReplyService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
