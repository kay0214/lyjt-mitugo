/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.rest;

import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxExpress;
import co.yixiang.modules.shop.service.YxExpressService;
import co.yixiang.modules.shop.service.dto.YxExpressQueryCriteria;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @author hupeng
* @date 2019-12-12
*/
@Api(tags = "商城:快递管理")
@RestController
@RequestMapping("api")
public class ExpressController {


    private final YxExpressService yxExpressService;

    public ExpressController(YxExpressService yxExpressService) {
        this.yxExpressService = yxExpressService;
    }

    @ApiOperation(value = "查询快递")
    @GetMapping(value = "/yxExpress")
    @PreAuthorize("hasAnyRole('admin','YXEXPRESS_ALL','YXEXPRESS_SELECT')")
    public ResponseEntity getYxExpresss(YxExpressQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxExpressService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ApiOperation(value = "新增快递")
    @PostMapping(value = "/yxExpress")
    @PreAuthorize("hasAnyRole('admin','YXEXPRESS_ALL','YXEXPRESS_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxExpress resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        return new ResponseEntity(yxExpressService.saveExpress(resources),HttpStatus.CREATED);
    }

    @Log("修改快递")
    @ApiOperation(value = "修改快递")
    @PutMapping(value = "/yxExpress")
    @PreAuthorize("hasAnyRole('admin','YXEXPRESS_ALL','YXEXPRESS_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxExpress resources){
        List<YxExpress> exit = yxExpressService.list(new QueryWrapper<YxExpress>().lambda().eq(YxExpress::getCode, resources.getCode())
                .ne(YxExpress::getId, resources.getId()));
        if (null != exit && exit.size() > 0) {
            throw new BadRequestException("快递公司编号重复");
        }
        if(null==resources.getSort()) resources.setSort(0);
        yxExpressService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除快递")
    @ApiOperation(value = "删除快递")
    @DeleteMapping(value = "/yxExpress/{id}")
    @PreAuthorize("hasAnyRole('admin','YXEXPRESS_ALL','YXEXPRESS_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxExpressService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
