/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shop.domain.YxCustomerService;
import co.yixiang.modules.shop.service.YxCustomerServiceService;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceDto;
import co.yixiang.modules.shop.service.dto.YxCustomerServiceQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxCustomerServiceMapper;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @author nxl
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCustomerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCustomerServiceServiceImpl extends BaseServiceImpl<YxCustomerServiceMapper, YxCustomerService> implements YxCustomerServiceService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCustomerServiceQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxCustomerService> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxCustomerService> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCustomerService::getDelFlag, 0);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildStoreId() || criteria.getChildStoreId().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxCustomerService::getMerId, criteria.getChildStoreId());
        }
        IPage<YxCustomerService> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }

        map.put("content", generator.convert(ipage.getRecords(), YxCustomerServiceDto.class));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCustomerService> queryAll(YxCustomerServiceQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCustomerService.class, criteria));
    }


    @Override
    public void download(List<YxCustomerServiceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCustomerServiceDto yxCustomerService : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("问题", yxCustomerService.getQuestion());
            map.put("排序", yxCustomerService.getSort());
            map.put("状态：0：启用，1：禁用", yxCustomerService.getStatus());
            map.put("用户角色：0->平台运营,1->合伙人,2->商户", yxCustomerService.getUserRole());
            map.put("是否删除（0：未删除，1：已删除）", yxCustomerService.getDelFlag());
            map.put("创建人", yxCustomerService.getCreateUserId());
            map.put("修改人", yxCustomerService.getUpdateUserId());
            map.put("创建时间", yxCustomerService.getCreateTime());
            map.put("更新时间", yxCustomerService.getUpdateTime());
            map.put("回答", yxCustomerService.getAnswer());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
