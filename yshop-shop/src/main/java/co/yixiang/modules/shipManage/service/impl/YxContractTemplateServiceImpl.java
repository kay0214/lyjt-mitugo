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
import co.yixiang.modules.shipManage.domain.YxContractTemplate;
import co.yixiang.modules.shipManage.service.YxContractTemplateService;
import co.yixiang.modules.shipManage.service.dto.YxContractTemplateDto;
import co.yixiang.modules.shipManage.service.dto.YxContractTemplateQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxContractTemplateMapper;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.service.UserService;
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
 * @author nxl
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxContractTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxContractTemplateServiceImpl extends BaseServiceImpl<YxContractTemplateMapper, YxContractTemplate> implements YxContractTemplateService {

    private final IGenerator generator;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxContractTemplateQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxContractTemplate> page = new PageInfo<>(queryAll(criteria));

        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxContractTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.lambda().eq(YxContractTemplate::getDelFlag, 0);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxContractTemplate::getCreateUserId, criteria.getChildUser());
        }
        if (StringUtils.isNotBlank(criteria.getTempName())) {
            queryWrapper.lambda().eq(YxContractTemplate::getTempName, criteria.getTempName());
        }
        IPage<YxContractTemplate> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxContractTemplateDto> resultList = generator.convert(ipage.getRecords(), YxContractTemplateDto.class);
        for (YxContractTemplateDto item : resultList) {
            User user = this.userService.getById(item.getCreateUserId());
            if (null != user) {
                item.setCreateUsername(user.getUsername());
                item.setCreateNickname(user.getNickName());
            }
        }

        map.put("content", resultList);
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxContractTemplate> queryAll(YxContractTemplateQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxContractTemplate.class, criteria));
    }


    @Override
    public void download(List<YxContractTemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxContractTemplateDto yxContractTemplate : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("模板名称", yxContractTemplate.getTempName());
            map.put("模板文件地址", yxContractTemplate.getFilePath());
            map.put("是否删除（0：未删除，1：已删除）", yxContractTemplate.getDelFlag());
            map.put("创建人", yxContractTemplate.getCreateUserId());
            map.put("修改人", yxContractTemplate.getUpdateUserId());
            map.put("创建时间", yxContractTemplate.getCreateTime());
            map.put("更新时间", yxContractTemplate.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
