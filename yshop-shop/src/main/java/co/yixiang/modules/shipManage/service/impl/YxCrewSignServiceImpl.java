/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shipManage.domain.YxCrewSign;
import co.yixiang.modules.shipManage.service.YxCrewSignService;
import co.yixiang.modules.shipManage.service.dto.YxCrewSignDto;
import co.yixiang.modules.shipManage.service.dto.YxCrewSignQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxCrewSignMapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
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
* @author nxl
* @date 2020-11-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCrewSign")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCrewSignServiceImpl extends BaseServiceImpl<YxCrewSignMapper, YxCrewSign> implements YxCrewSignService {

    private final IGenerator generator;
    @Autowired
    private YxCrewSignMapper yxCrewSignMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCrewSignQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCrewSign> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCrewSignDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCrewSign> queryAll(YxCrewSignQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCrewSign.class, criteria));
    }


    @Override
    public void download(List<YxCrewSignDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCrewSignDto yxCrewSign : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", yxCrewSign.getUid());
            map.put("用户名", yxCrewSign.getUsername());
            map.put("联系电话", yxCrewSign.getUserPhone());
            map.put("体温", yxCrewSign.getTemperature());
            map.put("是否删除（0：未删除，1：已删除）", yxCrewSign.getDelFlag());
            map.put("创建人", yxCrewSign.getCreateUserId());
            map.put("修改人", yxCrewSign.getUpdateUserId());
            map.put("创建时间（签到时间）", yxCrewSign.getCreateTime());
            map.put("更新时间", yxCrewSign.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> queryAllNew(YxCrewSignQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCrewSign> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxCrewSign> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCrewSign::getDelFlag ,0);
        if(StringUtils.isNotBlank(criteria.getEndDate())&&StringUtils.isNotBlank(criteria.getStartDate())){
            queryWrapper.lambda().between(YxCrewSign::getCreateTime,criteria.getStartDate()+" 00:00:00",criteria.getEndDate()+" 23:59:59");
        }
        if(StringUtils.isNotBlank(criteria.getNickName())){
            queryWrapper.lambda().like(YxCrewSign::getNickName,criteria.getNickName());
        }
        if(StringUtils.isNotBlank(criteria.getUsername())){
            queryWrapper.lambda().like(YxCrewSign::getUsername,criteria.getUsername());
        }
        IPage<YxCrewSign> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        map.put("content", generator.convert(ipage.getRecords(), YxCrewSignDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }
}
