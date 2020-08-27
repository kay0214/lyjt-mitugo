/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.YxCouponOrderDetail;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsMapper;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.utils.FileUtil;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author huiy
 * @date 2020-08-14
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCoupons")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponsServiceImpl extends BaseServiceImpl<YxCouponsMapper, YxCoupons> implements YxCouponsService {

    private final IGenerator generator;

    @Autowired
    private YxImageInfoService yxImageInfoService;

    @Autowired
    private YxCouponsCategoryService yxCouponsCategoryService;
    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCoupons> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YxCouponsDto> yxCouponsDtoList = generator.convert(page.getList(), YxCouponsDto.class);
        if (yxCouponsDtoList.size() > 0) {
            // 查询缩略图和幻灯片
            for (YxCouponsDto yxCouponsDto : yxCouponsDtoList) {

                YxCouponsCategory couponsCategory = yxCouponsCategoryService.getOne(new QueryWrapper<YxCouponsCategory>()
                        .eq("del_flag", 0).eq("id", yxCouponsDto.getCouponCategory()));
                if (couponsCategory != null) {
                    yxCouponsDto.setCouponCategoryName(couponsCategory.getCateName());
                }

                // 查询缩略图图片是否存在
                QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
                imageInfoQueryWrapper.lambda()
                        .and(type -> type.eq(YxImageInfo::getTypeId, yxCouponsDto.getId()))
                        .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC))
                        .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                        .and(del -> del.eq(YxImageInfo::getDelFlag, false));
                YxImageInfo imageInfo = yxImageInfoService.getOne(imageInfoQueryWrapper);
                if (imageInfo != null) {
                    yxCouponsDto.setImage(imageInfo.getImgUrl());
                }

                // 查询幻灯片
                QueryWrapper<YxImageInfo> sliderImageInfoQueryWrapper = new QueryWrapper<>();
                sliderImageInfoQueryWrapper.lambda().select(YxImageInfo::getImgUrl)
                        .and(type -> type.eq(YxImageInfo::getTypeId, yxCouponsDto.getId()))
                        .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_ROTATION1))
                        .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                        .and(del -> del.eq(YxImageInfo::getDelFlag, false));

                List<YxImageInfo> sliderImageInfoList = yxImageInfoService.list(sliderImageInfoQueryWrapper);

                if (sliderImageInfoList.size() > 0) {
                    List<String> sliderList = new ArrayList<>();
                    for (YxImageInfo sliderImage : sliderImageInfoList) {
                        sliderList.add(sliderImage.getImgUrl());
                    }
                    yxCouponsDto.setSliderImage(sliderList);
                }
            }
        }
        map.put("content", yxCouponsDtoList);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCoupons> queryAll(YxCouponsQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCoupons.class, criteria));
    }


    @Override
    public void download(List<YxCouponsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponsDto yxCoupons : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("卡券编号", yxCoupons.getCouponNum());
            map.put("卡券名称", yxCoupons.getCouponName());
            map.put("卡券类型;1:代金券, 2:折扣券, 3:满减券", yxCoupons.getCouponType());
            map.put("卡券所属分类", yxCoupons.getCouponCategory());
            map.put("代金券面额, coupon_type为1时使用", yxCoupons.getDenomination());
            map.put("折扣券折扣率, coupon_type为2时使用", yxCoupons.getDiscount());
            map.put("使用门槛, coupon_type为3时使用", yxCoupons.getThreshold());
            map.put("优惠金额, coupon_type为3时使用", yxCoupons.getDiscountAmount());
            map.put("销售价格", yxCoupons.getSellingPrice());
            map.put("原价", yxCoupons.getOriginalPrice());
            map.put("平台结算价", yxCoupons.getSettlementPrice());
            map.put("佣金", yxCoupons.getCommission());
            map.put("每人限购数量", yxCoupons.getQuantityLimit());
            map.put("库存", yxCoupons.getInventory());
            map.put("销量", yxCoupons.getSales());
            map.put("虚拟销量", yxCoupons.getFicti());
            map.put("核销次数", yxCoupons.getWriteOff());
            map.put("有效期始", yxCoupons.getExpireDateStart());
            map.put("有效期止", yxCoupons.getExpireDateEnd());
            map.put("热门优惠; 1:是, 0否", yxCoupons.getIsHot());
            map.put("状态（0：未上架，1：上架）", yxCoupons.getIsShow());
            map.put("过期退 0:不支持 1支持", yxCoupons.getOuttimeRefund());
            map.put("免预约 0:不支持 1支持", yxCoupons.getNeedOrder());
            map.put("随时退 0:不支持 1支持", yxCoupons.getAwaysRefund());
            map.put("使用条件 描述", yxCoupons.getUseCondition());
            map.put("可用时间始", yxCoupons.getAvailableTimeStart());
            map.put("可用时间止", yxCoupons.getAvailableTimeEnd());
            map.put("是否删除（0：未删除，1：已删除）", yxCoupons.getDelFlag());
            map.put("创建人 根据创建人关联店铺", yxCoupons.getCreateUserId());
            map.put("修改人", yxCoupons.getUpdateUserId());
            map.put("创建时间", yxCoupons.getCreateTime());
            map.put("更新时间", yxCoupons.getUpdateTime());
            map.put("卡券详情", yxCoupons.getContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据核销码查询卡券信息
     *
     * @param verifyCode
     * @param uid
     * @return
     */
    @Override
    public YxCouponsDto getCouponByVerifyCode(String verifyCode, int uid) {
        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        YxCouponOrderDetail yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        if (null == yxCouponOrderDetail) {
            yxCouponsDto.setStatus(-1);
            yxCouponsDto.setStatusDesc("无效卡券");
            return yxCouponsDto;
        }
        // 查询优惠券信息
        YxCoupons yxCoupons = this.getById(yxCouponOrderDetail.getCouponId());
        if (null == yxCoupons) {
            yxCouponsDto.setStatus(-2);
            yxCouponsDto.setStatusDesc("卡券已失效");
            return yxCouponsDto;
        }
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            yxCouponsDto.setStatus(-3);
            yxCouponsDto.setStatusDesc("当前卡券已达核销上限");
        }
        // 判断卡券状态
        switch (yxCouponOrderDetail.getStatus()) {
            case 0:
                yxCouponsDto.setStatus(-4);
                yxCouponsDto.setStatusDesc("待支付");
                break;
            case 1:
                yxCouponsDto.setStatus(-5);
                yxCouponsDto.setStatusDesc("已过期");
                break;
            case 2:
                yxCouponsDto.setStatus(-6);
                yxCouponsDto.setStatusDesc("待发放");
                break;
            case 3:
                yxCouponsDto.setStatus(-7);
                yxCouponsDto.setStatusDesc("支付失败");
                break;
            case 4:
                yxCouponsDto.setStatus(4);
                yxCouponsDto.setStatusDesc("待使用");
                break;
            case 5:
                yxCouponsDto.setStatus(5);
                yxCouponsDto.setStatusDesc("已使用");
                break;
            case 6:
                yxCouponsDto.setStatus(-8);
                yxCouponsDto.setStatusDesc("已核销");
                break;
            case 7:
                yxCouponsDto.setStatus(-9);
                yxCouponsDto.setStatusDesc("退款中");
                break;
            case 8:
                yxCouponsDto.setStatus(-10);
                yxCouponsDto.setStatusDesc("已退款");
                break;
            case 9:
                yxCouponsDto.setStatus(-11);
                yxCouponsDto.setStatusDesc("退款驳回");
                break;
            default:
                yxCouponsDto.setStatus(-12);
                yxCouponsDto.setStatusDesc("未知状态");
                break;
        }
        // 判断有效期
        LocalDateTime expireDateStart = yxCoupons.getExpireDateStart().toLocalDateTime();
        LocalDateTime expireDateEnd = yxCoupons.getExpireDateEnd().toLocalDateTime();
        if (expireDateStart.isBefore(LocalDateTime.now()) || expireDateEnd.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatus(-12);
            yxCouponsDto.setStatusDesc("已失效");
            return yxCouponsDto;
        }
        return yxCouponsDto;
    }
}
