/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxLeaveMessage;
import co.yixiang.modules.shop.domain.YxStoreOrder;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxLeaveMessageService;
import co.yixiang.modules.shop.service.YxStoreOrderService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageDto;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxLeaveMessageMapper;
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
//@CacheConfig(cacheNames = "yxLeaveMessage")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxLeaveMessageServiceImpl extends BaseServiceImpl<YxLeaveMessageMapper, YxLeaveMessage> implements YxLeaveMessageService {

    private final IGenerator generator;
    @Autowired
    private YxStoreProductService yxStoreProductService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxStoreOrderService yxStoreOrderService;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxLeaveMessageQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxLeaveMessage> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxLeaveMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxLeaveMessage::getDelFlag, 0);
        // 处理查询角色
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxLeaveMessage::getMerId, criteria.getChildUser()).ne(YxLeaveMessage::getMessageType, 4);
        } else {
            // 平台管理只查看平台相关的留言
            // 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台
            queryWrapper.lambda().in(YxLeaveMessage::getMessageType, 5);
        }
        // 处理查询条件
        if (StringUtils.isNotBlank(criteria.getUserName())) {
            queryWrapper.lambda().eq(YxLeaveMessage::getUserName, criteria.getUserName());
        }
        if (StringUtils.isNotBlank(criteria.getUserPhone())) {
            queryWrapper.lambda().eq(YxLeaveMessage::getUserPhone, criteria.getUserPhone());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxLeaveMessage::getStatus, criteria.getStatus());
        }
        // 数据查询
        IPage<YxLeaveMessage> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        // 查询数据处理
        List<YxLeaveMessageDto> resultList = generator.convert(ipage.getRecords(), YxLeaveMessageDto.class);
        for (YxLeaveMessageDto item : resultList) {
            if (null != item.getTakeTime() && item.getTakeTime() > 0) {
                // 格式化处理时间
                item.setTakeTimeStr(DateUtils.timestampToStr10(item.getTakeTime()));
            }
            if(0 != item.getStatus()) {
                // 处理人信息
                User user = this.userService.getById(item.getUpdateUserId());
                if(null != user) {
                    item.setUpdateUsername(user.getUsername());
                    item.setUpdateNickname(user.getNickName());
                }
            }
            // 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台
            switch (item.getMessageType()) {
                case 0:
                    YxStoreProduct yxStoreProduct = this.yxStoreProductService.getById(item.getLinkId());
                    if (null != yxStoreProduct) {
                        item.setGoodsName(yxStoreProduct.getStoreName());
                    }
                    break;
                case 1:
                    YxCoupons yxCoupons = this.yxCouponsService.getById(item.getLinkId());
                    if (null != yxCoupons) {
                        item.setGoodsName(yxCoupons.getCouponName());
                    }
                    break;
                case 2:
                    YxStoreOrder yxStoreOrder = this.yxStoreOrderService.getById(item.getLinkId());
                    if (null != yxStoreOrder) {
                        item.setOrderId(yxStoreOrder.getOrderId());
                    }
                    break;
                case 3:
                    YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getById(item.getLinkId());
                    if (null != yxCouponOrder) {
                        item.setOrderId(yxCouponOrder.getOrderId());
                    }
                    break;
                default:
                    break;
            }
        }

        map.put("content", resultList);
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxLeaveMessage> queryAll(YxLeaveMessageQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxLeaveMessage.class, criteria));
    }


    @Override
    public void download(List<YxLeaveMessageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxLeaveMessageDto yxLeaveMessage : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单号", yxLeaveMessage.getLinkId());
            map.put("商户id", yxLeaveMessage.getMerId());
            map.put("联系人", yxLeaveMessage.getUserName());
            map.put("电话", yxLeaveMessage.getUserPhone());
            map.put("留言信息", yxLeaveMessage.getMessage());
            map.put("状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理", yxLeaveMessage.getStatus());
            map.put("留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台", yxLeaveMessage.getMessageType());
            map.put("备注", yxLeaveMessage.getRemark());
            map.put("是否删除（0：未删除，1：已删除）", yxLeaveMessage.getDelFlag());
            map.put("创建人", yxLeaveMessage.getCreateUserId());
            map.put("修改人", yxLeaveMessage.getUpdateUserId());
            map.put("创建时间", yxLeaveMessage.getCreateTime());
            map.put("更新时间", yxLeaveMessage.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
