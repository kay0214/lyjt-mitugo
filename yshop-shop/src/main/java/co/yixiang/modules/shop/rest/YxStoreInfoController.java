/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.rest;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.domain.YxStoreInfoRequest;
import co.yixiang.modules.shop.domain.YxStoreInfoResponse;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.dto.YxStoreInfoDto;
import co.yixiang.modules.shop.service.dto.YxStoreInfoQueryCriteria;
import co.yixiang.utils.BeanUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author nxl
 * @date 2020-08-14
 */
@AllArgsConstructor
@Api(tags = "店铺表管理")
@RestController
@RequestMapping("/api/yxStoreInfo")
public class YxStoreInfoController {

    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxStoreInfo:list')")
    public void download(HttpServletResponse response, YxStoreInfoQueryCriteria criteria) throws IOException {
        criteria.setDelFlag(0);
        yxStoreInfoService.download(generator.convert(yxStoreInfoService.queryAll(criteria), YxStoreInfoDto.class), response);
    }

    @GetMapping
    @Log("查询店铺表")
    @ApiOperation("查询店铺表")
    @PreAuthorize("@el.check('admin','yxStoreInfo:list')")
    public ResponseEntity<Object> getYxStoreInfos(YxStoreInfoQueryCriteria criteria, Pageable pageable) {
        criteria.setDelFlag(0);
        return new ResponseEntity<>(yxStoreInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增店铺表")
    @ApiOperation("新增店铺表")
    @PreAuthorize("@el.check('admin','yxStoreInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxStoreInfoDto resources) {
        YxStoreInfo yxStoreInfo = new YxStoreInfo();
        BeanUtils.copyProperties(resources,yxStoreInfo);
        return new ResponseEntity<>(yxStoreInfoService.save(yxStoreInfo), HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateStoreInfo")
    @Log("修改店铺表")
    @ApiOperation("修改店铺表")
    @PreAuthorize("@el.check('admin','yxStoreInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxStoreInfoRequest resources) {
        yxStoreInfoService.updateStoreInfo(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除店铺表")
    @ApiOperation("删除店铺表")
    @PreAuthorize("@el.check('admin','yxStoreInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id -> {
            yxStoreInfoService.updateDelFlg(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/changeStatus/{id}")
    @Log("店铺上下架")
    @ApiOperation("店铺上下架")
    @PreAuthorize("@el.check('admin','yxStoreInfo:edit')")
    public ResponseEntity<Object> changeStatus(@PathVariable Integer id,@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        yxStoreInfoService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/getStoreInfoById/{id}")
    @Log("获取店铺信息")
    @ApiOperation("获取店铺信息")
    @PreAuthorize("@el.check('admin','yxStoreInfo:edit')")
    public ResponseEntity<Object> getStoreInfoById(@PathVariable Integer id) {
        YxStoreInfoResponse yxStoreInfoResponse = yxStoreInfoService.getStoreInfo(id);
        return new ResponseEntity<>(yxStoreInfoResponse, HttpStatus.OK);
    }
}
