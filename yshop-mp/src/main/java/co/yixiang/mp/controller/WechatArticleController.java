/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.mp.controller;

import cn.hutool.core.date.DateUtil;
import co.yixiang.mp.domain.YxArticle;
import co.yixiang.mp.service.YxArticleService;
import co.yixiang.mp.service.dto.YxArticleDto;
import co.yixiang.mp.service.dto.YxArticleQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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

import java.util.Date;

/**
* @author hupeng
* @date 2019-10-07
*/
@Api(tags = "商城:微信图文管理")
@RestController
@RequestMapping("api")
public class WechatArticleController {

    private final YxArticleService yxArticleService;

    public WechatArticleController(YxArticleService yxArticleService) {
        this.yxArticleService = yxArticleService;
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxArticle")
    @PreAuthorize("hasAnyRole('admin','YXARTICLE_ALL','YXARTICLE_SELECT')")
    public ResponseEntity getYxArticles(YxArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxArticleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/yxArticle")
    @PreAuthorize("hasAnyRole('admin','YXARTICLE_ALL','YXARTICLE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxArticle resources){
        resources.setAddTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm"));
        return new ResponseEntity(yxArticleService.save(resources),HttpStatus.CREATED);
    }


    @ApiOperation(value = "修改")
    @PutMapping(value = "/yxArticle")
    @PreAuthorize("hasAnyRole('admin','YXARTICLE_ALL','YXARTICLE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxArticle resources){
        yxArticleService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxArticle/{id}")
    @PreAuthorize("hasAnyRole('admin','YXARTICLE_ALL','YXARTICLE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxArticleService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "发布文章")
    @GetMapping(value = "/yxArticle/publish/{id}")
    @PreAuthorize("hasAnyRole('admin','YXARTICLE_ALL','YXARTICLE_DELETE')")
    public ResponseEntity publish(@PathVariable Integer id)  throws Exception{
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        YxArticleDto yxArticleDTO= new YxArticleDto();
        YxArticle yxArticle = yxArticleService.getById(id);
        BeanUtils.copyProperties(yxArticle,yxArticleDTO);
        yxArticleService.uploadNews(yxArticleDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
