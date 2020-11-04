/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxCustomizeRate;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxCustomizeRateService;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateDto;
import co.yixiang.modules.shop.service.dto.YxCustomizeRateQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxCustomizeRateMapper;
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
    public List<YxCustomizeRate> queryAll(YxCustomizeRateQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCustomizeRate.class, criteria));
    }


    @Override
    public void download(List<YxCustomizeRateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCustomizeRateDto yxCustomizeRate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型：0：本地生活，1：商城", yxCustomizeRate.getRateType());
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
}
