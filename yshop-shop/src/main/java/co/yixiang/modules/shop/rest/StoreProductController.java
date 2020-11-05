/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.domain.YxStoreProductChange;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.dto.YxStoreProductDto;
import co.yixiang.modules.shop.service.dto.YxStoreProductQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author hupeng
 * @date 2019-10-04
 */
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController {

    @Autowired
    private YxStoreProductService yxStoreProductService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    public StoreProductController(YxStoreProductService yxStoreProductService) {
        this.yxStoreProductService = yxStoreProductService;
    }

    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_SELECT')")
    public ResponseEntity getYxStoreProducts(YxStoreProductQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        return new ResponseEntity(yxStoreProductService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增商品")
    @ApiOperation(value = "新增商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreProductDto resources) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        //
        int sysUserId = SecurityUtils.getUserId().intValue();
        YxStoreInfo store = yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", sysUserId));
        if (ObjectUtil.isNull(store)) {
            throw new BadRequestException("商户id：" + sysUserId + "，未找到对应店铺信息！");
        }
        resources.setStoreId(store.getId());
        resources.setMerId(store.getMerId());
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        if (ObjectUtil.isEmpty(resources.getGiveIntegral())) resources.setGiveIntegral(BigDecimal.ZERO);
        if (ObjectUtil.isEmpty(resources.getCost())) resources.setCost(BigDecimal.ZERO);
        resources.setCommission(resources.getPrice().subtract(resources.getSettlement()));
        // 自定义分佣比例校验总分成是否是100
        if (2 == resources.getCustomizeType()) {
            BigDecimal fundsRate = resources.getYxCustomizeRate().getFundsRate();
            BigDecimal shareRate = resources.getYxCustomizeRate().getShareRate();
            BigDecimal shareParentRate = resources.getYxCustomizeRate().getShareParentRate();
            BigDecimal parentRate = resources.getYxCustomizeRate().getParentRate();
            BigDecimal partnerRate = resources.getYxCustomizeRate().getPartnerRate();
            BigDecimal referenceRate = resources.getYxCustomizeRate().getReferenceRate();
            BigDecimal merRate = resources.getYxCustomizeRate().getMerRate();
            // 分佣总和应等于100
            BigDecimal count = fundsRate.add(shareRate).add(shareParentRate).add(parentRate).add(partnerRate).add(referenceRate).add(merRate);
            // 分佣比例总和应等于100
            if (count.compareTo(new BigDecimal("100")) != 0) {
                throw new BadRequestException("分佣比例配置不正确!");
            }
        }
        return new ResponseEntity(yxStoreProductService.saveProduct(resources), HttpStatus.CREATED);
    }

    @Log("修改商品")
    @ApiOperation(value = "修改商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PutMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreProductDto resources) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.updateProduct(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "恢复数据")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/recovery/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity recovery(@PathVariable Integer id) {
        yxStoreProductService.recovery(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    @PostMapping(value = "/yxStoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Integer id, @RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        yxStoreProductService.onSale(id, status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/yxStoreProduct/isFormatAttr/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity isFormatAttr(@PathVariable Integer id, @RequestBody String jsonStr) {
        return new ResponseEntity(yxStoreProductService.isFormatAttr(id, jsonStr), HttpStatus.OK);
    }

    @ApiOperation(value = "设置保存属性")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct/setAttr/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity setAttr(@PathVariable Integer id, @RequestBody String jsonStr) {
        yxStoreProductService.createProductAttr(id, jsonStr);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "清除属性")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct/clearAttr/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity clearAttr(@PathVariable Integer id) {
        yxStoreProductService.clearProductAttr(id, true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "获取属性")
    @GetMapping(value = "/yxStoreProduct/attr/{id}")
    public ResponseEntity attr(@PathVariable Integer id) {
        String str = yxStoreProductService.getStoreProductAttrResult(id);
        if (StrUtil.isEmpty(str)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        JSONObject jsonObject = JSON.parseObject(str);

        return new ResponseEntity(jsonObject, HttpStatus.OK);
    }

    @ApiOperation(value = "商品促销修改")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_CHANGE')")
    @PostMapping(value = "/yxStoreProduct/changeStatus")
    public ResponseEntity onBenefit(@RequestBody YxStoreProductChange request) {
        yxStoreProductService.changeStatus(request);
        return new ResponseEntity(true, HttpStatus.OK);
    }


}
