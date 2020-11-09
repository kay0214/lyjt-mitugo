/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shopConfig.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shopConfig.domain.YxMerchantsSettlement;
import co.yixiang.modules.shopConfig.service.YxMerchantsSettlementService;
import co.yixiang.modules.shopConfig.service.dto.YxMerchantsSettlementDto;
import co.yixiang.modules.shopConfig.service.dto.YxMerchantsSettlementQueryCriteria;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nxl
 * @date 2020-11-04
 */
@AllArgsConstructor
@Api(tags = "商家入驻管理")
@RestController
@RequestMapping("/api/yxMerchantsSettlement")
public class YxMerchantsSettlementController {

    private final YxMerchantsSettlementService yxMerchantsSettlementService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yxMerchantsSettlement:list')")
    public void download(HttpServletResponse response, YxMerchantsSettlementQueryCriteria criteria) throws IOException {
        yxMerchantsSettlementService.download(generator.convert(yxMerchantsSettlementService.queryAll(criteria), YxMerchantsSettlementDto.class), response);
    }

    @GetMapping
    @Log("查询商家入驻")
    @ApiOperation("查询商家入驻")
    @PreAuthorize("@el.check('admin','yxMerchantsSettlement:list')")
    public ResponseEntity<Object> getYxMerchantsSettlements(YxMerchantsSettlementQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxMerchantsSettlementService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PutMapping
    @Log("处理商家入驻数据")
    @ApiOperation("处理商家入驻数据")
    @PreAuthorize("@el.check('admin','yxMerchantsSettlement:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxMerchantsSettlement resources) {
        int sysUserId = SecurityUtils.getUserId().intValue();
        YxMerchantsSettlement updateConfig = this.yxMerchantsSettlementService.selectById(resources.getId());
        if (null == updateConfig) {
            throw new BadRequestException("当前数据不存在");
        }
        YxMerchantsSettlement updateDate = new YxMerchantsSettlement();
        updateDate.setId(resources.getId());
        updateDate.setStatus(resources.getStatus());
        updateDate.setRemark(resources.getRemark());
        updateDate.setUpdateUserId(sysUserId);
        updateDate.setUpdateTime(DateTime.now().toTimestamp());
        yxMerchantsSettlementService.updateById(updateDate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除商家入驻")
    @ApiOperation("删除商家入驻")
    @PreAuthorize("@el.check('admin','yxMerchantsSettlement:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer id) {
        int sysUserId = SecurityUtils.getUserId().intValue();
        YxMerchantsSettlement updateConfig = this.yxMerchantsSettlementService.selectById(id);
        if (null == updateConfig) {
            throw new BadRequestException("当前数据不存在");
        }
        updateConfig.setId(id);
        updateConfig.setDelFlag(1);
        updateConfig.setUpdateUserId(sysUserId);
        updateConfig.setUpdateTime(DateTime.now().toTimestamp());
        yxMerchantsSettlementService.updateById(updateConfig);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
