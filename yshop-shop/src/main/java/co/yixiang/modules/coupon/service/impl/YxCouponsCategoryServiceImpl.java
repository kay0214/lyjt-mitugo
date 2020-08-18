/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsCategoryMapper;
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
//@CacheConfig(cacheNames = "yxCouponsCategory")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponsCategoryServiceImpl extends BaseServiceImpl<YxCouponsCategoryMapper, YxCouponsCategory> implements YxCouponsCategoryService {

    private final IGenerator generator;

    /**
     * 写入 ()
     * @param yxCouponsCategory
     * @return
     */
    @Override
    public int insCouponCate(YxCouponsCategory yxCouponsCategory) {
        return baseMapper.insert(yxCouponsCategory);
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsCategoryQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);

        PageInfo<YxCouponsCategory> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponsCategoryDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponsCategory> queryAll(YxCouponsCategoryQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponsCategory.class, criteria));
    }


    @Override
    public void download(List<YxCouponsCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponsCategoryDto yxCouponsCategory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("父id", yxCouponsCategory.getPid());
            map.put("分类名称", yxCouponsCategory.getCateName());
            map.put("排序", yxCouponsCategory.getSort());
            map.put("是否推荐. 0:不推荐, 1:推荐", yxCouponsCategory.getIsShow());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponsCategory.getDelFlag());
            map.put("创建人", yxCouponsCategory.getCreateUserId());
            map.put("修改人", yxCouponsCategory.getUpdateUserId());
            map.put("创建时间", yxCouponsCategory.getCreateTime());
            map.put("更新时间", yxCouponsCategory.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
