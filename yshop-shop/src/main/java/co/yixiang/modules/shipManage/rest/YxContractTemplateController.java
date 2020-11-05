/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.rest;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shipManage.domain.YxContractTemplate;
import co.yixiang.modules.shipManage.service.YxContractTemplateService;
import co.yixiang.modules.shipManage.service.dto.YxContractTemplateDto;
import co.yixiang.modules.shipManage.service.dto.YxContractTemplateQueryCriteria;
import co.yixiang.utils.CommonsUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.List;

/**
* @author nxl
* @date 2020-11-04
*/
@AllArgsConstructor
@Api(tags = "合同模板管理")
@RestController
@RequestMapping("/api/yxContractTemplate")
public class YxContractTemplateController {

    private final YxContractTemplateService yxContractTemplateService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxContractTemplate:list')")
    public void download(HttpServletResponse response, YxContractTemplateQueryCriteria criteria) throws IOException {
        yxContractTemplateService.download(generator.convert(yxContractTemplateService.queryAll(criteria), YxContractTemplateDto.class), response);
    }

    @GetMapping
    @Log("查询合同模板")
    @ApiOperation("查询合同模板")
    @PreAuthorize("@el.check('admin','yxContractTemplate:list')")
    public ResponseEntity<Object> getYxContractTemplates(YxContractTemplateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxContractTemplateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增合同模板")
    @ApiOperation("新增合同模板")
    @PreAuthorize("@el.check('admin','yxContractTemplate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxContractTemplate resources){
        return new ResponseEntity<>(yxContractTemplateService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改合同模板")
    @ApiOperation("修改合同模板")
    @PreAuthorize("@el.check('admin','yxContractTemplate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxContractTemplate resources){
        yxContractTemplateService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除合同模板")
    @ApiOperation("删除合同模板")
    @PreAuthorize("@el.check('admin','yxContractTemplate:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxContractTemplateService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AnonymousAccess
    @ApiOperation(value = "获取合同模板列表")
    @GetMapping(value = "/getContractTemp")
    public ResponseEntity getContractTemp() {
        QueryWrapper<YxContractTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxContractTemplate::getDelFlag, 0);
        List<YxContractTemplate> contractTemplateList =  yxContractTemplateService.list(queryWrapper);
        List<YxContractTemplateDto> shipInfoDtoList = CommonsUtils.convertBeanList(contractTemplateList, YxContractTemplateDto.class);
        return new ResponseEntity(shipInfoDtoList, HttpStatus.OK);
    }
}
