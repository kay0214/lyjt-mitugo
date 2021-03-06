package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.rocketmq.MqProducer;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.MQConstant;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.domain.YxCouponOrderDetail;
import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.YxCouponOrderUseService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDetailDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderQueryCriteria;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
import co.yixiang.modules.coupon.service.mapper.YxCouponOrderMapper;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.mapper.UserSysMapper;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.hyjf.framework.starter.recketmq.MessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author huiy
 * @date 2020-08-14
 */
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponOrderServiceImpl extends BaseServiceImpl<YxCouponOrderMapper, YxCouponOrder> implements YxCouponOrderService {

    private final IGenerator generator;
    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxCouponOrderUseService yxCouponOrderUseService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private MqProducer mqProducer;
    @Autowired
    private YxMiniPayService miniPayService;
    @Autowired
    private UserSysMapper userSysMapper;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxCouponOrder> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxCouponOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxCouponOrder::getMerId, criteria.getChildUser()).eq(YxCouponOrder::getDelFlag, 0);
        }
        if (null != criteria.getOrderStatus()) {
            if (7 == criteria.getOrderStatus()) {
                queryWrapper.lambda().eq(YxCouponOrder::getRefundStatus, 1);
            } else if (1 == criteria.getOrderStatus()) {
                // 已过期
                queryWrapper.lambda().eq(YxCouponOrder::getStatus, 1);
            } else if (8 == criteria.getOrderStatus()) {
                queryWrapper.lambda().and(refund -> refund.eq(YxCouponOrder::getStatus, criteria.getOrderStatus()).or().eq(YxCouponOrder::getRefundStatus, 2));
            } else {
                queryWrapper.lambda().eq(YxCouponOrder::getStatus, criteria.getOrderStatus()).eq(YxCouponOrder::getRefundStatus, 0);
            }
        }
        if (StringUtils.isNotBlank(criteria.getOrderType()) && StringUtils.isNotBlank(criteria.getValue())) {
            if ("orderId".equals(criteria.getOrderType())) {
                queryWrapper.lambda().eq(YxCouponOrder::getOrderId, criteria.getValue());
            }
            if ("realName".equals(criteria.getOrderType())) {
                queryWrapper.lambda().like(YxCouponOrder::getRealName, criteria.getValue());
            }
            if ("userPhone".equals(criteria.getOrderType())) {
                queryWrapper.lambda().like(YxCouponOrder::getUserPhone, criteria.getValue());
            }
            if ("merUsername".equals(criteria.getOrderType())) {
                User user = this.userSysMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getValue()));
                if (null == user) {
                    Map<String, Object> map = new LinkedHashMap<>(2);
                    map.put("content", new ArrayList<>());
                    map.put("totalElements", 0);
                    return map;
                }
                queryWrapper.lambda().eq(YxCouponOrder::getMerId, user.getId());
            }
        }
        if (StringUtils.isNotBlank(criteria.getStoreName())) {
            List<YxCoupons> coupons = this.yxCouponsService.list(new QueryWrapper<YxCoupons>().lambda().like(YxCoupons::getCouponName, criteria.getStoreName()));
            if (null == coupons || coupons.size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            List<Integer> ids = new ArrayList<>();
            for (YxCoupons item : coupons) {
                ids.add(item.getId());
            }
            queryWrapper.lambda().in(YxCouponOrder::getCouponId, ids);
        }
        if (null != criteria.getCreateTime() && criteria.getCreateTime().size() == 2) {
            queryWrapper.lambda().ge(YxCouponOrder::getCreateTime, criteria.getCreateTime().get(0).concat(" 00:00:00")).le(YxCouponOrder::getCreateTime, criteria.getCreateTime().get(1).concat(" 23:59:59"));
        }

        IPage<YxCouponOrder> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            Map<String, Object> map = new LinkedHashMap<>(2);
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxCouponOrderDto> list = convertList(ipage.getRecords());


        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", list);
        map.put("totalElements", ipage.getTotal());
        return map;
    }

    private List<YxCouponOrderDto> convertList(List<YxCouponOrder> records) {
        List<YxCouponOrderDto> list = generator.convert(records, YxCouponOrderDto.class);
        for (YxCouponOrderDto item : list) {
            // 购买卡券的id
            Integer couponId = item.getCouponId();
            // 购买卡券的信息
            YxCoupons yxCoupons = this.yxCouponsService.getById(couponId);
            // 卡券类型 1:代金券, 2:折扣券, 3:满减券 ，4：船票券
            switch (yxCoupons.getCouponType()) {
                case 1:
                    item.setCouponTypeStr("代金券");
                    break;
                case 2:
                    item.setCouponTypeStr("折扣券");
                    break;
                case 3:
                    item.setCouponTypeStr("满减券");
                    break;
                case 4:
                    item.setCouponTypeStr("船票券");
                    break;
                default:
                    item.setCouponTypeStr("其他");
                    break;
            }
            // 卡券缩略图
            YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", couponId).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                    .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
            if (null != thumbnail && StringUtils.isNotBlank(thumbnail.getImgUrl())) {
                item.setImage(thumbnail.getImgUrl());
            }
            item.setYxCouponsDto(generator.convert(yxCoupons, YxCouponsDto.class));
            String orderStatusStr = "";
            // 卡券详情
            List<YxCouponOrderDetail> detailList = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getCouponId, couponId));
            item.setDetailList(generator.convert(detailList, YxCouponOrderDetailDto.class));
            if (item.getRefundStatus() == 1) {
                String refundTime = "";
                if (null != item.getRefundReasonTime()) {
                    refundTime = DateUtils.timestampToStr10(item.getRefundReasonTime());
                }
                item.setRefundReasonWap(StringUtils.isNotBlank(item.getRefundReasonWap()) ? item.getRefundReasonWap() : "");
                item.setRefundReasonWapExplain(StringUtils.isNotBlank(item.getRefundReasonWapExplain()) ? item.getRefundReasonWapExplain() : "");
                orderStatusStr = "<b style='color:#f124c7'>申请退款</b><br/>" +
                        "<span>退款原因：" + item.getRefundReasonWap() + "</span><br/>" +
                        "<span>备注说明：" + item.getRefundReasonWapExplain() + "</span><br/>" +
                        "<span>退款时间：" + refundTime + "</span><br/>";
            }
            item.setStatusName(orderStatusStr);
            User user = this.userService.getById(item.getMerId());
            if (null != user) {
                item.setUser(user);
            }
            // 处理核销时间
            List<YxCouponOrderUse> useList = this.yxCouponOrderUseService.list(new QueryWrapper<YxCouponOrderUse>().lambda().eq(YxCouponOrderUse::getOrderId, item.getOrderId()));
            if (null != useList && useList.size() > 0) {
                String usedTime = "";
                for (YxCouponOrderUse useItem : useList) {
                    usedTime = usedTime.concat(",").concat(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, useItem.getCreateTime()));
                }
                item.setUsedTime(usedTime.substring(1, usedTime.length()));
            }
        }
        return list;
    }


    @Override
    //@Cacheable
    public List<YxCouponOrder> queryAll(YxCouponOrderQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrder.class, criteria));
    }

    @Override
    public void download(List<YxCouponOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponOrderDto yxCouponOrder : all) {
            // 处理订单状态
            String statusStr = "";
            switch (yxCouponOrder.getStatus()) {
                case 0:
                    statusStr = "待支付";
                    break;
                case 1:
                    statusStr = "已过期";
                    break;
                case 2:
                    statusStr = "待发放";
                    break;
                case 3:
                    statusStr = "支付失败";
                    break;
                case 4:
                    statusStr = "待使用";
                    break;
                case 5:
                    statusStr = "已使用";
                    break;
                case 6:
                    statusStr = "已核销";
                    break;
                case 7:
                    statusStr = "退款中";
                    break;
                case 8:
                    statusStr = "已退款";
                    break;
                case 9:
                    statusStr = "退款驳回";
                    break;
                case 10:
                    statusStr = "已取消";
                    break;
                default:
                    statusStr = "未知状态" + yxCouponOrder.getStatus();
                    break;
            }

            // 处理退款状态
            String refundStatusStr = "";
            switch (yxCouponOrder.getRefundStatus()) {
                case 0:
                    refundStatusStr = "未退款";
                    break;
                case 1:
                    refundStatusStr = "申请中";
                    break;
                case 2:
                    refundStatusStr = "已退款";
                    break;
                default:
                    refundStatusStr = "未知状态" + yxCouponOrder.getRefundStatus();
                    break;
            }
            // 处理推荐人类型
            String parentTypeStr = "";
            switch (yxCouponOrder.getParentType()) {
                case 1:
                    parentTypeStr = "商户";
                    break;
                case 2:
                    parentTypeStr = "合伙人";
                    break;
                case 3:
                    parentTypeStr = "用户";
                    break;
                default:
                    parentTypeStr = "未知类型";
                    break;
            }

            String shareParentTypeStr = "";
            switch (yxCouponOrder.getShareParentType()) {
                case 1:
                    shareParentTypeStr = "商户";
                    break;
                case 2:
                    shareParentTypeStr = "合伙人";
                    break;
                case 3:
                    shareParentTypeStr = "用户";
                    break;
                default:
                    shareParentTypeStr = "未知类型";
                    break;
            }
            // 卡券类型  订单号  用户昵称  uid   商品名称   实际支付   佣金  支付状态   订单状态   创建时间   核销时间  商户id  商户名称  商户登录名  退款时间  退款订单号  退款状态  退款原因  备注
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("卡券类型", yxCouponOrder.getCouponTypeStr());
            map.put("订单号", yxCouponOrder.getOrderId());
            map.put("用户昵称", yxCouponOrder.getRealName());
            map.put("uid", yxCouponOrder.getUid());
            map.put("商品名称", null != yxCouponOrder.getYxCouponsDto() && StringUtils.isNotBlank(yxCouponOrder.getYxCouponsDto().getCouponName()) ? yxCouponOrder.getYxCouponsDto().getCouponName() : "");
            map.put("实际支付", yxCouponOrder.getTotalPrice());
            map.put("佣金", yxCouponOrder.getCommission());
            map.put("支付状态", yxCouponOrder.getPayStaus() == 0 ? "未支付" : "已支付");
            map.put("订单状态", statusStr);
            map.put("创建时间", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yxCouponOrder.getCreateTime()));
            // 核销时间暂时无法处理
            map.put("核销时间", yxCouponOrder.getUsedTime());
            map.put("商户ID", yxCouponOrder.getMerId());
            if (null != yxCouponOrder.getUser()) {
                map.put("商户名称", yxCouponOrder.getUser().getNickName());
                map.put("商户登录名", yxCouponOrder.getUser().getUsername());
            }
            map.put("退款时间", DateUtils.timestampToStr10(yxCouponOrder.getRefundReasonTime()));
            // 退款没有订单号
            map.put("退款状态", refundStatusStr);
            map.put("退款原因", yxCouponOrder.getRefundReasonWap());
            map.put("备注", yxCouponOrder.getMark());
