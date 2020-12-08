/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxNowRate;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxNowRateService;
import co.yixiang.modules.shop.service.dto.YxNowRateDto;
import co.yixiang.modules.shop.service.dto.YxNowRateQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxNowRateMapper;
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
//@CacheConfig(cacheNames = "yxNowRate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxNowRateServiceImpl extends BaseServiceImpl<YxNowRateMapper, YxNowRate> implements YxNowRateService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxNowRateQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxNowRate> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxNowRateDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxNowRate> queryAll(YxNowRateQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxNowRate.class, criteria));
    }


    @Override
    public void download(List<YxNowRateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxNowRateDto yxNowRate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型：0:商品购买 1:本地生活", yxNowRate.getRateType());
            map.put("关联订单id", yxNowRate.getOrderId());
            map.put("购物车id", yxNowRate.getCartId());
            map.put("商品id", yxNowRate.getProductId());
            map.put("平台抽成", yxNowRate.getFundsRate());
            map.put("分享人", yxNowRate.getShareRate());
            map.put("分享人上级", yxNowRate.getShareParentRate());
            map.put("推荐人", yxNowRate.getParentRate());
            map.put("商户", yxNowRate.getMerRate());
            map.put("合伙人", yxNowRate.getPartnerRate());
            map.put("拉新池", yxNowRate.getReferenceRate());
            map.put("是否删除（0：未删除，1：已删除）", yxNowRate.getDelFlag());
            map.put("创建人", yxNowRate.getCreateUserId());
            map.put("修改人", yxNowRate.getUpdateUserId());
            map.put("创建时间", yxNowRate.getCreateTime());
            map.put("更新时间", yxNowRate.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
