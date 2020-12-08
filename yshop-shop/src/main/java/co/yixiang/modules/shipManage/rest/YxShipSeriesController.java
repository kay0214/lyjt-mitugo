/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shipManage.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.mybatis.GeoPoint;
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.param.YxShipSeriesRequest;
import co.yixiang.modules.shipManage.service.YxShipSeriesService;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesDto;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesQueryCriteria;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author nxl
 * @date 2020-11-04
 */
@AllArgsConstructor
@Api(tags = "船只系列管理")
@RestController
@RequestMapping("/api/yxShipSeries")
public class YxShipSeriesController {

    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    private final IGenerator generator;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipSeries:list')")
    public void download(HttpServletResponse response, YxShipSeriesQueryCriteria criteria) throws IOException {
        yxShipSeriesService.download(generator.convert(yxShipSeriesService.queryAll(criteria), YxShipSeriesDto.class), response);
    }

    @GetMapping
    @Log("查询船只系列")
    @ApiOperation("查询船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:list')")
    public ResponseEntity<Object> getYxShipSeriess(YxShipSeriesQueryCriteria criteria, Pageable pageable) {
        //商户id = 当前登录用户
        int loginUserId = SecurityUtils.getUserId().intValue();
        if (2 == SecurityUtils.getCurrUser().getUserRole()) {
            criteria.setMerId(loginUserId);
        }
        return new ResponseEntity<>(yxShipSeriesService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只系列")
    @ApiOperation("新增船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipSeriesRequest resources) {
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxStoreInfo storeInfo = yxStoreInfoService.getStoreInfoByMerId(loginUserId);
        if (null == storeInfo) {
            throw new RuntimeException("根据商户id ：" + loginUserId + " 获取店铺信息失败！");
        }
        resources.setDelFlag(0);
        resources.setCreateUserId(loginUserId);
        resources.setCreateTime(DateTime.now().toTimestamp());
        resources.setUpdateUserId(loginUserId);
        resources.setUpdateTime(DateTime.now().toTimestamp());
        resources.setMerId(loginUserId);
        //获取店铺信息
        resources.setStoreId(storeInfo.getId());
        GeoPoint geoPoint = new GeoPoint(new BigDecimal(resources.getCoordinateX()), new BigDecimal(resources.getCoordinateY()));
//        resources.setCoordinate(geoPoint);
        YxShipSeries shipSeries = new YxShipSeries();
        BeanUtils.copyProperties(resources, shipSeries);
        shipSeries.setCoordinate(geoPoint);
        int isSave = yxShipSeriesService.insertSelective(shipSeries);
        return new ResponseEntity<>(isSave, HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只系列")
    @ApiOperation("修改船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipSeriesRequest resources) {
        int loginUserId = SecurityUtils.getUserId().intValue();
        /*YxStoreInfo storeInfo = yxStoreInfoService.getStoreInfoByMerId(loginUserId);
        if (null == storeInfo) {
            throw new RuntimeException("根据商户id ：" + loginUserId + " 获取店铺信息失败！");
        }*/
        resources.setUpdateUserId(loginUserId);
        resources.setUpdateTime(DateTime.now().toTimestamp());
//        resources.setMerId(loginUserId);
        GeoPoint geoPoint = new GeoPoint(new BigDecimal(resources.getCoordinateX()), new BigDecimal(resources.getCoordinateY()));
        YxShipSeries shipSeries = new YxShipSeries();
        BeanUtils.copyProperties(resources, shipSeries);
        shipSeries.setCoordinate(geoPoint);
        yxShipSeriesService.updateByPrimaryKeySelective(shipSeries);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只系列")
    @ApiOperation("删除船只系列")
    @PreAuthorize("@el.check('admin','yxShipSeries:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id -> {
            yxShipSeriesService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/changeStatus/{id}")
    @Log("修改船只系列状态")
    @ApiOperation("修改船只系列状态")
    @PreAuthorize("@el.check('admin','yxShipSeries:edit')")
    public ResponseEntity<Object> changeStatus(@PathVariable Integer id) {
        yxShipSeriesService.changeStatus(id);

        return new ResponseEntity(HttpStatus.OK);
    }

}
