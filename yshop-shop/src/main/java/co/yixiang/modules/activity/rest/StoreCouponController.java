/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.activity.rest;

import cn.hutool.core.bean.BeanUtil;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.SroreCouponAddRequest;
import co.yixiang.modules.activity.domain.SroreCouponModifyRequest;
import co.yixiang.modules.activity.domain.YxStoreCoupon;
import co.yixiang.modules.activity.service.YxStoreCouponService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponQueryCriteria;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author hupeng
* @date 2019-11-09
*/
@Api(tags = "商城:优惠券管理")
@RestController
@RequestMapping("api")
public class StoreCouponController {

    private final YxStoreCouponService yxStoreCouponService;

    @Autowired
    private UserService userService;

    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    public StoreCouponController(YxStoreCouponService yxStoreCouponService) {
        this.yxStoreCouponService = yxStoreCouponService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxStoreCoupon")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_SELECT')")
    public ResponseEntity getYxStoreCoupons(YxStoreCouponQueryCriteria criteria, Pageable pageable){
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
            criteria.setChildStoreId(this.yxStoreInfoService.getStoreIdByMerId(currUser.getChildUser()));
        }
        return new ResponseEntity(yxStoreCouponService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询指定优惠券详情")
    @ApiOperation(value = "查询指定优惠券详情")
    @GetMapping(value = "/yxStoreCoupon/getCouponInfo/{id}")
    public ResponseEntity<Object> getCouponInfo(@PathVariable Integer id){
        return new ResponseEntity<>(yxStoreCouponService.getOne(new QueryWrapper<YxStoreCoupon>().eq("id", id)), HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yxStoreCoupon")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SroreCouponAddRequest resources){

        // 当前登录用户ID (此账户应为商铺管理员)
        int loginUserId = SecurityUtils.getUserId().intValue();

        YxStoreInfo findStoreInfo = yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", loginUserId).eq("del_flag", 0));
        if (findStoreInfo == null){
            throw new BadRequestException("当前商户未绑定商铺!");
        }

        YxStoreCoupon yxStoreCoupon = new YxStoreCoupon();
        BeanUtil.copyProperties(resources, yxStoreCoupon);
        // 优惠券直接绑定到商铺 不可修改
        yxStoreCoupon.setBelong(findStoreInfo.getId());
        yxStoreCoupon.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxStoreCouponService.save(yxStoreCoupon),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yxStoreCoupon")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_EDIT')")
    public ResponseEntity update(@Validated @RequestBody SroreCouponModifyRequest resources){
        YxStoreCoupon yxStoreCoupon = new YxStoreCoupon();
        BeanUtil.copyProperties(resources, yxStoreCoupon);
        return new ResponseEntity(yxStoreCouponService.saveOrUpdate(yxStoreCoupon), HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxStoreCoupon/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        YxStoreCoupon resources = new YxStoreCoupon();
        resources.setId(id);
        resources.setIsDel(1);
        yxStoreCouponService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.OK);
    }
}
