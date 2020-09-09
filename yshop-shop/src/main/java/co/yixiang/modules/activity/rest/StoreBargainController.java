/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxStoreBargain;
import co.yixiang.modules.activity.service.YxStoreBargainService;
import co.yixiang.modules.activity.service.dto.YxStoreBargainQueryCriteria;
import co.yixiang.utils.OrderUtil;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author hupeng
* @date 2019-12-22
*/
@Api(tags = "商城:砍价管理")
@RestController
@RequestMapping("api")
public class StoreBargainController {

    private final YxStoreBargainService yxStoreBargainService;

    public StoreBargainController(YxStoreBargainService yxStoreBargainService) {
        this.yxStoreBargainService = yxStoreBargainService;
    }

    @ApiOperation(value = "查询砍价")
    @GetMapping(value = "/yxStoreBargain")
    @PreAuthorize("hasAnyRole('admin','YXSTOREBARGAIN_ALL','YXSTOREBARGAIN_SELECT')")
    public ResponseEntity getYxStoreBargains(YxStoreBargainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxStoreBargainService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改砍价")
    @ApiOperation(value = "修改砍价")
    @PutMapping(value = "/yxStoreBargain")
    @PreAuthorize("hasAnyRole('admin','YXSTOREBARGAIN_ALL','YXSTOREBARGAIN_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreBargain resources){

        if(ObjectUtil.isNotNull(resources.getStartTimeDate())){
            resources.setStartTime(OrderUtil.
                    dateToTimestamp(resources.getStartTimeDate()));
        }
        if(ObjectUtil.isNotNull(resources.getEndTimeDate())){
            resources.setStopTime(OrderUtil.
                    dateToTimestamp(resources.getEndTimeDate()));
        }
        if(ObjectUtil.isNull(resources.getId())){
            resources.setAddTime(OrderUtil.getSecondTimestampTwo());
            return new ResponseEntity(yxStoreBargainService.save(resources),HttpStatus.CREATED);
        }else{
            yxStoreBargainService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @Log("删除砍价")
    @ApiOperation(value = "删除砍价")
    @DeleteMapping(value = "/yxStoreBargain/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREBARGAIN_ALL','YXSTOREBARGAIN_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreBargainService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
