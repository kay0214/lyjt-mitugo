/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.rest;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shipManage.domain.YxShipInfo;
import co.yixiang.modules.shipManage.param.YxShipInfoRequest;
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.service.YxShipInfoService;
import co.yixiang.modules.shipManage.service.YxShipSeriesService;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoDto;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoQueryCriteria;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesDto;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.CurrUser;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author nxl
* @date 2020-11-04
*/
@AllArgsConstructor
@Api(tags = "船只管理管理")
@RestController
@RequestMapping("/api/yxShipInfo")
public class YxShipInfoController {

    @Autowired
    private YxShipInfoService yxShipInfoService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private IGenerator generator;
    @Autowired
    private UserService userService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxShipInfo:list')")
    public void download(HttpServletResponse response, YxShipInfoQueryCriteria criteria) throws IOException {
        yxShipInfoService.download(generator.convert(yxShipInfoService.queryAll(criteria), YxShipInfoDto.class), response);
    }

    @GetMapping
    @Log("查询船只管理")
    @ApiOperation("查询船只管理")
    @PreAuthorize("@el.check('admin','yxShipInfo:list')")
    public ResponseEntity<Object> getYxShipInfos(YxShipInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxShipInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增船只管理")
    @ApiOperation("新增船只管理")
    @PreAuthorize("@el.check('admin','yxShipInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxShipInfoRequest resources){
        return new ResponseEntity<>(yxShipInfoService.saveOrUpdShipInfoByParam(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改船只管理")
    @ApiOperation("修改船只管理")
    @PreAuthorize("@el.check('admin','yxShipInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxShipInfoRequest resources){
//        yxShipInfoService.updateById(resources);
        yxShipInfoService.saveOrUpdShipInfoByParam(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除船只管理")
    @ApiOperation("删除船只管理")
    @PreAuthorize("@el.check('admin','yxShipInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yxShipInfoService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AnonymousAccess
    @ApiOperation(value = "查询船只系列")
    @GetMapping(value = "/getShipSeriseList")
    public ResponseEntity getShipSeriseList() {
        List<YxShipSeries> shipSeriesList = yxShipInfoService.getShipSeriseList();
        List<YxShipSeriesDto> categoryDtoLists = CommonsUtils.convertBeanList(shipSeriesList, YxShipSeriesDto.class);
        return new ResponseEntity(categoryDtoLists, HttpStatus.OK);
    }

    @AnonymousAccess
    @ApiOperation(value = "根据船只系列获取船只")
    @GetMapping(value = "/getShipInfoBySeriesId/{seriesId}")
    public ResponseEntity getShipInfoBySeriesId(@PathVariable Integer seriesId) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        Map<String,Object> mapRetrun = new HashMap<String,Object>();
        YxShipSeries yxShipSeries = yxShipSeriesService.getById(seriesId);
        mapRetrun.put("rideLimit",yxShipSeries.getRideLimit());
        List<YxShipInfo> shipInfoList = yxShipInfoService.getShipInfoList(seriesId,currUser.getId().intValue());
        List<YxShipInfoDto> shipInfoDtoList = CommonsUtils.convertBeanList(shipInfoList, YxShipInfoDto.class);
        mapRetrun.put("shipInfoList",shipInfoDtoList);
        return new ResponseEntity(mapRetrun, HttpStatus.OK);
//        return new ResponseEntity(shipInfoDtoList, HttpStatus.OK);
    }
    @PostMapping(value = "/changeStatus/{id}")
    @Log("修改船只系列状态")
    @ApiOperation("修改船只系列状态")
    @PreAuthorize("@el.check('admin','yxShipSeries:edit')")
    public ResponseEntity<Object> changeStatus(@PathVariable Integer id) {
        yxShipInfoService.changeStatus(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
