/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.YxCouponsPriceConfig;
import co.yixiang.modules.coupon.service.YxCouponsPriceConfigService;
import co.yixiang.modules.coupon.service.dto.YxCouponsPriceConfigDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsPriceConfigQueryCriteria;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
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
@Api(tags = "本地生活价格配置管理")
@RestController
@RequestMapping("/api/yxCouponsPriceConfig")
public class YxCouponsPriceConfigController {

    private final YxCouponsPriceConfigService yxCouponsPriceConfigService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:list')")
    public void download(HttpServletResponse response, YxCouponsPriceConfigQueryCriteria criteria) throws IOException {
        yxCouponsPriceConfigService.download(generator.convert(yxCouponsPriceConfigService.queryAll(criteria), YxCouponsPriceConfigDto.class), response);
    }

    @GetMapping
    @Log("查询本地生活价格配置")
    @ApiOperation("查询本地生活价格配置")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:list')")
    public ResponseEntity<Object> getYxCouponsPriceConfigs(YxCouponsPriceConfigQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxCouponsPriceConfigService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增本地生活价格配置")
    @ApiOperation("新增本地生活价格配置")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponsPriceConfig resources) {
        return new ResponseEntity<>(yxCouponsPriceConfigService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改本地生活价格配置")
    @ApiOperation("修改本地生活价格配置")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCouponsPriceConfig resources) {
        yxCouponsPriceConfigService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除本地生活价格配置")
    @ApiOperation("删除本地生活价格配置")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id -> {
            yxCouponsPriceConfigService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 价格配置
     *
     * @param priceJson
     * @return
     */
    @PostMapping(value = "/setPriceConfig")
    @Log("本地生活价格配置")
    @ApiOperation("本地生活价格配置")
    @PreAuthorize("@el.check('admin','yxCouponsPriceConfig:add')")
    public ResponseEntity<Object> setPriceConfig(@Validated @RequestBody String priceJson) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        yxCouponsPriceConfigService.setPriceConfig(priceJson, currUser.getId().intValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getPriceConfigList/{couponId}")
    @Log("获取本地生活价格配置")
    @ApiOperation("获取本地生活价格配置")
//    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> getPriceConfigList(@PathVariable Integer couponId) {
        QueryWrapper<YxCouponsPriceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponsPriceConfig::getCouponId, couponId).eq(YxCouponsPriceConfig::getDelFlag, 0).orderByAsc(YxCouponsPriceConfig::getStartDate);
        List<YxCouponsPriceConfig> priceConfigList = yxCouponsPriceConfigService.list(queryWrapper);
        List<YxCouponsPriceConfigDto> priceConfigDtoList = CommonsUtils.convertBeanList(priceConfigList, YxCouponsPriceConfigDto.class);
        //格式日期
        if (!CollectionUtils.isEmpty(priceConfigDtoList)) {
            for (YxCouponsPriceConfigDto configDto : priceConfigDtoList) {
                if (configDto.getStartDate().toString().length() == 3) {
                    configDto.setStartDateStr("0" + configDto.getStartDate().toString());
                } else {
                    configDto.setStartDateStr(configDto.getStartDate().toString());
                }
                if (configDto.getEndDate().toString().length() == 3) {
                    configDto.setEndDateStr("0" + configDto.getEndDate().toString());
                } else {
                    configDto.setEndDateStr(configDto.getEndDate().toString());
                }
            }
        }
        return new ResponseEntity(priceConfigDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/delAllPriceConfig/{couponId}")
    @Log("清空价格配置")
    @ApiOperation("清空价格配置")
//    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> delAllPriceConfig(@PathVariable Integer couponId) {
        QueryWrapper<YxCouponsPriceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponsPriceConfig::getCouponId, couponId).eq(YxCouponsPriceConfig::getDelFlag, 0);
        yxCouponsPriceConfigService.remove(queryWrapper);
        return new ResponseEntity(HttpStatus.OK);
    }
}
