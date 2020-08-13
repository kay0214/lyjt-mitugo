/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.dto.YxStoreProductQueryCriteria;
import co.yixiang.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
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

import java.math.BigDecimal;

/**
* @author hupeng
* @date 2019-10-04
*/
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController {

    private final YxStoreProductService yxStoreProductService;

    public StoreProductController(YxStoreProductService yxStoreProductService) {
        this.yxStoreProductService = yxStoreProductService;
    }

    @Log("查询商品")
    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_SELECT')")
    public ResponseEntity getYxStoreProducts(YxStoreProductQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxStoreProductService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增商品")
    @ApiOperation(value = "新增商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreProduct resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        if(ObjectUtil.isEmpty(resources.getGiveIntegral())) resources.setGiveIntegral(BigDecimal.ZERO);
        if(ObjectUtil.isEmpty(resources.getCost())) resources.setCost(BigDecimal.ZERO);
        return new ResponseEntity(yxStoreProductService.saveProduct(resources),HttpStatus.CREATED);
    }

    @Log("修改商品")
    @ApiOperation(value = "修改商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PutMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreProduct resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.updateProduct(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "恢复数据")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/recovery/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity recovery(@PathVariable Integer id){
        yxStoreProductService.recovery(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Integer id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        yxStoreProductService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/yxStoreProduct/isFormatAttr/{id}")
    public ResponseEntity isFormatAttr(@PathVariable Integer id,@RequestBody String jsonStr){
        return new ResponseEntity(yxStoreProductService.isFormatAttr(id,jsonStr),HttpStatus.OK);
    }

    @ApiOperation(value = "设置保存属性")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct/setAttr/{id}")
    public ResponseEntity setAttr(@PathVariable Integer id,@RequestBody String jsonStr){
        yxStoreProductService.createProductAttr(id,jsonStr);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "清除属性")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct/clearAttr/{id}")
    public ResponseEntity clearAttr(@PathVariable Integer id){
        yxStoreProductService.clearProductAttr(id,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "获取属性")
    @GetMapping(value = "/yxStoreProduct/attr/{id}")
    public ResponseEntity attr(@PathVariable Integer id){
        String str = yxStoreProductService.getStoreProductAttrResult(id);
        if(StrUtil.isEmpty(str)){
            return new ResponseEntity(HttpStatus.OK);
        }
        JSONObject jsonObject = JSON.parseObject(str);

        return new ResponseEntity(jsonObject,HttpStatus.OK);
    }



}
