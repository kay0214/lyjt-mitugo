package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.CouponsCategoryRequest;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsCategoryMapper;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.dto.YxStoreCategoryDto;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private YxImageInfoService yxImageInfoService;

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
                .orderByDesc(YxCouponsCategory::getSort)
                .orderByDesc(YxCouponsCategory::getCreateTime);
        List<YxCouponsCategory> categoryList = baseMapper.selectList(queryWrapper);
        List<YxCouponsCategoryDto> list = new ArrayList<>();
        for (YxCouponsCategory item :categoryList) {

            YxCouponsCategoryDto dto = new YxCouponsCategoryDto();
            BeanUtils.copyBeanProp(dto,item);
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda()
                    .and(type -> type.eq(YxImageInfo::getTypeId, item.getId()))
                    .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC))
                    .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS_CATEGORY))
                    .and(del -> del.eq(YxImageInfo::getDelFlag, false));

            List<YxImageInfo> imageInfoList = yxImageInfoService.list(imageInfoQueryWrapper);
            if(imageInfoList!=null && imageInfoList.size()>0){
                dto.setPath(imageInfoList.get(0).getImgUrl());
            }
            list.add(dto);
        }
//        Paging<YxCouponsCategory> yxCouponsCategoryIPage = baseMapper.selectPage();
        map.put("content", list);
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
        QueryWrapper queryWrapper = QueryHelpPlus.getPredicate(YxStoreCategoryDto.class, criteria);
        queryWrapper.orderByDesc("sort","create_time");
        return baseMapper.selectList(queryWrapper);
    }


    @Override
    public Object buildTree(List<YxCouponsCategoryDto> categoryDTOS) {
        Set<YxCouponsCategoryDto> trees = new LinkedHashSet<>();
        Set<YxCouponsCategoryDto> cates= new LinkedHashSet<>();
        List<String> deptNames = categoryDTOS.stream().map(YxCouponsCategoryDto::getCateName)
                .collect(Collectors.toList());

        YxStoreCategoryDto categoryDTO = new YxStoreCategoryDto();
        Boolean isChild;
        List<YxCouponsCategory> categories = this.list();
        for (YxCouponsCategoryDto deptDTO : categoryDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
//            for (YxCouponsCategoryDto it : categoryDTOS) {
//                if (it.getPid().equals(deptDTO.getId())) {
//                    isChild = true;
//                    if (deptDTO.getChildren() == null) {
//                        deptDTO.setChildren(new ArrayList<YxStoreCategoryDto>());
//                    }
//                    deptDTO.getChildren().add(it);
//                }
//            }
            if(isChild)
                cates.add(deptDTO);
            for (YxCouponsCategory category : categories) {
                if(category.getId()==deptDTO.getPid()&&!deptNames.contains(category.getCateName())){
                    cates.add(deptDTO);
                }
            }
        }



        if (CollectionUtils.isEmpty(trees)) {
            trees = cates;
        }



        Integer totalElements = categoryDTOS!=null?categoryDTOS.size():0;

        Map map = new HashMap();
        map.put("totalElements",totalElements);
        map.put("content",CollectionUtils.isEmpty(trees)?categoryDTOS:trees);
        return map;
    }
}
