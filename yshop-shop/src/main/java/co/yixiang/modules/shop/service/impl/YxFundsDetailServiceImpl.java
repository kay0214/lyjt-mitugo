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
import co.yixiang.modules.shop.domain.YxFundsDetail;
import co.yixiang.modules.shop.service.YxFundsDetailService;
import co.yixiang.modules.shop.service.dto.YxFundsDetailDto;
import co.yixiang.modules.shop.service.dto.YxFundsDetailQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxFundsDetailMapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author huiy
* @date 2020-08-19
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxFundsDetailServiceImpl extends BaseServiceImpl<YxFundsDetailMapper, YxFundsDetail> implements YxFundsDetailService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(YxFundsDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxFundsDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxFundsDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxFundsDetail> queryAll(YxFundsDetailQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxFundsDetail.class, criteria));
    }
}
