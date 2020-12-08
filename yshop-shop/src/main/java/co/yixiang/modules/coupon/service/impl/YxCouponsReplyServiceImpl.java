/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.domain.YxCouponsReply;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.YxCouponsReplyService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsReplyMapper;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponsReply")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponsReplyServiceImpl extends BaseServiceImpl<YxCouponsReplyMapper, YxCouponsReply> implements YxCouponsReplyService {

    private final IGenerator generator;

    @Autowired
    private UserService userService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsReplyQueryCriteria criteria, Pageable pageable) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxCouponsReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponsReply::getDelFlag, 0);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxCouponsReply::getMerId, criteria.getChildUser());
        }
        // 根据用户昵称查询
        if (StringUtils.isNotBlank(criteria.getNickName())) {
            User user = this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getNickName, criteria.getNickName()));
            if (null == user) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().eq(YxCouponsReply::getMerId, user.getId());
        }
        // 根据用户登录名查询
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            User user = this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getUsername()));
            if (null == user) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().eq(YxCouponsReply::getMerId, user.getId());
        }
        // 根据商品名称查询
        if (StringUtils.isNotBlank(criteria.getCouponName())) {
            List<YxCoupons> yxCoupons = this.yxCouponsService.list(new QueryWrapper<YxCoupons>().lambda().eq(YxCoupons::getCouponName, criteria.getCouponName()));
            if (null == yxCoupons || yxCoupons.size() <= 0) {
                // 未查到卡券信息的直接返回空
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            // 大部分卡券的名称没有重复的、单独判断只查到一条的单独处理
            if (yxCoupons.size() == 1) {
                queryWrapper.lambda().eq(YxCouponsReply::getCouponId, yxCoupons.get(0).getId());
            } else {
                // 查到多条卡券的用拿到id的list用in查询
                List<Integer> ids = new ArrayList<>();
                for (YxCoupons item : yxCoupons) {
                    ids.add(item.getId());
                }
                queryWrapper.lambda().in(YxCouponsReply::getCouponId, ids);
            }
        }
        // 根据卡券id
        if (null != criteria.getCouponId()) {
            queryWrapper.lambda().eq(YxCouponsReply::getCouponId, criteria.getCouponId());
        }
        if (null != criteria.getIsReply()) {
            queryWrapper.lambda().eq(YxCouponsReply::getIsReply, criteria.getIsReply());
        }

        IPage<YxCouponsReply> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        map.put("content", dualDto(ipage.getRecords()));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponsReplyDto> queryAll(YxCouponsReplyQueryCriteria criteria) {
        QueryWrapper<YxCouponsReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponsReply::getDelFlag, 0);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                return new ArrayList<>();
            }
            queryWrapper.lambda().in(YxCouponsReply::getMerId, criteria.getChildUser());
        }
        // 根据用户昵称查询
        if (StringUtils.isNotBlank(criteria.getNickName())) {
            User user = this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getNickName, criteria.getNickName()));
            if (null == user) {
                return new ArrayList<>();
            }
            queryWrapper.lambda().eq(YxCouponsReply::getMerId, user.getId());
        }
        // 根据用户登录名查询
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            User user = this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getUsername()));
            if (null == user) {
                return new ArrayList<>();
            }
            queryWrapper.lambda().eq(YxCouponsReply::getMerId, user.getId());
        }
        // 根据商品名称查询
        if (StringUtils.isNotBlank(criteria.getCouponName())) {
            List<YxCoupons> yxCoupons = this.yxCouponsService.list(new QueryWrapper<YxCoupons>().lambda().eq(YxCoupons::getCouponName, criteria.getCouponName()));
            if (null == yxCoupons || yxCoupons.size() <= 0) {
                return new ArrayList<>();
            }
            // 大部分卡券的名称没有重复的、单独判断只查到一条的单独处理
            if (yxCoupons.size() == 1) {
                queryWrapper.lambda().eq(YxCouponsReply::getCouponId, yxCoupons.get(0).getId());
            } else {
                // 查到多条卡券的用拿到id的list用in查询
                List<Integer> ids = new ArrayList<>();
                for (YxCoupons item : yxCoupons) {
                    ids.add(item.getId());
                }
                queryWrapper.lambda().in(YxCouponsReply::getCouponId, ids);
            }
        }
        // 根据卡券id
        if (null != criteria.getCouponId()) {
            queryWrapper.lambda().eq(YxCouponsReply::getCouponId, criteria.getCouponId());
        }
        if (null != criteria.getIsReply()) {
            queryWrapper.lambda().eq(YxCouponsReply::getIsReply, criteria.getIsReply());
        }
        List<YxCouponsReply> list = this.list(queryWrapper);
        if (null == list || list.size() <= 0) {
            return new ArrayList<>();
        }

        return dualDto(list);
    }

    private List<YxCouponsReplyDto> dualDto(List<YxCouponsReply> list) {
        List<YxCouponsReplyDto> resultList = generator.convert(list, YxCouponsReplyDto.class);
        for (YxCouponsReplyDto item : resultList) {
            // 用户名处理
            YxUser yxUser = this.yxUserService.getById(item.getUid());
            if (null != yxUser) {
                item.setUsername(yxUser.getNickname());
            }
            // 所在商户nickname 所在商户username
            User user = this.userService.getById(item.getMerId());
            if (null != user) {
                item.setNickName(user.getNickName());
                item.setMerUsername(user.getUsername());
            }
            // 评论时间
            if (null != item.getAddTime()) {
                item.setAddTimeStr(DateUtils.timestampToStr10(item.getAddTime()));
            }
            // 评论图片
            List<YxImageInfo> imageList = this.yxImageInfoService.list(new QueryWrapper<YxImageInfo>().lambda()
                    .eq(YxImageInfo::getDelFlag, 0)
                    .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_CARD)
                    .eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_REPLY)
                    .eq(YxImageInfo::getTypeId, item.getId()));
            if (null != imageList && imageList.size() > 0) {
                List<String> images = new ArrayList<>();
                for (YxImageInfo image : imageList) {
                    images.add(image.getImgUrl());
                }
                item.setImages(images);
            }
            // 处理卡券信息
            YxCoupons yxCoupons = this.yxCouponsService.getById(item.getCouponId());
            if (null != yxCoupons && StringUtils.isNotBlank(yxCoupons.getCouponName())) {
                item.setCouponName(yxCoupons.getCouponName());
            }
            YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getById(item.getOid());
            if (null != yxCouponOrder) {
                item.setOrderNo(yxCouponOrder.getOrderId());
            }
        }
        return resultList;
    }

    @Override
    public void download(List<YxCouponsReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponsReplyDto yxCouponsReply : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户ID", yxCouponsReply.getUid());
            map.put("用户昵称", yxCouponsReply.getUsername());
            map.put("订单号", yxCouponsReply.getOrderNo());
            map.put("卡券名称", yxCouponsReply.getCouponName());
            map.put("分数", yxCouponsReply.getGeneralScore());
            map.put("评论内容", yxCouponsReply.getComment());
            map.put("评论时间", yxCouponsReply.getAddTimeStr());
            map.put("管理员回复时间", DateUtils.timestampToStr10(yxCouponsReply.getMerchantReplyTime()));
            map.put("回复状态", yxCouponsReply.getIsReply() == 0 ? "未回复" : "已回复");
            map.put("商户id", yxCouponsReply.getMerId());
            map.put("商户登陆名", yxCouponsReply.getMerUsername());
            map.put("商户名称", yxCouponsReply.getNickName());
            map.put("管理员回复内容", yxCouponsReply.getMerchantReplyContent());
            map.put("创建时间", yxCouponsReply.getCreateTime());
            map.put("更新时间", yxCouponsReply.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
