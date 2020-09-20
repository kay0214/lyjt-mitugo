/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.rest;

import co.yixiang.modules.activity.service.YxStoreCouponUserService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserQueryCriteria;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hupeng
 * @date 2019-11-10
 */
@Api(tags = "商城:优惠券发放记录管理")
@RestController
@RequestMapping("api")
public class StoreCouponUserController {

    @Autowired
    private YxStoreCouponUserService yxStoreCouponUserService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxStoreCouponUser")
    @PreAuthorize("hasAnyRole('admin','YXSTORECOUPONUSER_ALL','YXSTORECOUPONUSER_SELECT')")
    public ResponseEntity getYxStoreCouponUsers(YxStoreCouponUserQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
            criteria.setChildStoreId(this.yxStoreInfoService.getStoreIdByMerId(currUser.getChildUser()));
        }
        if(criteria.getChildStoreId()==null || criteria.getChildStoreId().size()==0){
            return new ResponseEntity(null, HttpStatus.OK);
        }
        return new ResponseEntity(yxStoreCouponUserService.queryAll(criteria, pageable), HttpStatus.OK);
    }


}
