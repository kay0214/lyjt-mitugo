package co.yixiang.modules.shop.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxCommissionRate;
import co.yixiang.modules.shop.service.YxCommissionRateService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Admin 分佣配置
* @author huiy
* @date 2020-08-20
*/
@AllArgsConstructor
@Api(tags = "分佣配置管理")
@RestController
@RequestMapping("/api/yxCommissionRate")
public class YxCommissionRateController {

    private final YxCommissionRateService yxCommissionRateService;


    @GetMapping
    @ApiOperation("查询分佣配置")
    @PreAuthorize("@el.check('admin','yxCommissionRate:view')")
    public ResponseEntity<Object> getOne(){
        QueryWrapper<YxCommissionRate> commissionRateQueryWrapper = new QueryWrapper<>();
        commissionRateQueryWrapper.lambda()
                .and(delF -> delF.eq(YxCommissionRate::getDelFlag, 0));
        YxCommissionRate yxCommissionRate = yxCommissionRateService.getOne(commissionRateQueryWrapper);
        yxCommissionRate.setFundsRate(yxCommissionRate.getFundsRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setShareRate(yxCommissionRate.getShareRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setShareParentRate(yxCommissionRate.getShareParentRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setParentRate(yxCommissionRate.getParentRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setPartnerRate(yxCommissionRate.getPartnerRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setReferenceRate(yxCommissionRate.getReferenceRate().multiply(new BigDecimal("100")));
        yxCommissionRate.setMerRate(yxCommissionRate.getMerRate().multiply(new BigDecimal("100")));
       return new ResponseEntity<>(yxCommissionRate, HttpStatus.OK);
    }

    @PutMapping
    @Log("修改分佣配置")
    @ApiOperation("修改分佣配置")
    @PreAuthorize("@el.check('admin','yxCommissionRate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxCommissionRate resources){
        // 不存在数据则保存, 存在则更新
        YxCommissionRate yxCommissionRate = yxCommissionRateService.getOne(new QueryWrapper<YxCommissionRate>().eq("del_flag", 0));

        BigDecimal fundsRate = resources.getFundsRate().divide(new BigDecimal("100"));
        BigDecimal shareRate = resources.getShareRate().divide(new BigDecimal("100"));
        BigDecimal shareParentRate = resources.getShareParentRate().divide(new BigDecimal("100"));
        BigDecimal parentRate = resources.getParentRate().divide(new BigDecimal("100"));
        BigDecimal partnerRate = resources.getPartnerRate().divide(new BigDecimal("100"));
        BigDecimal referenceRate = resources.getReferenceRate().divide(new BigDecimal("100"));
        BigDecimal merRate = resources.getMerRate().divide(new BigDecimal("100"));
//        BigDecimal fundsRate = resources.getFundsRate();
//        BigDecimal shareRate = resources.getShareRate();
//        BigDecimal shareParentRate = resources.getShareParentRate();
//        BigDecimal parentRate = resources.getParentRate();
//        BigDecimal partnerRate = resources.getPartnerRate();
//        BigDecimal referenceRate = resources.getReferenceRate();
//        BigDecimal merRate = resources.getMerRate();

        // 分佣总和应等于1.
        BigDecimal count = fundsRate.add(shareRate).add(shareParentRate).add(parentRate).add(partnerRate).add(referenceRate).add(merRate);

        // 分佣比例总和应等于1
        if (count.compareTo(new BigDecimal("1")) != 0){
            throw new BadRequestException("分佣比例配置不正确!");
        }

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();

        // 重新赋值计算后的值
        resources.setFundsRate(fundsRate);
        resources.setShareRate(shareRate);
        resources.setShareParentRate(shareParentRate);
        resources.setParentRate(parentRate);
        resources.setPartnerRate(partnerRate);
        resources.setReferenceRate(referenceRate);
        resources.setMerRate(merRate);

        // 更新状态
        boolean midifyStatus = false;
        if (yxCommissionRate == null){
            resources.setCreateUserId(loginUserId);
            resources.setCreateTime(DateTime.now().toTimestamp());
            resources.setUpdateUserId(loginUserId);
            resources.setUpdateTime(DateTime.now().toTimestamp());
            midifyStatus = yxCommissionRateService.save(resources);
        }else {
            resources.setId(yxCommissionRate.getId());
            resources.setUpdateUserId(loginUserId);
            resources.setUpdateTime(DateTime.now().toTimestamp());
            midifyStatus = yxCommissionRateService.updateById(resources);
        }
        return new ResponseEntity<>(midifyStatus, HttpStatus.NO_CONTENT);
    }

}
