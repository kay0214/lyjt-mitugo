package co.yixiang.modules.coupons.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.coupons.entity.YxCouponsCategory;
import co.yixiang.modules.coupons.mapper.YxCouponsCategoryMapper;
import co.yixiang.modules.coupons.service.YxCouponsCategoryService;
import co.yixiang.modules.coupons.web.param.YxCouponsCategoryQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsCategoryQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.utils.CateDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 本地生活, 卡券分类表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsCategoryServiceImpl extends BaseServiceImpl<YxCouponsCategoryMapper, YxCouponsCategory> implements YxCouponsCategoryService {

    @Autowired
    private YxCouponsCategoryMapper yxCouponsCategoryMapper;

    @Autowired
    private YxImageInfoService yxImageInfoService;

    @Override
    public YxCouponsCategoryQueryVo getYxCouponsCategoryById(Serializable id) throws Exception{
        return yxCouponsCategoryMapper.getYxCouponsCategoryById(id);
    }

    @Override
    public Paging<YxCouponsCategoryQueryVo> getYxCouponsCategoryPageList(YxCouponsCategoryQueryParam yxCouponsCategoryQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsCategoryQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsCategoryQueryVo> iPage = yxCouponsCategoryMapper.getYxCouponsCategoryPageList(page,yxCouponsCategoryQueryParam);
        return new Paging(iPage);
    }

    /**
     * 卡券分类
     * @return
     */
    @Override
    public List<CateDTO> getList() {
        QueryWrapper<YxCouponsCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("is_show",1).eq("del_flag",0).orderByAsc("sort");
        List<YxCouponsCategory> categoryList = baseMapper.selectList(wrapper);

        List<CateDTO> cateDTOList = new ArrayList<>();
        if (categoryList != null){
            for (YxCouponsCategory category : categoryList){
                CateDTO cateDTO = new CateDTO();
                cateDTO.setId(category.getId().longValue());
                cateDTO.setPid(category.getPid().longValue());
                cateDTO.setCateName(category.getCateName());
                YxImageInfo yxImageInfo = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", category.getId())
                        .eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS_CATEGORY).eq("img_category", ShopConstants.IMG_CATEGORY_PIC));
                if (yxImageInfo != null){
                    cateDTO.setPic(yxImageInfo.getImgUrl());
                }
                cateDTOList.add(cateDTO);
            }
        }
        return cateDTOList;
    }
}
