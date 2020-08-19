package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shop.domain.YxPointDetail;
import co.yixiang.modules.shop.service.YxPointDetailService;
import co.yixiang.modules.shop.service.dto.YxPointDetailDto;
import co.yixiang.modules.shop.service.dto.YxPointDetailQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxPointDetailMapper;
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
//@CacheConfig(cacheNames = "yxPointDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxPointDetailServiceImpl extends BaseServiceImpl<YxPointDetailMapper, YxPointDetail> implements YxPointDetailService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxPointDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxPointDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxPointDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxPointDetail> queryAll(YxPointDetailQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxPointDetail.class, criteria));
    }
}
