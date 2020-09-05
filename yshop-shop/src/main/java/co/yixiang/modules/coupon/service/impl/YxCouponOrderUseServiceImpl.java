/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponOrderUseService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponOrderUseMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author huiy
 * @date 2020-08-27
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponOrderUse")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponOrderUseServiceImpl extends BaseServiceImpl<YxCouponOrderUseMapper, YxCouponOrderUse> implements YxCouponOrderUseService {

    private final IGenerator generator;
    @Autowired
    private YxCouponsService yxCouponsService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderUseQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponOrderUse> page = new PageInfo<>(queryAll(criteria));
        if (page.getTotal() == 0) {
            Map<String, Object> map = new LinkedHashMap<>(2);
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxCouponOrderUseDto> list = new ArrayList<>();
        for (YxCouponOrderUse item : page.getList()) {
            YxCouponOrderUseDto dto = generator.convert(item, YxCouponOrderUseDto.class);
            YxCoupons yxCoupons = this.yxCouponsService.getById(item.getCouponId());
            dto.setCouponType(yxCoupons.getCouponType());
            dto.setDenomination(yxCoupons.getDenomination());
            dto.setDiscount(yxCoupons.getDiscount());
            dto.setThreshold(yxCoupons.getThreshold());
            dto.setDiscountAmount(yxCoupons.getDiscountAmount());
            list.add(dto);
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponOrderUseDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponOrderUse> queryAll(YxCouponOrderUseQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrderUse.class, criteria));
    }


    @Override
    public void download(List<YxCouponOrderUseDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponOrderUseDto yxCouponOrderUse : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单号", yxCouponOrderUse.getOrderId());
            map.put("核销商铺id", yxCouponOrderUse.getStoreId());
            map.put("店铺名称", yxCouponOrderUse.getStoreName());
            map.put("核销次数", yxCouponOrderUse.getUsedCount());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponOrderUse.getDelFlag());
            map.put("创建人 根据创建人关联店铺", yxCouponOrderUse.getCreateUserId());
            map.put("修改人", yxCouponOrderUse.getUpdateUserId());
            map.put("创建时间", yxCouponOrderUse.getCreateTime());
            map.put("更新时间", yxCouponOrderUse.getUpdateTime());
            map.put("卡券id", yxCouponOrderUse.getCouponId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
