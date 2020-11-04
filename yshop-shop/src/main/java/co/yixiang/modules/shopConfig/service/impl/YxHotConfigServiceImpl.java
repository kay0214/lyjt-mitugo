/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.service.impl;

import co.yixiang.modules.shopConfig.domain.YxHotConfig;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shopConfig.service.YxHotConfigService;
import co.yixiang.modules.shopConfig.service.dto.YxHotConfigDto;
import co.yixiang.modules.shopConfig.service.dto.YxHotConfigQueryCriteria;
import co.yixiang.modules.shopConfig.service.mapper.YxHotConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author nxl
* @date 2020-11-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxHotConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxHotConfigServiceImpl extends BaseServiceImpl<YxHotConfigMapper, YxHotConfig> implements YxHotConfigService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxHotConfigQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxHotConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxHotConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxHotConfig> queryAll(YxHotConfigQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxHotConfig.class, criteria));
    }


    @Override
    public void download(List<YxHotConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxHotConfigDto yxHotConfig : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("公司名", yxHotConfig.getTitle());
            map.put("封面图地址", yxHotConfig.getCoverImg());
            map.put("链接", yxHotConfig.getLinkUrl());
            map.put("排序", yxHotConfig.getSort());
            map.put("状态：0：启用，1：禁用", yxHotConfig.getStatus());
            map.put("是否删除（0：未删除，1：已删除）", yxHotConfig.getDelFlag());
            map.put("创建人", yxHotConfig.getCreateUserId());
            map.put("修改人", yxHotConfig.getUpdateUserId());
            map.put("创建时间", yxHotConfig.getCreateTime());
            map.put("更新时间", yxHotConfig.getUpdateTime());
            map.put("内容", yxHotConfig.getContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
