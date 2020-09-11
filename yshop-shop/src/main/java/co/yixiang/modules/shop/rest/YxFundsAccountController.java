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
import co.yixiang.modules.shop.domain.YxFundsAccount;
import co.yixiang.modules.shop.service.YxFundsAccountService;
import co.yixiang.modules.shop.service.dto.YxFundsAccountQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxFundsAccountDto;
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
* @date 2020-09-11
*/
@AllArgsConstructor
@Api(tags = "平台账户表管理")
@RestController
@RequestMapping("/api/yxFundsAccount")
public class YxFundsAccountController {

    private final YxFundsAccountService yxFundsAccountService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxFundsAccount:list')")
    public void download(HttpServletResponse response, YxFundsAccountQueryCriteria criteria) throws IOException {
        yxFundsAccountService.download(generator.convert(yxFundsAccountService.queryAll(criteria), YxFundsAccountDto.class), response);
    }

    @GetMapping
    @Log("查询平台账户表")
    @ApiOperation("查询平台账户表")
    @PreAuthorize("@el.check('admin','yxFundsAccount:list')")
    public ResponseEntity<Object> getYxFundsAccounts(YxFundsAccountQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxFundsAccountService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增平台账户表")
    @ApiOperation("新增平台账户表")
    @PreAuthorize("@el.check('admin','yxFundsAccount:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxFundsAccount resources){
        return new ResponseEntity<>(yxFundsAccountService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改平台账户表")
    @ApiOperation("修改平台账户表")
    @PreAuthorize("@el.check('admin','yxFundsAccount:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxFundsAccount resources){
        yxFundsAccountService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除平台账户表")
    @ApiOperation("删除平台账户表")
    @PreAuthorize("@el.check('admin','yxFundsAccount:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxFundsAccountService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
