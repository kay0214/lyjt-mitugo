package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.DistanceMeterUtil;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.*;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.CouponOrderMap;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.*;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.monitor.service.RedisService;
import co.yixiang.modules.order.web.dto.ComputeDTO;
import co.yixiang.modules.order.web.dto.CouponCacheDTO;
import co.yixiang.modules.order.web.dto.PriceGroupDTO;
import co.yixiang.modules.order.web.param.OrderParam;
import co.yixiang.modules.order.web.param.RefundParam;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 卡券订单表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderServiceImpl extends BaseServiceImpl<YxCouponOrderMapper, YxCouponOrder> implements YxCouponOrderService {

    @Autowired
    private YxCouponOrderMapper yxCouponOrderMapper;
    @Autowired
    private YxCouponsMapper yxCouponsMapper;

    @Autowired
    private YxSystemConfigService systemConfigService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private YxUserService userService;

    @Autowired
    private YxSystemStoreService systemStoreService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private YxUserBillService billService;

    @Autowired
    private YxCouponsService couponsService;

    @Autowired
    private YxStoreInfoService storeInfoService;

    @Autowired
    private YxImageInfoService yxImageInfoService;

    @Autowired
    private YxWechatUserService wechatUserService;

    @Autowired
    private YxMiniPayService miniPayService;

    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;

    @Autowired
    private CouponOrderMap couponOrderMap;

    @Override
    public YxCouponOrderQueryVo getYxCouponOrderById(Serializable id) throws Exception {
        return yxCouponOrderMapper.getYxCouponOrderById(id);
    }

    @Override
    public Paging<YxCouponOrderQueryVo> getYxCouponOrderPageList(YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception {
        Page page = setPageParam(yxCouponOrderQueryParam, OrderItem.desc("create_time"));
        IPage<YxCouponOrderQueryVo> iPage = yxCouponOrderMapper.getYxCouponOrderPageList(page, yxCouponOrderQueryParam);
        return new Paging(iPage);
    }


    @Override
    public PriceGroupDTO getOrderPriceGroup(CouponOrderQueryVo couponOrderQueryVo) {
        String storePostageStr = systemConfigService.getData(SystemConfigConstants.STORE_POSTAGE);//邮费基础价
        Double storePostage = 0d;
        if (StrUtil.isNotEmpty(storePostageStr)) storePostage = Double.valueOf(storePostageStr);

        String storeFreePostageStr = systemConfigService.getData(SystemConfigConstants.STORE_FREE_POSTAGE);//满额包邮
        Double storeFreePostage = 0d;
        if (StrUtil.isNotEmpty(storeFreePostageStr)) storeFreePostage = Double.valueOf(storeFreePostageStr);
        List<YxCouponsQueryVo> couponsList = couponOrderQueryVo.getYxCouponsQueryVoList();
        Double totalPrice = getOrderSumPrice(couponOrderQueryVo, "truePrice");//获取订单总金额
//        Double costPrice = getOrderSumPrice(couponOrderQueryVo, "costPrice");//获取订单成本价

//        if (storeFreePostage == 0) {//包邮
//            storePostage = 0d;
//        } else {
//            for (YxCouponsQueryVo couponsQueryVo : couponsList) {
//                if (storeCart.getProductInfo().getIsPostage() == 0) {//不包邮
//                    storePostage = NumberUtil.add(storePostage
//                            , storeCart.getProductInfo().getPostage()).doubleValue();
//                }
//            }
//            //如果总价大于等于满额包邮 邮费等于0
//            if (storeFreePostage <= totalPrice) storePostage = 0d;
//        }

        PriceGroupDTO priceGroupDTO = new PriceGroupDTO();
        priceGroupDTO.setStorePostage(storePostage);
        priceGroupDTO.setStoreFreePostage(storeFreePostage);
        priceGroupDTO.setTotalPrice(totalPrice);
//        priceGroupDTO.setCostPrice(costPrice);
//        priceGroupDTO.setVipPrice(vipPrice);

        return priceGroupDTO;
    }

    /**
     * 获取某字段价格
     *
     * @param couponOrderQueryVo
     * @param key
     * @return
     */
    @Override
    public Double getOrderSumPrice(CouponOrderQueryVo couponOrderQueryVo, String key) {
        BigDecimal sumPrice = BigDecimal.ZERO;
        List<YxCouponsQueryVo> couponsQueryVos = couponOrderQueryVo.getYxCouponsQueryVoList();
        if (key.equals("truePrice")) {
            for (YxCouponsQueryVo couponsQueryVo : couponsQueryVos) {
                sumPrice = NumberUtil.add(sumPrice, NumberUtil.mul(couponOrderQueryVo.getQuantity(), couponsQueryVo.getSellingPrice()));
            }
        }
//        else if (key.equals("costPrice")) {
//            for (YxCouponsQueryVo couponsQueryVo : couponsQueryVos) {
//                sumPrice = NumberUtil.add(sumPrice,
//                        NumberUtil.mul(couponOrderQueryVo.getQuantity(), couponsQueryVo.getCostPrice()));
//            }
//        }

        //System.out.println("sumPrice:"+sumPrice);
        return sumPrice.doubleValue();
    }


    /**
     * 创建订单
     *
     * @param uid   uid
     * @param key   key
     * @param param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxCouponOrder createOrder(int uid, String key, OrderParam param) {
        YxUserQueryVo userInfo = userService.getYxUserById(uid);
        if (ObjectUtil.isNull(userInfo)) {
            throw new ErrorRequestException("用户不存在");
        }

        CouponCacheDTO cacheDTO = getCacheOrderInfo(uid, key);
        if (ObjectUtil.isNull(cacheDTO)) {
            throw new ErrorRequestException("订单已过期,请刷新当前页面");
        }

        YxCoupons coupons = yxCouponsMapper.selectById(param.getCouponId());
        Double totalPrice = cacheDTO.getPriceGroup().getTotalPrice();
        Double payPrice = cacheDTO.getPriceGroup().getTotalPrice();
        Double payPostage = cacheDTO.getPriceGroup().getStorePostage();

        Integer totalNum = 0;
        Integer gainIntegral = 0;
        List<String> cartIds = new ArrayList<>();
        int combinationId = 0;
        int seckillId = 0;
        int bargainId = 0;


        if (payPrice <= 0) payPrice = 0d;

        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        //组合数据
        YxCouponOrder couponOrder = new YxCouponOrder();
        couponOrder.setUid(uid);
        couponOrder.setOrderId(orderSn);
        couponOrder.setRealName("");
        couponOrder.setUserPhone("");
        couponOrder.setTotalNum(totalNum);
        couponOrder.setTotalPrice(BigDecimal.valueOf(totalPrice));
        couponOrder.setTotalPrice(BigDecimal.valueOf(payPrice));
        couponOrder.setPayStaus(OrderInfoEnum.PAY_STATUS_0.getValue());

        couponOrder.setPayType(param.getPayType());
        couponOrder.setMark(param.getMark());
        couponOrder.setCouponId(param.getCouponId());
        couponOrder.setUseCount(coupons.getWriteOff());
        couponOrder.setCouponPrice(coupons.getSellingPrice());
        // 分享人的相关未做

        if (AppFromEnum.ROUNTINE.getValue().equals(param.getFrom())) {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_1.getValue());
        } else {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_0.getValue());
        }
        couponOrder.setCreateTime(DateTime.now());
        couponOrder.setUnique(key);
        //处理门店
        if (OrderInfoEnum.SHIPPIING_TYPE_2.getValue().equals(param.getShippingType())) {
            YxSystemStoreQueryVo systemStoreQueryVo = systemStoreService.getYxSystemStoreById(param.getStoreId());
            if (systemStoreQueryVo == null) throw new ErrorRequestException("暂无门店无法选择门店自提");
            couponOrder.setVerifyCode(StrUtil.sub(orderSn, orderSn.length(), -12));
        }

        boolean res = save(couponOrder);
        if (!res) throw new ErrorRequestException("订单生成失败");

        YxCouponOrderDetail couponOrderDetail = new YxCouponOrderDetail();
        couponOrderDetail.setUid(uid);
        couponOrderDetail.setOrderId(orderSn);
        couponOrderDetail.setCouponId(coupons.getId());

        // 插入detail表相关未做

        //减库存加销量

        //删除缓存
        delCacheOrderInfo(uid, key);

        //增加状态 (订单操作纪律, 卡券无此表)
//        orderStatusService.create(couponOrder.getId(), "cache_key_create_coupon_order", "订单生成");


        //使用MQ延时消息
        //mqProducer.sendMsg("yshop-topic",storeOrder.getId().toString());
        //log.info("投递延时订单id： [{}]：", storeOrder.getId());

        //加入redis，30分钟自动取消
        String redisKey = String.valueOf(StrUtil.format("{}{}",
                LocalLiveConstants.REDIS_COUPON_ORDER_OUTTIME_UNPAY, couponOrder.getId()));
        redisTemplate.opsForValue().set(redisKey, couponOrder.getOrderId(),
                LocalLiveConstants.ORDER_OUTTIME_UNPAY, TimeUnit.MINUTES);

        return couponOrder;
    }

    /**
     * 余额支付
     *
     * @param orderId 订单号
     * @param uid     用户id
     */
    @Override
    public void yuePay(String orderId, int uid) {
        YxCouponOrder orderInfo = getOrderInfo(orderId, uid);
        if (ObjectUtil.isNull(orderInfo)) throw new ErrorRequestException("订单不存在");

        if (orderInfo.getPayStaus().equals(OrderInfoEnum.PAY_STATUS_1.getValue()))
            throw new ErrorRequestException("该订单已支付");

        YxUserQueryVo userInfo = userService.getYxUserById(uid);

        if (userInfo.getNowMoney().doubleValue() < orderInfo.getTotalPrice().doubleValue()) {
            throw new ErrorRequestException("余额不足");
        }

        userService.decPrice(uid, orderInfo.getTotalPrice().doubleValue());

        YxUserBill userBill = new YxUserBill();
        userBill.setUid(uid);
        userBill.setTitle("购买商品");
        userBill.setLinkId(orderInfo.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("pay_product");
        userBill.setNumber(orderInfo.getTotalPrice());
        userBill.setBalance(userInfo.getNowMoney());
        userBill.setMark("余额支付");
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        userBill.setPm(BillEnum.PM_0.getValue());
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //支付成功后处理
        paySuccess(orderInfo.getOrderId(), "yue");

    }

    @Override
    public CouponCacheDTO getCacheOrderInfo(int uid, String key) {

        return (CouponCacheDTO) redisService.getObj("user_coupon_order_" + uid + key);
    }

    /**
     * 缓存数据
     *
     * @param uid
     * @param couponsQueryVos
     * @param priceGroup
     * @return
     */
    @Override
    public String cacheOrderInfo(int uid, List<YxCouponsQueryVo> couponsQueryVos, PriceGroupDTO priceGroup) {
        String key = IdUtil.simpleUUID();
        CouponCacheDTO couponCacheDTO = new CouponCacheDTO();
        couponCacheDTO.setCouponsQueryVoList(couponsQueryVos);
        couponCacheDTO.setPriceGroup(priceGroup);
        redisService.saveCode("user_coupon_order_" + uid + key, couponCacheDTO, 600L);
        return key;
    }

    /**
     * 删除缓存
     *
     * @param uid
     * @param key
     */
    @Override
    public void delCacheOrderInfo(int uid, String key) {
        redisService.delete("user_coupon_order_" + uid + key);
    }

    /**
     * 订单信息
     *
     * @param unique
     * @param uid
     * @return
     */
    @Override
    public YxCouponOrder getOrderInfo(String unique, int uid) {
        QueryWrapper<YxCouponOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", 0).and(
                i -> i.eq("order_id", unique).or().eq("`unique`", unique));
        if (uid > 0) wrapper.eq("uid", uid);

        return yxCouponOrderMapper.selectOne(wrapper);
        //return new YxCouponOrderQueryVo();
    }

    /**
     * 支付成功后操作
     *
     * @param orderId 订单号
     * @param payType 支付方式
     */
    @Override
    public void paySuccess(String orderId, String payType) {
        YxCouponOrder orderInfo = getOrderInfo(orderId, 0);

        //更新订单状态
        QueryWrapper<YxCouponOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        YxCouponOrder couponOrder = new YxCouponOrder();
        couponOrder.setPayStaus(OrderInfoEnum.PAY_STATUS_1.getValue());

        couponOrder.setPayType(payType);
        couponOrder.setPayTime(OrderUtil.getSecondTimestampTwo());
        yxCouponOrderMapper.update(couponOrder, wrapper);

        //增加用户购买次数
        userService.incPayCount(orderInfo.getUid());
        //增加状态
//        orderStatusService.create(orderInfo.getId(), "pay_success", "用户付款成功");

        // TODO :: 等待对接微信公众号,以推送消息
        //模板消息推送
//        YxWechatUserQueryVo wechatUser = wechatUserService.getYxWechatUserById(orderInfo.getUid());
//        if (ObjectUtil.isNotNull(wechatUser)) {
//            ////公众号与小程序打通统一公众号模板通知
//            if (StrUtil.isNotBlank(wechatUser.getOpenid())) {
//                templateService.paySuccessNotice(orderInfo.getOrderId(),
//                        orderInfo.getPayPrice().toString(), wechatUser.getOpenid());
//            }
//        }

    }

    /**
     * 计算价格
     *
     * @param uid
     * @param key
     * @param couponId
     * @param useIntegral
     * @param shippingType
     * @return
     */
    @Override
    public ComputeDTO computedOrder(int uid, String key, int couponId, int useIntegral, int shippingType) {
        YxUserQueryVo userInfo = userService.getYxUserById(uid);
        if (ObjectUtil.isNull(userInfo)) {
            throw new ErrorRequestException("用户不存在");
        }

        CouponCacheDTO cacheDTO = getCacheOrderInfo(uid, key);
        if (ObjectUtil.isNull(cacheDTO)) {
            throw new ErrorRequestException("订单已过期,请刷新当前页面");
        }
        ComputeDTO computeDTO = new ComputeDTO();
        computeDTO.setTotalPrice(cacheDTO.getPriceGroup().getTotalPrice());
        Double payPrice = cacheDTO.getPriceGroup().getTotalPrice();
        Double payPostage = cacheDTO.getPriceGroup().getStorePostage();

        //1-配送 2-到店
        if (shippingType == 1) {
            payPrice = NumberUtil.add(payPrice, payPostage);
        } else {
            payPostage = 0d;
        }

        boolean deduction = false;//拼团秒杀砍价等
        int combinationId = 0;
        int seckillId = 0;
        int bargainId = 0;

        if (payPrice <= 0) payPrice = 0d;

        computeDTO.setPayPrice(payPrice);
        computeDTO.setPayPostage(payPostage);

        return computeDTO;
    }

    /**
     * 通过卡券ID 获取卡券信息和所属公司信息
     *
     * @param couponId
     * @return
     */
    @Override
    public CouponInfoQueryVo getCouponInfo(Integer couponId) {
        YxCoupons yxCoupons = couponsService.getOne(new QueryWrapper<YxCoupons>().eq("id", couponId).eq("del_flag", 0));
        if (yxCoupons == null) {
            throw new ErrorRequestException("卡券不存在, 请检查卡券ID是否正确!");
        }

        CouponInfoQueryVo couponInfoQueryVo = new CouponInfoQueryVo();
        BeanUtil.copyProperties(yxCoupons, couponInfoQueryVo);
        // 卡券缩略图
        YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", yxCoupons.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
        if (thumbnail != null) {
            couponInfoQueryVo.setImage(thumbnail.getImgUrl());
        }
        YxStoreInfo storeInfo = storeInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("id", yxCoupons.getBelong()).eq("del_flag", 0));
        if (storeInfo != null) {
            couponInfoQueryVo.setStoreInfo(storeInfo);
        }
        return couponInfoQueryVo;
    }

    @Override
    public WxPayMpOrderResult wxAppPay(String orderId, String ip) throws WxPayException {
        YxCouponOrder orderInfo = getOrderInfo(orderId, 0);
        if (ObjectUtil.isNull(orderInfo)) throw new ErrorRequestException("订单不存在");
        if (orderInfo.getPayStaus().equals(OrderInfoEnum.PAY_STATUS_1.getValue()))
            throw new ErrorRequestException("该订单已支付");

        if (orderInfo.getTotalPrice().doubleValue() <= 0) throw new ErrorRequestException("该支付无需支付");

        YxWechatUser wechatUser = wechatUserService.getById(orderInfo.getUid());
        if (ObjectUtil.isNull(wechatUser)) throw new ErrorRequestException("用户错误");


        BigDecimal bigDecimal = new BigDecimal(100);

        return miniPayService.couponWxPay(orderId, wechatUser.getRoutineOpenid(), "小程序本地生活购买",
                bigDecimal.multiply(orderInfo.getTotalPrice()).intValue(),
                BillDetailEnum.TYPE_8.getValue(), ip);
    }

    /**
     * 提交订单退款
     *
     * @param param
     * @param uid
     */
    @Override
    public void orderApplyRefund(RefundParam param, int uid) {
        YxCouponOrder order = getOrderInfo(param.getUni(), uid);
        if (ObjectUtil.isNull(order)) throw new ErrorRequestException("订单不存在");
        if (order.getRefundStatus() == 2) throw new ErrorRequestException("订单已退款");
        if (order.getRefundStatus() == 1) throw new ErrorRequestException("正在申请退款中");
        if (order.getStatus() == 1) throw new ErrorRequestException("订单当前无法退款");

        YxCouponOrder storeOrder = new YxCouponOrder();
        storeOrder.setRefundStatus(OrderInfoEnum.REFUND_STATUS_1.getValue());
        storeOrder.setRefundReasonTime(OrderUtil.getSecondTimestampTwo());
        storeOrder.setRefundReasonWapExplain(param.getRefund_reason_wap_explain());
        storeOrder.setId(order.getId());
        yxCouponOrderMapper.updateById(storeOrder);

        //增加状态
        //orderStatusService.create(order.getId(), "apply_refund", "用户申请退款，原因：" + param.getText());
    }

    /**
     * 个人中心 我的卡券列表
     *
     * @param yxCouponOrderQueryParam
     * @param uid
     * @return
     */
    @Override
    public List<YxCouponOrderQueryVo> getMyCouponOrderPageList(YxCouponOrderQueryParam yxCouponOrderQueryParam, int uid) {

        QueryWrapper<YxCouponOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).orderByDesc("create_time");

        switch (OrderStatusEnum.toType(yxCouponOrderQueryParam.getType())) {
            case STATUS_1://待付款
                wrapper.eq("status", 0).eq("refund_status", 0).eq("pay_staus", 0);
                break;
            case STATUS_2://待使用
                wrapper.eq("status", 4).eq("refund_status", 0).eq("pay_staus", 1);
                break;
            case STATUS_3://已使用
                wrapper.in("status", 5, 6).eq("refund_status", 0).eq("pay_staus", 1);
                break;
            case STATUS_4://已过期
                wrapper.eq("status", 1);
                break;
            case STATUS_MINUS_1://退款售后
                wrapper.in("status", 7, 8, 9);
                break;
        }

        Page<YxCouponOrder> pageModel = new Page<>(yxCouponOrderQueryParam.getPage(), yxCouponOrderQueryParam.getLimit());

        IPage<YxCouponOrder> pageList = yxCouponOrderMapper.selectPage(pageModel, wrapper);
        List<YxCouponOrderQueryVo> list = new ArrayList<>();
        for (YxCouponOrder item1 : pageList.getRecords()) {
            YxCouponOrderQueryVo item = new YxCouponOrderQueryVo();
            BeanUtils.copyBeanProp(item, item1);
            // 获取卡券list
            List<YxCouponOrderDetail> detailList = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().eq("order_id", item.getOrderId()));
            // 获取该订单购买的优惠券id
            Integer couponId = detailList.get(0).getCouponId();
            // 根据优惠券id获取优惠券信息
            YxCoupons yxCoupons = this.couponsService.getOne(new QueryWrapper<YxCoupons>().eq("", couponId));
            // 拼接有效期
            String expireDate = DateUtils.parseDateToStr(DateUtils.getDate(), yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.getDate(), yxCoupons.getExpireDateEnd());
            // 根据优惠券所属获取商户信息
            YxStoreInfo yxStoreInfo = this.storeInfoService.getById(yxCoupons.getBelong());
            List<YxCouponOrderDetailQueryVo> voList = new ArrayList<>();
            for (YxCouponOrderDetail yxCouponOrderDetail : detailList) {
                YxCouponOrderDetailQueryVo vo = new YxCouponOrderDetailQueryVo();
                BeanUtils.copyBeanProp(vo, yxCouponOrderDetail);
                // 有效期
                vo.setExpireDate(expireDate);
                // 卡券类型;1:代金券, 2:折扣券, 3:满减券
                vo.setCouponType(yxCoupons.getCouponType());
                // 代金券面额
                vo.setDenomination(yxCoupons.getDenomination());
                // 折扣券折扣率
                vo.setDiscount(yxCoupons.getDiscount());
                // 使用门槛
                vo.setThreshold(yxCoupons.getThreshold());
                // 优惠金额
                vo.setDiscountAmount(yxCoupons.getDiscountAmount());
                voList.add(vo);
            }

            item.setStoreName(yxStoreInfo.getStoreName());
            item.setDetailList(voList);
            list.add(item);
        }

        return list;
    }

    /**
     * 支付成功处理订单状态
     *
     * @param yxCouponOrder
     */
    @Override
    public void updatePaySuccess(YxCouponOrder yxCouponOrder) {
        yxCouponOrder.setPayStaus(OrderInfoEnum.PAY_STATUS_1.getValue());
        yxCouponOrder.setPayTime(OrderUtil.getSecondTimestampTwo());
        yxCouponOrder.setStatus(4);
        this.updateById(yxCouponOrder);

        List<YxCouponOrderDetail> list = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().eq("order_id", yxCouponOrder.getOrderId()));
        if (null == list || list.size() <= 0) {
            return;
        }
        for (YxCouponOrderDetail item : list) {
            item.setStatus(4);
        }
        this.yxCouponOrderDetailService.updateBatchById(list);
    }

    /**
     * 获取卡券订单详情
     *
     * @param id
     * @param location
     * @return
     */
    @Override
    public YxCouponOrderQueryVo getYxCouponOrderDetail(String id, String location) {
        YxCouponOrderQueryVo item = new YxCouponOrderQueryVo();
        BeanUtils.copyBeanProp(item, this.yxCouponOrderDetailService.getById(id));

        // 获取卡券list
        List<YxCouponOrderDetail> detailList = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().eq("order_id", item.getOrderId()));
        // 获取该订单购买的优惠券id
        Integer couponId = detailList.get(0).getCouponId();
        // 根据优惠券id获取优惠券信息
        YxCoupons yxCoupons = this.couponsService.getOne(new QueryWrapper<YxCoupons>().eq("", couponId));
        // 拼接有效期
        String expireDate = DateUtils.parseDateToStr(DateUtils.getDate(), yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.getDate(), yxCoupons.getExpireDateEnd());
        // 根据优惠券所属获取商户信息
        YxStoreInfo yxStoreInfo = this.storeInfoService.getById(yxCoupons.getBelong());
        List<YxCouponOrderDetailQueryVo> voList = new ArrayList<>();
        for (YxCouponOrderDetail yxCouponOrderDetail : detailList) {
            YxCouponOrderDetailQueryVo vo = new YxCouponOrderDetailQueryVo();
            BeanUtils.copyBeanProp(vo, yxCouponOrderDetail);
            // 有效期
            vo.setExpireDate(expireDate);
            // 卡券类型;1:代金券, 2:折扣券, 3:满减券
            vo.setCouponType(yxCoupons.getCouponType());
            // 代金券面额
            vo.setDenomination(yxCoupons.getDenomination());
            // 折扣券折扣率
            vo.setDiscount(yxCoupons.getDiscount());
            // 使用门槛
            vo.setThreshold(yxCoupons.getThreshold());
            // 优惠金额
            vo.setDiscountAmount(yxCoupons.getDiscountAmount());
            voList.add(vo);
        }

        // 店铺id
        item.setStoreId(yxStoreInfo.getId());
        // 店铺名称
        item.setStoreName(yxStoreInfo.getStoreName());
        // 店铺详细地址
        item.setStoreAddress(yxStoreInfo.getStoreAddress());
        // 店铺经纬度信息
        item.setCoordinate(yxStoreInfo.getCoordinate());
        item.setCoordinateX(yxStoreInfo.getCoordinateX());
        item.setCoordinateY(yxStoreInfo.getCoordinateY());
        // 计算当前位置距离店铺距离
        String[] locationArr = location.split(",");
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(yxStoreInfo.getCoordinateY()), Double.parseDouble(yxStoreInfo.getCoordinateX()));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
        double distance = DistanceMeterUtil.getDistanceMeter(source, target);
        item.setDistance(distance + "");
        // 卡卷详情
        item.setDetailList(voList);

        return item;
    }
}
