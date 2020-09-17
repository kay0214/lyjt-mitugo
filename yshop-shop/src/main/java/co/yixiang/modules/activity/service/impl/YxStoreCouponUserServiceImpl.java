/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.activity.domain.YxStoreCouponUser;
import co.yixiang.modules.activity.service.YxStoreCouponUserService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponQueryParam;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserDto;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponUserMapper;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
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
//@CacheConfig(cacheNames = "yxStoreCouponUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreCouponUserServiceImpl extends BaseServiceImpl<YxStoreCouponUserMapper, YxStoreCouponUser> implements YxStoreCouponUserService {

    private final IGenerator generator;
    private final YxUserService userService;
    private final YxStoreCouponUserMapper storeCouponUserMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreCouponUserQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxStoreCouponUser> page = new PageInfo<>(queryAll(criteria));
        /*QueryWrapper<YxStoreCouponUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxStoreCouponUser::getAddTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildStoreId() || criteria.getChildStoreId().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxStoreCouponUser::getStoreId, criteria.getChildStoreId());
        }
        if(StringUtils.isNotBlank(criteria.getCouponTitle())){
            queryWrapper.lambda().like(YxStoreCouponUser::getCouponTitle, criteria.getCouponTitle());

        }
        IPage<YxStoreCouponUser> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        List<YxStoreCouponUserDto> storeOrderDTOS = generator.convert(ipage.getRecords(), YxStoreCouponUserDto.class);
        for (YxStoreCouponUserDto couponUserDTO : storeOrderDTOS) {
            YxUser user = userService.getOne(new QueryWrapper<YxUser>().eq("uid", couponUserDTO.getUid()));
            if (null != user && StringUtils.isNotBlank(user.getNickname())) {
                couponUserDTO.setNickname(user.getNickname());
            }
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeOrderDTOS);
        map.put("totalElements", ipage.getTotal());*/

        YxStoreCouponQueryParam queryParam = new YxStoreCouponQueryParam();
        BeanUtils.copyProperties(criteria,queryParam);
        Page<YxStoreCouponUserDto> pageParam = new Page<YxStoreCouponUserDto>(pageable.getPageNumber()+1,pageable.getPageSize());
        int countSize = storeCouponUserMapper.countCouponUserPage(queryParam,criteria.getChildStoreId());
        List<YxStoreCouponUserDto> storeCouponUserDtoList = storeCouponUserMapper.selectCouponUserPage(pageParam,queryParam,criteria.getChildStoreId());

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeCouponUserDtoList);
        map.put("totalElements", countSize);
        return map;
    }

    @Override
    //@Cacheable
    public List<YxStoreCouponUser> queryAll(YxStoreCouponUserQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreCouponUser.class, criteria));
    }

    @Override
    public void download(List<YxStoreCouponUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreCouponUserDto yxStoreCouponUser : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("兑换的项目id", yxStoreCouponUser.getCid());
            map.put("优惠券所属用户", yxStoreCouponUser.getUid());
            map.put("优惠券名称", yxStoreCouponUser.getCouponTitle());
            map.put("优惠券的面值", yxStoreCouponUser.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", yxStoreCouponUser.getUseMinPrice());
            map.put("优惠券创建时间", yxStoreCouponUser.getAddTime());
            map.put("优惠券结束时间", yxStoreCouponUser.getEndTime());
            map.put("使用时间", yxStoreCouponUser.getUseTime());
            map.put("获取方式", yxStoreCouponUser.getType());
            map.put("状态（0：未使用，1：已使用, 2:已过期）", yxStoreCouponUser.getStatus());
            map.put("是否有效", yxStoreCouponUser.getIsFail());
            map.put("商铺id", yxStoreCouponUser.getStoreId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
