/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.service.impl;

import co.yixiang.modules.shopConfig.domain.YxMerchantsSettlement;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shopConfig.service.YxMerchantsSettlementService;
import co.yixiang.modules.shopConfig.service.dto.YxMerchantsSettlementDto;
import co.yixiang.modules.shopConfig.service.dto.YxMerchantsSettlementQueryCriteria;
import co.yixiang.modules.shopConfig.service.mapper.YxMerchantsSettlementMapper;
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
//@CacheConfig(cacheNames = "yxMerchantsSettlement")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxMerchantsSettlementServiceImpl extends BaseServiceImpl<YxMerchantsSettlementMapper, YxMerchantsSettlement> implements YxMerchantsSettlementService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxMerchantsSettlementQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxMerchantsSettlement> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxMerchantsSettlementDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxMerchantsSettlement> queryAll(YxMerchantsSettlementQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxMerchantsSettlement.class, criteria));
    }


    @Override
    public void download(List<YxMerchantsSettlementDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxMerchantsSettlementDto yxMerchantsSettlement : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("公司名", yxMerchantsSettlement.getCompanyName());
            map.put("联系人", yxMerchantsSettlement.getContactsName());
            map.put("联系电话", yxMerchantsSettlement.getPhone());
            map.put("联系地址", yxMerchantsSettlement.getAddress());
            map.put("说明", yxMerchantsSettlement.getExplain());
            map.put("备注", yxMerchantsSettlement.getRemark());
            map.put("状态：0：待联系，1：有意向，2：已拒绝", yxMerchantsSettlement.getStatus());
            map.put("是否删除（0：未删除，1：已删除）", yxMerchantsSettlement.getDelFlag());
            map.put("创建人", yxMerchantsSettlement.getCreateUserId());
            map.put("修改人", yxMerchantsSettlement.getUpdateUserId());
            map.put("创建时间", yxMerchantsSettlement.getCreateTime());
            map.put("更新时间", yxMerchantsSettlement.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
