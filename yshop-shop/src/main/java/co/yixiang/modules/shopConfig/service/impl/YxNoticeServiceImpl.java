/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.service.impl;

import co.yixiang.modules.shopConfig.domain.YxNotice;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shopConfig.service.YxNoticeService;
import co.yixiang.modules.shopConfig.service.dto.YxNoticeDto;
import co.yixiang.modules.shopConfig.service.dto.YxNoticeQueryCriteria;
import co.yixiang.modules.shopConfig.service.mapper.YxNoticeMapper;
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
//@CacheConfig(cacheNames = "yxNotice")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxNoticeServiceImpl extends BaseServiceImpl<YxNoticeMapper, YxNotice> implements YxNoticeService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxNoticeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxNotice> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxNoticeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxNotice> queryAll(YxNoticeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxNotice.class, criteria));
    }


    @Override
    public void download(List<YxNoticeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxNoticeDto yxNotice : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("图片", yxNotice.getNoticeImage());
            map.put("内容", yxNotice.getNoticeContent());
            map.put("跳转链接", yxNotice.getLinkUrl());
            map.put("状态：0：启用，1：禁用", yxNotice.getStatus());
            map.put("是否删除（0：未删除，1：已删除）", yxNotice.getDelFlag());
            map.put("创建人", yxNotice.getCreateUserId());
            map.put("修改人", yxNotice.getUpdateUserId());
            map.put("创建时间", yxNotice.getCreateTime());
            map.put("更新时间", yxNotice.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
