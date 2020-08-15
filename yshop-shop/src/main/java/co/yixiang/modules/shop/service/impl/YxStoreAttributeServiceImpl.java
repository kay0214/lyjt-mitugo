/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxStoreAttribute;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxStoreAttributeService;
import co.yixiang.modules.shop.service.dto.YxStoreAttributeDto;
import co.yixiang.modules.shop.service.dto.YxStoreAttributeQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxStoreAttributeMapper;
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
* @author huiy
* @date 2020-08-14
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxStoreAttribute")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreAttributeServiceImpl extends BaseServiceImpl<YxStoreAttributeMapper, YxStoreAttribute> implements YxStoreAttributeService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreAttributeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxStoreAttribute> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxStoreAttributeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreAttribute> queryAll(YxStoreAttributeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreAttribute.class, criteria));
    }


    @Override
    public void download(List<YxStoreAttributeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreAttributeDto yxStoreAttribute : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("店铺id", yxStoreAttribute.getStoreId());
            map.put("属性值1", yxStoreAttribute.getAttributeValue1());
            map.put("属性值2", yxStoreAttribute.getAttributeValue2());
            map.put("属性类型：0：营业时间，1：店铺服务", yxStoreAttribute.getAttributeType());
            map.put("是否删除（0：未删除，1：已删除）", yxStoreAttribute.getDelFlag());
            map.put("创建人", yxStoreAttribute.getCreateUserId());
            map.put("修改人", yxStoreAttribute.getUpdateUserId());
            map.put("创建时间", yxStoreAttribute.getCreateTime());
            map.put("更新时间", yxStoreAttribute.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
