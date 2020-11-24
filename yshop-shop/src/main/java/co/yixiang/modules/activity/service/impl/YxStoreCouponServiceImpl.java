/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.activity.domain.YxStoreCoupon;
import co.yixiang.modules.activity.service.YxStoreCouponService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponDto;
import co.yixiang.modules.activity.service.dto.YxStoreCouponQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponMapper;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author liusy
 * @date 2020-08-31
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxStoreCoupon")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreCouponServiceImpl extends BaseServiceImpl<YxStoreCouponMapper, YxStoreCoupon> implements YxStoreCouponService {

    private final IGenerator generator;
    @Autowired
    private UserService userService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreCouponQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxStoreCoupon> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxStoreCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxStoreCoupon::getIsDel, 0).orderByDesc(YxStoreCoupon::getAddTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildStoreId() || criteria.getChildStoreId().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxStoreCoupon::getStoreId, criteria.getChildStoreId());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxStoreCoupon::getStatus, criteria.getStatus());
        }
        if (StringUtils.isNotBlank(criteria.getTitle())) {
            queryWrapper.lambda().like(YxStoreCoupon::getTitle, criteria.getTitle());
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            User user = this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getUsername()));
            if (null == user) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().lambda().eq(YxStoreInfo::getMerId, user.getId()));
            if (null == yxStoreInfo) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().eq(YxStoreCoupon::getStoreId, yxStoreInfo.getId());
        }
        IPage<YxStoreCoupon> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        map.put("content", generator.convert(ipage.getRecords(), YxStoreCouponDto.class));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreCoupon> queryAll(YxStoreCouponQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreCoupon.class, criteria));
    }


    @Override
    public void download(List<YxStoreCouponDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreCouponDto yxStoreCoupon : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("优惠券名称", yxStoreCoupon.getTitle());
            map.put("兑换消耗积分值", yxStoreCoupon.getIntegral());
            map.put("兑换的优惠券面值", yxStoreCoupon.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", yxStoreCoupon.getUseMinPrice());
            map.put("优惠券有效期限（单位：天）", yxStoreCoupon.getCouponTime());
            map.put("排序", yxStoreCoupon.getSort());
            map.put("状态（0：关闭，1：开启）", yxStoreCoupon.getStatus());
            map.put("兑换项目添加时间", yxStoreCoupon.getAddTime());
            map.put("是否删除", yxStoreCoupon.getIsDel());
            map.put("卡券所属商铺", yxStoreCoupon.getStoreId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
