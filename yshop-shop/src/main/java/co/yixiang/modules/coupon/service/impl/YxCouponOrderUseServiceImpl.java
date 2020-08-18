/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.coupon.service.YxCouponOrderUseService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponOrderUseMapper;
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
//@CacheConfig(cacheNames = "yxCouponOrderUse")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponOrderUseServiceImpl extends BaseServiceImpl<YxCouponOrderUseMapper, YxCouponOrderUse> implements YxCouponOrderUseService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderUseQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponOrderUse> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponOrderUseDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponOrderUse> queryAll(YxCouponOrderUseQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrderUse.class, criteria));
    }


    @Override
    public void download(List<YxCouponOrderUseDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponOrderUseDto yxCouponOrderUse : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", yxCouponOrderUse.getOrderId());
            map.put("核销商铺id", yxCouponOrderUse.getStoreId());
            map.put("店铺名称", yxCouponOrderUse.getStoreName());
            map.put("核销次数", yxCouponOrderUse.getUsedCount());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponOrderUse.getDelFlag());
            map.put("创建人 根据创建人关联店铺", yxCouponOrderUse.getCreateUserId());
            map.put("修改人", yxCouponOrderUse.getUpdateUserId());
            map.put("创建时间", yxCouponOrderUse.getCreateTime());
            map.put("更新时间", yxCouponOrderUse.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
