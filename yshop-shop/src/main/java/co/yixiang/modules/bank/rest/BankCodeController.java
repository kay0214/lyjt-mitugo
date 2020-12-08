/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.bank.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.bank.domain.BankCode;
import co.yixiang.modules.bank.service.BankCodeService;
import co.yixiang.modules.bank.service.dto.BankCodeQueryCriteria;
import co.yixiang.modules.bank.service.dto.BankCodeDto;
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
* @author lsy
* @date 2020-11-20
*/
@AllArgsConstructor
@Api(tags = "联行号配置管理")
@RestController
@RequestMapping("/api/bankCode")
public class BankCodeController {

    private final BankCodeService bankCodeService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','bankCode:list')")
    public void download(HttpServletResponse response, BankCodeQueryCriteria criteria) throws IOException {
        bankCodeService.download(generator.convert(bankCodeService.queryAll(criteria), BankCodeDto.class), response);
    }

    @GetMapping
    @Log("查询联行号配置")
    @ApiOperation("查询联行号配置")
    @PreAuthorize("@el.check('admin','bankCode:list')")
    public ResponseEntity<Object> getBankCodes(BankCodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bankCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增联行号配置")
    @ApiOperation("新增联行号配置")
    @PreAuthorize("@el.check('admin','bankCode:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody BankCode resources){
        return new ResponseEntity<>(bankCodeService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改联行号配置")
    @ApiOperation("修改联行号配置")
    @PreAuthorize("@el.check('admin','bankCode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody BankCode resources){
        bankCodeService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除联行号配置")
    @ApiOperation("删除联行号配置")
    @PreAuthorize("@el.check('admin','bankCode:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            bankCodeService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
