package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.CouponsCategoryRequest;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsCategoryMapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
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

    @Autowired
    private YxCouponsCategoryMapper yxCouponsCategoryMapper;

    /**
     * 写入 ()
     * @param yxCouponsCategory
     * @return
     */
    @Override
    public int insCouponCate(YxCouponsCategory yxCouponsCategory) {
        return baseMapper.insert(yxCouponsCategory);
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getAllList(CouponsCategoryRequest request) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxCouponsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select()
                .and(StringUtils.isNoneBlank(request.getCateName()), cateName -> cateName.like(YxCouponsCategory::getCateName, request.getCateName()))
                .orderByDesc(YxCouponsCategory::getId)
                .orderByAsc(YxCouponsCategory::getSort);
        List<YxCouponsCategory> categoryList = baseMapper.selectList(queryWrapper);
//        Paging<YxCouponsCategory> yxCouponsCategoryIPage = baseMapper.selectPage();
        map.put("content", generator.convert(categoryList, YxCouponsCategoryDto.class));
        map.put("totalElements", categoryList.size());
        return map;
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