//
//            map.put("用户电话", yxCouponOrder.getUserPhone());
//            map.put("订单商品总数", yxCouponOrder.getTotalNum());
//            map.put("卡券id", yxCouponOrder.getCouponId());
//            map.put("卡券金额", yxCouponOrder.getCouponPrice());
//            map.put("支付时间", yxCouponOrder.getPayTime());
//            map.put("可被核销次数", yxCouponOrder.getUseCount());
//            map.put("已核销次数", yxCouponOrder.getUsedCount());
//            map.put("不退款的理由", yxCouponOrder.getRefundReason());
//            map.put("退款金额", yxCouponOrder.getRefundPrice());
//            map.put("推荐人用户ID", yxCouponOrder.getParentId());
//            map.put("推荐人类型", parentTypeStr);
//            map.put("分享人Id", yxCouponOrder.getShareId());
//            map.put("分享人的推荐人id", yxCouponOrder.getShareParentId());
//            map.put("分享人的推荐人类型", shareParentTypeStr);
//            map.put("核销码", yxCouponOrder.getVerifyCode());
//            map.put("是否删除", yxCouponOrder.getDelFlag() == 0 ? "未删除" : "已删除");
//            map.put("创建人id", yxCouponOrder.getCreateUserId());
//            map.put("修改人id", yxCouponOrder.getUpdateUserId());
//            map.put("更新时间", yxCouponOrder.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 卡券核销
     *
     * @param decodeVerifyCode
     * @param uid
     * @return
     */
    @Override
    public boolean updateCouponOrder(String decodeVerifyCode, int uid) {
        String[] decode = decodeVerifyCode.split(",");
        if (decode.length != 2) {
            throw new BadRequestException("无效核销码");
        }
        // 获取核销码
        String verifyCode = decode[0];
        // 获取核销用户的id
        String useUid = decode[1];
        YxCouponOrderDetail yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        if (null == yxCouponOrderDetail) {
            throw new BadRequestException("查询卡券订单详情失败");
        }
        YxCouponOrder yxCouponOrder = this.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", yxCouponOrderDetail.getOrderId()));
        if (null == yxCouponOrder) {
            throw new BadRequestException("查询卡券订单失败");
        }
        // 判断订单状态
        if (4 != yxCouponOrder.getStatus() && 5 != yxCouponOrder.getStatus()) {
            throw new BadRequestException("当前订单状态不是待使用");
        }
        if (1 == yxCouponOrder.getRefundStatus()) {
            throw new BadRequestException("退款申请中的订单无法核销");
        }
        if (!yxCouponOrder.getUid().equals(useUid)) {
            throw new BadRequestException("核销码与用户信息不匹配");
        }
        // 查询优惠券信息
        YxCoupons yxCoupons = this.yxCouponsService.getById(yxCouponOrderDetail.getCouponId());
        if (null == yxCoupons) {
            throw new BadRequestException("核销未查询到卡券信息");
        }
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", uid));
        if (null == yxStoreInfo) {
            throw new BadRequestException("未获取到用户的店铺信息");
        }
        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getCreateUserId().equals(uid)) {
            throw new BadRequestException("不可核销其他商户的卡券");
        }
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            throw new BadRequestException("当前卡券已达核销上限");
        }
        // 判断有效期
        LocalDateTime expireDateStart = yxCoupons.getExpireDateStart().toLocalDateTime();
        LocalDateTime expireDateEnd = yxCoupons.getExpireDateEnd().toLocalDateTime();
        if (expireDateStart.isBefore(LocalDateTime.now()) || expireDateEnd.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("当前卡券不在有效期内");
        }
        // 判断卡券状态
        if (4 != yxCouponOrderDetail.getStatus() && 5 != yxCouponOrderDetail.getStatus()) {
            throw new BadRequestException("当前卡券状态不是待使用");
        }
        // 第一次核销发送分佣mq
        boolean isFirst = false;
        if (0 == yxCouponOrder.getUsedCount() && 0 == yxCouponOrder.getRebateStatus()) {
            isFirst = true;
        }
        // 处理订单表数据
        yxCouponOrder.setUsedCount(yxCouponOrder.getUsedCount() + 1);
        if (yxCouponOrder.getUsedCount().equals(yxCouponOrder.getUseCount())) {
            // 次数全部使用完成的状态更新为已核销
            yxCouponOrder.setStatus(6);
        } else {
            yxCouponOrder.setStatus(5);
        }
        // 处理订单详情表数据
        yxCouponOrderDetail.setUsedCount(yxCouponOrderDetail.getUsedCount() + 1);
        if (yxCouponOrderDetail.getUsedCount().equals(yxCouponOrderDetail.getUseCount())) {
            // 次数全部使用完成的状态更新为已核销
            yxCouponOrderDetail.setStatus(6);
        } else {
            yxCouponOrderDetail.setStatus(5);
        }
        // 处理店铺核销数据
        YxCouponOrderUse yxCouponOrderUse = new YxCouponOrderUse();
        yxCouponOrderUse.setCouponId(yxCouponOrderDetail.getCouponId());
        yxCouponOrderUse.setOrderId(yxCouponOrder.getOrderId());
        yxCouponOrderUse.setStoreId(yxStoreInfo.getId());
        yxCouponOrderUse.setStoreName(yxStoreInfo.getStoreName());
        yxCouponOrderUse.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponOrderUse.setDelFlag(0);
        yxCouponOrderUse.setCreateUserId(uid);

        // 数据入库
        this.updateById(yxCouponOrder);
        this.yxCouponOrderDetailService.updateById(yxCouponOrderDetail);
        this.yxCouponOrderUseService.save(yxCouponOrderUse);

        if (isFirst) {
            // 分佣mq发送
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", yxCouponOrder.getOrderId());
            jsonObject.put("orderType", "1");
//            Map<String, String> map = new HashMap<>();
//            map.put("orderId", yxCouponOrder.getOrderId());
//            map.put("orderType", "1");
            mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_COMMISSION_TAG, UUID.randomUUID().toString(), jsonObject));
        }
        return true;
    }

    /**
     * 卡券订单退款
     *
     * @param resources
     */
    @Override
    public void refund(YxCouponOrderDto resources) {
        if (null == resources.getId()) {
            throw new BadRequestException("缺少主键id");
        }
        if (null == resources.getRefundStatus() || 1 == resources.getRefundStatus()) {
            throw new BadRequestException("请选择审核结果");
        }
        // 退款驳回
        if (0 == resources.getRefundStatus()) {
            YxCouponOrder refuse = new YxCouponOrder();
            refuse.setId(resources.getId());
            refuse.setRefundStatus(0);
            refuse.setRefundReason(StringUtils.isNotBlank(resources.getRefundReason()) ? resources.getRefundReason() : "退款申请被驳回");
            this.updateById(refuse);
            return;
        }

        // 退款通过
        if (resources.getRefundPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("请输入退款金额");
        }
        // 查询订单详情
        YxCouponOrder yxCouponOrder = this.getById(resources.getId());
        if (null == yxCouponOrder) {
            throw new BadRequestException("退款查询信息失败");
        }
        if (resources.getRefundPrice().compareTo(yxCouponOrder.getTotalPrice()) > 0) {
            throw new BadRequestException("退款金额不可大于支付金额");
        }
        if (2 == yxCouponOrder.getRefundStatus()) {
            throw new BadRequestException("该笔订单已退款");
        }

        BigDecimal bigDecimal = new BigDecimal("100");
        try {
            miniPayService.refundCouponOrderNew(yxCouponOrder.getOrderId(), bigDecimal.multiply(resources.getRefundPrice()).intValue(), yxCouponOrder.getOrderId(), bigDecimal.multiply(yxCouponOrder.getTotalPrice()).intValue());
        } catch (WxPayException e) {
            log.info("refund-error:{}", e.getMessage());
            throw new BadRequestException("退款失败");
        }
    }

    /**
     * 手动核销卡券
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean updateCouponOrderInput(String orderId, Integer uid) {
        // 获取订单信息
        YxCouponOrder yxCouponOrder = this.getOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId, orderId));
        if (null == yxCouponOrder) {
            throw new BadRequestException("卡券订单不存在");
        }
        // 判断卡券状态
        if (4 != yxCouponOrder.getStatus() && 5 != yxCouponOrder.getStatus()) {
            throw new BadRequestException("当前订单状态不是待使用");
        }
        if (1 == yxCouponOrder.getRefundStatus()) {
            throw new BadRequestException("退款申请中的订单无法核销");
        }
        // 判断总核销次数
        if (yxCouponOrder.getUseCount() <= yxCouponOrder.getUsedCount()) {
            throw new BadRequestException("该订单卡券已全部核销");
        }

        // 查询优惠券信息
        YxCoupons yxCoupons = this.yxCouponsService.getById(yxCouponOrder.getCouponId());
        if (null == yxCoupons) {
            throw new BadRequestException("优惠券已不存在，无法核销");
        }
        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getStoreId().equals(uid)) {
            throw new BadRequestException("该卡券不属于本店铺，无法核销");
        }
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", yxCoupons.getStoreId()));
        if (null == yxStoreInfo) {
            throw new BadRequestException("店铺信息不存在");
        }

        // 判断有效期
        LocalDateTime expireDateStart = yxCoupons.getExpireDateStart().toLocalDateTime();
        LocalDateTime expireDateEnd = yxCoupons.getExpireDateEnd().toLocalDateTime();
        if (expireDateStart.isBefore(LocalDateTime.now()) || expireDateEnd.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("当前卡券不在有效期内");
        }

        // 获取一张可用卡券
        List<YxCouponOrderDetail> detailList = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, orderId));
        if (null == detailList || detailList.size() <= 0) {
            throw new BadRequestException("卡券订单详情不存在");
        }
        boolean haveCoupon = false;
        YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
        for (YxCouponOrderDetail item : detailList) {
            if (item.getStatus() == 4 || item.getStatus() == 5) {
                // 未核销完
                if (item.getUsedCount() < item.getUseCount()) {
                    yxCouponOrderDetail = item;
                    haveCoupon = true;
                    break;
                }
            }
        }
        if (!haveCoupon) {
            throw new BadRequestException("该订单所有卡券均已被核销");
        }

        // 第一次核销发送分佣mq
        boolean isFirst = false;
        if (0 == yxCouponOrder.getUsedCount() && 0 == yxCouponOrder.getRebateStatus()) {
            isFirst = true;
        }
        // 处理订单表数据
        yxCouponOrder.setUsedCount(yxCouponOrder.getUsedCount() + 1);
        if (yxCouponOrder.getUsedCount().equals(yxCouponOrder.getUseCount())) {
            // 次数全部使用完成的状态更新为已核销
            yxCouponOrder.setStatus(6);
        } else {
            yxCouponOrder.setStatus(5);
        }
        // 处理订单详情表数据
        yxCouponOrderDetail.setUsedCount(yxCouponOrderDetail.getUsedCount() + 1);
        if (yxCouponOrderDetail.getUsedCount().equals(yxCouponOrderDetail.getUseCount())) {
            // 次数全部使用完成的状态更新为已核销
            yxCouponOrderDetail.setStatus(6);
        } else {
            yxCouponOrderDetail.setStatus(5);
        }
        // 处理店铺核销数据
        YxCouponOrderUse yxCouponOrderUse = new YxCouponOrderUse();
        yxCouponOrderUse.setCouponId(yxCouponOrderDetail.getCouponId());
        yxCouponOrderUse.setOrderId(yxCouponOrder.getOrderId());
        yxCouponOrderUse.setStoreId(yxStoreInfo.getId());
        yxCouponOrderUse.setStoreName(yxStoreInfo.getStoreName());
        yxCouponOrderUse.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponOrderUse.setDelFlag(0);
        yxCouponOrderUse.setCreateUserId(uid);

        // 数据入库
        this.updateById(yxCouponOrder);
        this.yxCouponOrderDetailService.updateById(yxCouponOrderDetail);
        this.yxCouponOrderUseService.save(yxCouponOrderUse);

        if (isFirst) {
            // 分佣mq发送
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", yxCouponOrder.getOrderId());
            jsonObject.put("orderType", "1");
            mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_COMMISSION_TAG, UUID.randomUUID().toString(), jsonObject));
        }
        return true;
    }

    /**
     * 支付超时取消订单
     *
     * @param yxCouponOrder
     * @return
     */
    @Override
    public boolean updateCancelNoPayOrder(YxCouponOrder yxCouponOrder) {
        YxCoupons yxCoupons = this.yxCouponsService.getById(yxCouponOrder.getCouponId());
        if (null == yxCoupons) {
            log.info("订单：" + yxCouponOrder.getOrderId() + "取消失败,未查询到相关卡券信息");
            return false;
        }
        boolean result = this.yxCouponsService.updateCancelNoPayOrder(yxCouponOrder.getCouponId(), yxCouponOrder.getTotalNum());
        if (result) {
            YxCouponOrder updateOrder = new YxCouponOrder();
            updateOrder.setId(yxCouponOrder.getId());
            // 已取消
            updateOrder.setStatus(10);
            this.updateById(updateOrder);
            YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
            yxCouponOrderDetail.setStatus(10);
            this.yxCouponOrderDetailService.update(yxCouponOrderDetail, new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, yxCouponOrder.getOrderId()));
        }
        return result;
    }

    /**
     * 查询导出数据
     *
     * @param criteria
     * @return
     */
    @Override
    public List<YxCouponOrderDto> queryDownload(YxCouponOrderQueryCriteria criteria) {
        QueryWrapper<YxCouponOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                throw new BadRequestException("未查询到数据");
            }
            queryWrapper.lambda().in(YxCouponOrder::getMerId, criteria.getChildUser()).eq(YxCouponOrder::getDelFlag, 0);
        }
        if (null != criteria.getOrderStatus()) {
            if (7 == criteria.getOrderStatus()) {
                queryWrapper.lambda().eq(YxCouponOrder::getRefundStatus, 1);
            } else if (1 == criteria.getOrderStatus()) {
                // 已过期
                queryWrapper.lambda().eq(YxCouponOrder::getStatus, 1);
            } else if (8 == criteria.getOrderStatus()) {
                queryWrapper.lambda().eq(YxCouponOrder::getStatus, criteria.getOrderStatus());
            } else {
                queryWrapper.lambda().eq(YxCouponOrder::getStatus, criteria.getOrderStatus()).eq(YxCouponOrder::getRefundStatus, 0);
            }
        }
        if (StringUtils.isNotBlank(criteria.getOrderType()) && StringUtils.isNotBlank(criteria.getValue())) {
            if ("orderId".equals(criteria.getOrderType())) {
                queryWrapper.lambda().eq(YxCouponOrder::getOrderId, criteria.getValue());
            }
            if ("realName".equals(criteria.getOrderType())) {
                queryWrapper.lambda().like(YxCouponOrder::getRealName, criteria.getValue());
            }
            if ("userPhone".equals(criteria.getOrderType())) {
                queryWrapper.lambda().like(YxCouponOrder::getUserPhone, criteria.getValue());
            }
            if ("merUsername".equals(criteria.getOrderType())) {
                User user = this.userSysMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, criteria.getValue()));
                if (null == user) {
                    throw new BadRequestException("未查询到数据");
                }
                queryWrapper.lambda().eq(YxCouponOrder::getMerId, user.getId());
            }
        }
        List<YxCouponOrder> yxCouponOrders = this.list(queryWrapper);
        if (null == yxCouponOrders || yxCouponOrders.size() <= 0) {
            throw new BadRequestException("未查询到数据");
        }
        return convertList(yxCouponOrders);
    }

    /**
     * 过期订单自动取消
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAutoCancel() {
        // 获取待使用和未使用的、当前时间已过期的订单
        List<YxCouponOrder> list = this.list(new QueryWrapper<YxCouponOrder>().lambda()
                .in(YxCouponOrder::getStatus, 0, 2, 4, 5, 7)
                .gt(YxCouponOrder::getOutTime, 0)
                .lt(YxCouponOrder::getOutTime, DateUtils.getNowTime()));
        for (YxCouponOrder item : list) {
            YxCoupons yxCoupons = this.yxCouponsService.getById(item.getCouponId());
            if (null == yxCoupons) {
                // 卡券查询不到的默认按不支持过期退处理
                yxCoupons = new YxCoupons();
                yxCoupons.setAwaysRefund(0);
                yxCoupons.setOuttimeRefund(0);
            }
            // 先判断下卡券是否支持过期退和随时退、支持的话把所有未使用的订单退款
            if (1 == yxCoupons.getAwaysRefund() || 1 == yxCoupons.getOuttimeRefund()) {
                if (4 == item.getStatus()) {
                    // 支持过期退和随时退的、未使用的订单全额退款
                    YxCouponOrderDto resources = new YxCouponOrderDto();
                    resources.setId(item.getId());
                    resources.setRefundStatus(2);
                    resources.setRefundPrice(item.getTotalPrice());
                    try {
                        this.refund(resources);
                    } catch (BadRequestException e) {
                        log.error("订单：" + item.getOrderId() + "退款失败！", e.getMessage());
                    }
                }
            }
            // 剩下未退款的更新成已过期
            item.setStatus(1);
            this.updateById(item);
            LambdaQueryWrapper<YxCouponOrderDetail> orderDetailWrapper = new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, item.getOrderId());
            YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
            yxCouponOrderDetail.setStatus(1);
            yxCouponOrderDetailService.update(yxCouponOrderDetail, orderDetailWrapper);
        }
    }
}
