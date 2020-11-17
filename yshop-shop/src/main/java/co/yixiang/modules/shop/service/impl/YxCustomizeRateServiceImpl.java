/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.shop.domain.YxCustomizeRate;
import co.yixiang.modules.shop.service.YxCustomizeRateService;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateDto;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxCustomizeRateMapper;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCustomizeRate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCustomizeRateServiceImpl extends BaseServiceImpl<YxCustomizeRateMapper, YxCustomizeRate> implements YxCustomizeRateService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCustomizeRateQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCustomizeRate> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCustomizeRateDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCustomizeRate> queryAll(YxCustomizeRateQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCustomizeRate.class, criteria));
    }


    @Override
    public void download(List<YxCustomizeRateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCustomizeRateDto yxCustomizeRate : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("类型：0:商品购买 1:本地生活", yxCustomizeRate.getRateType());
            map.put("卡券/商品关联id", yxCustomizeRate.getLinkId());
            map.put("平台抽成", yxCustomizeRate.getFundsRate());
            map.put("分享人", yxCustomizeRate.getShareRate());
            map.put("分享人上级", yxCustomizeRate.getShareParentRate());
            map.put("推荐人", yxCustomizeRate.getParentRate());
            map.put("商户", yxCustomizeRate.getMerRate());
            map.put("合伙人", yxCustomizeRate.getPartnerRate());
            map.put("拉新池", yxCustomizeRate.getReferenceRate());
            map.put("是否删除（0：未删除，1：已删除）", yxCustomizeRate.getDelFlag());
            map.put("创建人", yxCustomizeRate.getCreateUserId());
            map.put("修改人", yxCustomizeRate.getUpdateUserId());
            map.put("创建时间", yxCustomizeRate.getCreateTime());
            map.put("更新时间", yxCustomizeRate.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 保存或更新自定义分佣比例
     *
     * @param yxCustomizeRate
     * @return
     */
    @Override
    public boolean saveOrUpdateRate(YxCustomizeRate yxCustomizeRate) {
        BigDecimal fundsRate = yxCustomizeRate.getFundsRate().divide(new BigDecimal("100"));
        BigDecimal shareRate = yxCustomizeRate.getShareRate().divide(new BigDecimal("100"));
        BigDecimal shareParentRate = yxCustomizeRate.getShareParentRate().divide(new BigDecimal("100"));
        BigDecimal parentRate = yxCustomizeRate.getParentRate().divide(new BigDecimal("100"));
        BigDecimal partnerRate = yxCustomizeRate.getPartnerRate().divide(new BigDecimal("100"));
        BigDecimal referenceRate = yxCustomizeRate.getReferenceRate().divide(new BigDecimal("100"));
        BigDecimal merRate = yxCustomizeRate.getMerRate().divide(new BigDecimal("100"));
        // 分佣总和应等于1.
        BigDecimal count = fundsRate.add(shareRate).add(shareParentRate).add(parentRate).add(partnerRate).add(referenceRate).add(merRate);
        // 分佣比例总和应等于1
        if (count.compareTo(new BigDecimal("1")) != 0) {
            throw new BadRequestException("分佣比例配置不正确!");
        }

        YxCustomizeRate findRate = this.getOne(new QueryWrapper<YxCustomizeRate>().lambda()
                .eq(YxCustomizeRate::getRateType, yxCustomizeRate.getRateType())
                .eq(YxCustomizeRate::getLinkId, yxCustomizeRate.getLinkId())
                .eq(YxCustomizeRate::getDelFlag, 0));
        // 未查询到数据说明未保存过、插入一条数据
        if (null == findRate) {
            // 处理入库数据
            YxCustomizeRate insertRate = new YxCustomizeRate();
            insertRate.setRateType(yxCustomizeRate.getRateType());
            insertRate.setLinkId(yxCustomizeRate.getLinkId());
            insertRate.setFundsRate(fundsRate);
            insertRate.setShareRate(shareRate);
            insertRate.setShareParentRate(shareParentRate);
            insertRate.setParentRate(parentRate);
            insertRate.setPartnerRate(partnerRate);
            insertRate.setReferenceRate(referenceRate);
            insertRate.setMerRate(merRate);
            insertRate.setDelFlag(0);
            // 计入操作人和时间
            insertRate.setCreateUserId(yxCustomizeRate.getCreateUserId());
            insertRate.setCreateTime(DateTime.now().toTimestamp());
            this.save(insertRate);
            return true;
        } else {
            // 处理数据
            findRate.setFundsRate(fundsRate);
            findRate.setShareRate(shareRate);
            findRate.setShareParentRate(shareParentRate);
            findRate.setParentRate(parentRate);
            findRate.setPartnerRate(partnerRate);
            findRate.setReferenceRate(referenceRate);
            findRate.setMerRate(merRate);
            findRate.setUpdateUserId(yxCustomizeRate.getUpdateUserId());
            findRate.setUpdateTime(DateTime.now().toTimestamp());
            this.updateById(findRate);
            return true;
        }
    }
}
