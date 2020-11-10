/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupon.domain.*;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsMapper;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.YxCustomizeRateService;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.YxSystemAttachmentService;
import co.yixiang.modules.shop.service.mapper.UserSysMapper;
import co.yixiang.modules.shop.service.mapper.YxStoreInfoMapper;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
 * @author liusy
 * @date 2020-08-31
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
    @Autowired
    private YxCouponsMapper yxCouponsMapper;
    @Autowired
    private UserSysMapper userSysMapper;
    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;
    @Autowired
    private YxSystemAttachmentService yxSystemAttachmentService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxCoupons> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxCoupons> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildStoreId() || criteria.getChildStoreId().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxCoupons::getStoreId, criteria.getChildStoreId()).eq(YxCoupons::getDelFlag, 0);
        }

        if (StringUtils.isNotBlank(criteria.getCouponName())) {
            queryWrapper.lambda().like(YxCoupons::getCouponName, criteria.getCouponName());
        }
        if (null != criteria.getCouponType()) {
            queryWrapper.lambda().eq(YxCoupons::getCouponType, criteria.getCouponType());
        }
        if (null != criteria.getIsShow()) {
            queryWrapper.lambda().eq(YxCoupons::getIsShow, criteria.getIsShow());
        }
        if (null != criteria.getIsHot()) {
            queryWrapper.lambda().eq(YxCoupons::getIsHot, criteria.getIsHot());
        }
        if (null != criteria.getCouponCategory()) {
            queryWrapper.lambda().eq(YxCoupons::getCouponCategory, criteria.getCouponCategory());
        }
        if (StringUtils.isNotBlank(criteria.getMerUsername())) {
            User user = this.userSysMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getMerUsername()));
            if (null == user) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            YxStoreInfo yxStoreInfo = this.yxStoreInfoMapper.selectOne(new QueryWrapper<YxStoreInfo>().lambda().eq(YxStoreInfo::getMerId, user.getId()));
            if (null == yxStoreInfo) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().eq(YxCoupons::getStoreId, yxStoreInfo.getId());
        }
        IPage<YxCoupons> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YxCouponsDto> yxCouponsDtoList = generator.convert(ipage.getRecords(), YxCouponsDto.class);
        if (yxCouponsDtoList.size() > 0) {
            // 查询缩略图和幻灯片
            for (YxCouponsDto yxCouponsDto : yxCouponsDtoList) {

                YxCouponsCategory couponsCategory = yxCouponsCategoryService.getOne(new QueryWrapper<YxCouponsCategory>()
                        .eq("del_flag", 0).eq("id", yxCouponsDto.getCouponCategory()));
                if (couponsCategory != null) {
                    yxCouponsDto.setCouponCategoryName(couponsCategory.getCateName());
                }

                // 查询小视频是否存在
                QueryWrapper<YxImageInfo> videoWrapper = new QueryWrapper<>();
                videoWrapper.lambda()
                        .and(type -> type.eq(YxImageInfo::getTypeId, yxCouponsDto.getId()))
                        .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_VIDEO))
                        .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                        .and(del -> del.eq(YxImageInfo::getDelFlag, false));
                YxImageInfo video = yxImageInfoService.getOne(videoWrapper);
                if (video != null) {
                    yxCouponsDto.setVideo(video.getImgUrl());
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
                // 自定义分佣模式（0：按平台，1：不分佣，2：自定义分佣）
                if (2 == yxCouponsDto.getCustomizeType()) {
                    YxCustomizeRate yxCustomizeRate = this.yxCustomizeRateService.getOne(new QueryWrapper<YxCustomizeRate>().lambda()
                            .eq(YxCustomizeRate::getRateType, 0)
                            .eq(YxCustomizeRate::getLinkId, yxCouponsDto.getId())
                            .eq(YxCustomizeRate::getDelFlag, 0));
                    if (null != yxCustomizeRate) {
                        yxCustomizeRate.setFundsRate(yxCustomizeRate.getFundsRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setShareRate(yxCustomizeRate.getShareRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setShareParentRate(yxCustomizeRate.getShareParentRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setParentRate(yxCustomizeRate.getParentRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setPartnerRate(yxCustomizeRate.getPartnerRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setReferenceRate(yxCustomizeRate.getReferenceRate().multiply(new BigDecimal("100")));
                        yxCustomizeRate.setMerRate(yxCustomizeRate.getMerRate().multiply(new BigDecimal("100")));
                        yxCouponsDto.setYxCustomizeRate(yxCustomizeRate);
                    }
                }
            }
        }
        map.put("content", yxCouponsDtoList);
        map.put("totalElements", ipage.getTotal());
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
            map.put("卡券所属商铺", yxCoupons.getStoreId());
            map.put("卡券简介", yxCoupons.getCouponInfo());
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
        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getCreateUserId().equals(uid)) {
            yxCouponsDto.setStatus(-3);
            yxCouponsDto.setStatusDesc("非本商户卡券");
            return yxCouponsDto;
        }
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            yxCouponsDto.setStatus(-4);
            yxCouponsDto.setStatusDesc("当前卡券已达核销上限");
        }
        // 判断卡券状态
        switch (yxCouponOrderDetail.getStatus()) {
            case 0:
                yxCouponsDto.setStatus(-5);
                yxCouponsDto.setStatusDesc("待支付");
                break;
            case 1:
                yxCouponsDto.setStatus(-6);
                yxCouponsDto.setStatusDesc("已过期");
                break;
            case 2:
                yxCouponsDto.setStatus(-7);
                yxCouponsDto.setStatusDesc("待发放");
                break;
            case 3:
                yxCouponsDto.setStatus(-8);
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
                yxCouponsDto.setStatus(-9);
                yxCouponsDto.setStatusDesc("已核销");
                break;
            case 7:
                yxCouponsDto.setStatus(-10);
                yxCouponsDto.setStatusDesc("退款中");
                break;
            case 8:
                yxCouponsDto.setStatus(-11);
                yxCouponsDto.setStatusDesc("已退款");
                break;
            case 9:
                yxCouponsDto.setStatus(-12);
                yxCouponsDto.setStatusDesc("退款驳回");
                break;
            default:
                yxCouponsDto.setStatus(-13);
                yxCouponsDto.setStatusDesc("未知状态");
                break;
        }
        // 判断有效期
        LocalDateTime expireDateStart = yxCoupons.getExpireDateStart().toLocalDateTime();
        LocalDateTime expireDateEnd = yxCoupons.getExpireDateEnd().toLocalDateTime();
        if (expireDateStart.isBefore(LocalDateTime.now()) || expireDateEnd.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatus(-14);
            yxCouponsDto.setStatusDesc("已失效");
            return yxCouponsDto;
        }
        return yxCouponsDto;
    }

    /**
     * 更新卡券销量
     *
     * @param couponId
     * @param totalNum
     * @return
     */
    @Override
    public boolean updateCancelNoPayOrder(Integer couponId, Integer totalNum) {
        // 恢复销量
        int count = this.yxCouponsMapper.updateMinusSales(couponId, totalNum);
        return count > 0;
    }

    /**
     * 新增卡券处理
     *
     * @param request
     * @return
     */
    @Override
    public boolean createCoupons(CouponAddRequest request) {
        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda()
                .and(obj1 -> obj1.eq(YxCouponsCategory::getId, request.getCouponCategory()))
                .and(obj2 -> obj2.eq(YxCouponsCategory::getDelFlag, 0));
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null) {
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        String couponNum = OrderUtil.orderSn();

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(couponNum0bj -> couponNum0bj.eq(YxCoupons::getCouponNum, couponNum))
                .and(delFlag -> delFlag.eq(YxCoupons::getDelFlag, 0));
        int countCoupons = count(yxCouponsQueryWrapper);
        if (countCoupons > 0) {
            throw new BadRequestException("卡券核销码已存在, 请联系开发人员");
        }

        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        // 添加以后不可修改所属商铺
        yxCoupons.setCouponNum(couponNum);
        yxCoupons.setIsShow(0);
        yxCoupons.setIsHot(0);
        yxCoupons.setDelFlag(0);
        yxCoupons.setCreateUserId(request.getCreateUser());
        yxCoupons.setCreateTime(DateTime.now().toTimestamp());
        yxCoupons.setUpdateUserId(request.getCreateUser());
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        yxCoupons.setExpireDateStart(DateUtils.getDayStart(yxCoupons.getExpireDateStart()));
        yxCoupons.setExpireDateEnd(DateUtils.getDayEnd(yxCoupons.getExpireDateEnd()));
        boolean saveStatus = this.save(yxCoupons);
        if (saveStatus) {
            couponImg(yxCoupons.getId(), request.getVideo(), request.getImage(), request.getSliderImage(), request.getCreateUser());
        }
        return saveStatus;
    }


    /**
     * 缩略图操作
     *
     * @param typeId
     * @param video       小视频
     * @param imgPath     缩略图
     * @param sliderPath  缩略图
     * @param loginUserId
     */
    @Override
    public void couponImg(Integer typeId, String video, String imgPath, String sliderPath, Integer loginUserId) {
        // 处理视频
        if (StringUtils.isNotBlank(video)) {
            List<YxImageInfo> videoList = this.yxImageInfoService.list(new QueryWrapper<YxImageInfo>().lambda()
                    .eq(YxImageInfo::getTypeId, typeId)
                    .eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_VIDEO)
                    .eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS)
                    .eq(YxImageInfo::getDelFlag, 0));
            if (null != videoList || videoList.size() > 0) {
                // 存在视频数据的话统一更新删除状态为1、逻辑删除
                for (YxImageInfo item : videoList) {
                    item.setDelFlag(1);
                    item.setUpdateUserId(loginUserId);
                    item.setUpdateTime(DateTime.now().toTimestamp());
                    yxImageInfoService.updateById(item);
                }
            }
            // 新上传视频存储
            YxImageInfo insertVideo = new YxImageInfo();
            insertVideo.setTypeId(typeId);
            insertVideo.setImgCategory(ShopConstants.IMG_CATEGORY_VIDEO);
            insertVideo.setImgType(LocalLiveConstants.IMG_TYPE_COUPONS);
            insertVideo.setImgUrl(video);
            insertVideo.setDelFlag(0);
            insertVideo.setCreateUserId(loginUserId);
            insertVideo.setCreateTime(DateTime.now().toTimestamp());
            insertVideo.setUpdateUserId(loginUserId);
            insertVideo.setUpdateTime(DateTime.now().toTimestamp());
            this.yxImageInfoService.save(insertVideo);
        }

        if (StringUtils.isNotBlank(imgPath)) {
            // 查询缩略图图片是否存在(已存在则删除)
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda()
                    .and(type -> type.eq(YxImageInfo::getTypeId, typeId))
                    .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC))
                    .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                    .and(del -> del.eq(YxImageInfo::getDelFlag, false));

            List<YxImageInfo> imageInfoList = yxImageInfoService.list(imageInfoQueryWrapper);

            if (imageInfoList.size() > 0) {
                // 删除已存在的图片
                for (YxImageInfo imageInfo : imageInfoList) {
                    YxImageInfo delImageInfo = new YxImageInfo();
                    delImageInfo.setId(imageInfo.getId());
                    delImageInfo.setDelFlag(1);
                    delImageInfo.setUpdateUserId(loginUserId);
                    delImageInfo.setUpdateTime(DateTime.now().toTimestamp());
                    yxImageInfoService.updateById(delImageInfo);
                }
            }

            // 写入分类对应的图片关联表
            YxImageInfo imageInfo = new YxImageInfo();
            imageInfo.setTypeId(typeId);
            // 卡券分类 img_type 为 5
            imageInfo.setImgType(LocalLiveConstants.IMG_TYPE_COUPONS);
            imageInfo.setImgCategory(ShopConstants.IMG_CATEGORY_PIC);
            imageInfo.setImgUrl(imgPath);
            imageInfo.setDelFlag(0);
            imageInfo.setCreateUserId(loginUserId);
            imageInfo.setUpdateUserId(loginUserId);
            imageInfo.setCreateTime(DateTime.now().toTimestamp());
            imageInfo.setUpdateTime(DateTime.now().toTimestamp());
            yxImageInfoService.save(imageInfo);
        }

        if (StringUtils.isNotBlank(sliderPath)) {
            // 幻灯片
            QueryWrapper<YxImageInfo> sliderImageInfoQueryWrapper = new QueryWrapper<>();
            sliderImageInfoQueryWrapper.lambda()
                    .and(type -> type.eq(YxImageInfo::getTypeId, typeId))
                    .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_ROTATION1))
                    .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                    .and(del -> del.eq(YxImageInfo::getDelFlag, false));

            List<YxImageInfo> sliderImageInfoList = yxImageInfoService.list(sliderImageInfoQueryWrapper);

            List<YxImageInfo> delSliderImageInfoList = new ArrayList<YxImageInfo>();
            if (sliderImageInfoList.size() > 0) {
                // 删除已存在的图片
                for (YxImageInfo imageInfo : sliderImageInfoList) {
                    YxImageInfo delImageInfo = new YxImageInfo();
                    delImageInfo.setId(imageInfo.getId());
                    delImageInfo.setDelFlag(1);
                    delImageInfo.setUpdateUserId(loginUserId);
                    delImageInfo.setUpdateTime(DateTime.now().toTimestamp());
                    delSliderImageInfoList.add(delImageInfo);
                }
                yxImageInfoService.updateBatchById(delSliderImageInfoList, delSliderImageInfoList.size());
            }

            List<YxImageInfo> yxImageInfoList = new ArrayList<YxImageInfo>();
            String[] images = sliderPath.split(",");
            if (images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    YxImageInfo yxImageInfos = new YxImageInfo();
                    yxImageInfos.setTypeId(typeId);
                    yxImageInfos.setImgType(LocalLiveConstants.IMG_TYPE_COUPONS);
                    yxImageInfos.setImgCategory(ShopConstants.IMG_CATEGORY_ROTATION1);
                    yxImageInfos.setImgUrl(images[i]);
                    yxImageInfos.setDelFlag(0);
                    yxImageInfos.setUpdateUserId(loginUserId);
                    yxImageInfos.setCreateUserId(loginUserId);
                    yxImageInfoList.add(yxImageInfos);
                }
                yxImageInfoService.saveBatch(yxImageInfoList, yxImageInfoList.size());
            }
        }
    }

    /**
     * 更新卡券
     *
     * @param request
     * @return
     */
    @Override
    public boolean updateCoupons(CouponModifyRequest request) {

        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda().eq(YxCouponsCategory::getId, request.getCouponCategory()).eq(YxCouponsCategory::getDelFlag, 0).eq(YxCouponsCategory::getIsShow, 1);
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null) {
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        yxCoupons.setUpdateUserId(request.getCreateUser());
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        yxCoupons.setExpireDateStart(DateUtils.getDayStart(yxCoupons.getExpireDateStart()));
        yxCoupons.setExpireDateEnd(DateUtils.getDayEnd(yxCoupons.getExpireDateEnd()));
        if (null == yxCoupons.getFicti()) {
            yxCoupons.setFicti(0);
        }
        // 虚拟销量设为null不更新数据库
        yxCoupons.setSales(null);
        boolean updateStatus = updateById(yxCoupons);
        if (updateStatus) {
            // 存储相关图片
            couponImg(yxCoupons.getId(), request.getVideo(), request.getImage(), request.getSliderImage(), request.getCreateUser());

            // 卡券信息发生变更, 删除卡券海报
            String logo = String.valueOf(yxCoupons.getId()) + "_" + String.valueOf(request.getCreateUser()) + "_";
            String couponWapImage = logo + "coupon_routine_product_detail_wap.jpg";
            String couponSpreadImage = logo + "coupon_routine_product_user_spread.jpg";
            List<YxSystemAttachment> systemAttachmentWapImageList = yxSystemAttachmentService.list(new QueryWrapper<YxSystemAttachment>().lambda().eq(YxSystemAttachment::getName, couponWapImage));
            if (systemAttachmentWapImageList != null && systemAttachmentWapImageList.size() > 0) {
                systemAttachmentWapImageList.forEach(item -> {
                    yxSystemAttachmentService.removeById(item.getAttId());
                });
            }
            List<YxSystemAttachment> systemAttachmentSpreadImageList = yxSystemAttachmentService.list(new QueryWrapper<YxSystemAttachment>().lambda().eq(YxSystemAttachment::getName, couponSpreadImage));
            if (systemAttachmentSpreadImageList != null && systemAttachmentSpreadImageList.size() > 0) {
                systemAttachmentSpreadImageList.forEach(items -> {
                    yxSystemAttachmentService.removeById(items.getAttId());
                });
            }
        }
        return updateStatus;
    }

    /**
     * 修改分佣比例
     *
     * @param request
     */
    @Override
    public void updateRate(CouponModifyRateRequest request) {
        YxCoupons yxCoupons = new YxCoupons();
        yxCoupons.setId(request.getId());
        yxCoupons.setCustomizeType(request.getCustomizeType());
        yxCoupons.setUpdateUserId(request.getCreateUser());
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        if (2 == request.getCustomizeType()) {
            YxCustomizeRate yxCustomizeRate = request.getYxCustomizeRate();
            yxCustomizeRate.setLinkId(request.getId());
            // 0：本地生活，1：商城
            yxCustomizeRate.setRateType(0);
            // 存入操作人
            yxCustomizeRate.setCreateUserId(request.getCreateUser());
            boolean rateResult = yxCustomizeRateService.saveOrUpdateRate(yxCustomizeRate);
            if (!rateResult) {
                throw new BadRequestException("请核对分佣比例");
            }
        }
        this.updateById(yxCoupons);
    }
}
