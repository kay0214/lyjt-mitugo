/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.StrUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxStoreCategory;
import co.yixiang.modules.shop.service.YxStoreCategoryService;
import co.yixiang.modules.shop.service.dto.YxStoreCategoryDto;
import co.yixiang.modules.shop.service.dto.YxStoreCategoryQueryCriteria;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author hupeng
* @date 2019-10-03
*/
@Api(tags = "商城:商品分类管理")
@RestController
@RequestMapping("api")
public class StoreCategoryController {


    private final YxStoreCategoryService yxStoreCategoryService;


    public StoreCategoryController(YxStoreCategoryService yxStoreCategoryService) {
        this.yxStoreCategoryService = yxStoreCategoryService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/yxStoreCategory/download")
    @PreAuthorize("@el.check('admin','cate:list')")
    public void download(HttpServletResponse response, YxStoreCategoryQueryCriteria criteria) throws IOException {
        yxStoreCategoryService.download(yxStoreCategoryService.queryAll(criteria), response);
    }


    @Log("查询商品分类")
    @ApiOperation(value = "查询商品分类")
    @GetMapping(value = "/yxStoreCategory")
    @PreAuthorize("hasAnyRole('admin','YXSTORECATEGORY_ALL','YXSTORECATEGORY_SELECT')")
    public ResponseEntity getYxStoreCategorys(YxStoreCategoryQueryCriteria criteria, Pageable pageable){

        List<YxStoreCategoryDto> categoryDTOList = yxStoreCategoryService.queryAll(criteria);
        return new ResponseEntity(yxStoreCategoryService.buildTree(categoryDTOList),HttpStatus.OK);
    }

    @Log("新增商品分类")
    @ApiOperation(value = "新增商品分类")
    @PostMapping(value = "/yxStoreCategory")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YXSTORECATEGORY_ALL','YXSTORECATEGORY_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreCategory resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        QueryWrapper<YxStoreCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .and(cateName -> cateName.eq(YxStoreCategory::getCateName, resources.getCateName()))
                .and(delFlag -> delFlag.eq(YxStoreCategory::getIsDel, 0));
        if(null!=resources.getPid()&&0!=resources.getPid()){
            queryWrapper.eq("pid",resources.getPid());
        }else{
            queryWrapper.eq("pid",0);
        }
        int couponsCategoryCount = yxStoreCategoryService.count(queryWrapper);
        if (couponsCategoryCount > 0){
            throw new BadRequestException("[" +resources.getCateName() + "]分类已存在!");
        }
        if(resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new BadRequestException("子分类图片必传");
        }


        boolean checkResult = yxStoreCategoryService.checkCategory(resources.getPid());

        if(!checkResult) throw new BadRequestException("分类最多能添加2级哦");


        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxStoreCategoryService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改商品分类")
    @ApiOperation(value = "修改商品分类")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PutMapping(value = "/yxStoreCategory")
    @PreAuthorize("hasAnyRole('admin','YXSTORECATEGORY_ALL','YXSTORECATEGORY_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreCategory resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        if(resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new BadRequestException("子分类图片必传");
        }

        QueryWrapper<YxStoreCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .and(delFlag -> delFlag.eq(YxStoreCategory::getIsDel, 0));

        if(null!=resources.getPid()&&0!=resources.getPid()){
            queryWrapper.eq("pid",resources.getPid());
        }else{
            queryWrapper.eq("pid",0);
        }

        YxStoreCategory storeCategory = yxStoreCategoryService.getById(resources.getId());
        if(!storeCategory.getCateName().equals(resources.getCateName())){
            queryWrapper.lambda()
                    .and(cateName -> cateName.eq(YxStoreCategory::getCateName, resources.getCateName()));
            int couponsCategoryCount = yxStoreCategoryService.count(queryWrapper);
            if (couponsCategoryCount > 0){
                throw new BadRequestException("[" +resources.getCateName() + "]分类已存在!");
            }
        }

        if(resources.getId().equals(resources.getPid())){
            throw new BadRequestException("自己不能选择自己哦");
        }

        boolean checkResult = yxStoreCategoryService.checkCategory(resources.getPid());

        if(!checkResult) throw new BadRequestException("分类最多能添加2级哦");

        yxStoreCategoryService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品分类")
    @ApiOperation(value = "删除商品分类")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yxStoreCategory/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTORECATEGORY_ALL','YXSTORECATEGORY_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        String[] ids = id.split(",");
        for (String newId: ids) {
            yxStoreCategoryService.removeById(Integer.valueOf(newId));
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
