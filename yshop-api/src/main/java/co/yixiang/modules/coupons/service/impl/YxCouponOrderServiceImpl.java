package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.rocketmq.MqProducer;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.DistanceMeterUtil;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.MQConstant;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.*;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.entity.YxNowRate;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.commission.service.YxCustomizeRateService;
import co.yixiang.modules.commission.service.YxNowRateService;
import co.yixiang.modules.contract.mapper.YxContractTemplateMapper;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.CouponOrderMap;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.*;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.monitor.service.RedisService;
import co.yixiang.modules.order.web.dto.ComputeDTO;
import co.yixiang.modules.order.web.dto.CouponCacheDTO;
import co.yixiang.modules.order.web.dto.PriceGroupDTO;
import co.yixiang.modules.order.web.param.OrderParam;
import co.yixiang.modules.order.web.param.RefundParam;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.modules.ship.mapper.YxShipInfoMapper;
import co.yixiang.modules.ship.mapper.YxShipSeriesMapper;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.service.YxShipOperationDetailService;
import co.yixiang.modules.ship.service.YxShipOperationService;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.hyjf.framework.starter.recketmq.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


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

    @Autowired
    private YxUserBillService yxUserBillService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private YxCouponOrderUseService yxCouponOrderUseService;

    @Autowired
    private YxShipOperationService yxShipOperationService;

    @Autowired
    private YxShipOperationDetailService yxShipOperationDetailService;
    @Autowired
    private YxShipInfoService yxShipInfoService;

    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;
    @Autowired
    private YxNowRateService yxNowRateService;
    @Autowired
    private YxCommissionRateService commissionRateService;

    @Autowired
    private YxShipSeriesMapper yxShipSeriesMapper;
    @Autowired
    private YxShipInfoMapper yxShipInfoMapper;
    @Autowired
    private YxContractTemplateMapper yxContractTemplateMapper;

    @Autowired
    private YxShipPassengerService yxShipPassengerService;

    @Value("${yshop.snowflake.datacenterId}")
    private Integer datacenterId;

    @Autowired
    private MqProducer mqProducer;

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
        // 查询分享人id 没有分享人的话传值为0、查库肯定查不到数据
        YxUserQueryVo shareUserInfo = userService.getYxUserById(param.getSpread());

        CouponCacheDTO cacheDTO = getCacheOrderInfo(uid, key);
        if (ObjectUtil.isNull(cacheDTO)) {
            throw new ErrorRequestException("订单已过期,请刷新当前页面");
        }
        Integer couponId = cacheDTO.getYxCouponOrder().getCouponId();
        Integer totalNum = cacheDTO.getYxCouponOrder().getTotalNum();
        // 获取卡券信息
        YxCoupons yxCoupons = this.couponsService.getById(couponId);
        if (totalNum > yxCoupons.getInventory()) {
            throw new ErrorRequestException("库存不足");
        }
        Integer buyCount = this.getBuyCount(uid, couponId);
        // 校验购买数量是否超限
        buyCount = buyCount + totalNum;
        if (buyCount > yxCoupons.getQuantityLimit()) {
            throw new BadRequestException("当前购买卡券数量超限");
        }

        YxCoupons coupons = yxCouponsMapper.selectById(couponId);
        YxStoreInfo yxStoreInfo = this.storeInfoService.getById(coupons.getStoreId());
        Double totalPrice = cacheDTO.getPriceGroup().getTotalPrice();

        if (totalPrice <= 0) totalPrice = 0d;

        //生成分布式唯一值
        String orderSn = SnowflakeUtil.getOrderId(datacenterId);
        //组合数据
        YxCouponOrder couponOrder = new YxCouponOrder();
        couponOrder.setUid(uid);
        couponOrder.setOrderId(orderSn);
        // 订单存放用户昵称、真实姓名可能会不存在、无法区分订单所属
        couponOrder.setRealName(userInfo.getNickname());
        couponOrder.setUserPhone(StringUtils.isNotBlank(userInfo.getPhone()) ? userInfo.getPhone() : "");
        couponOrder.setTotalNum(totalNum);
        couponOrder.setTotalPrice(BigDecimal.valueOf(totalPrice));
        couponOrder.setPayStaus(OrderInfoEnum.PAY_STATUS_0.getValue());

        couponOrder.setPayType(param.getPayType());
        couponOrder.setMark(param.getMark());
        couponOrder.setCouponId(couponId);
        // 订单表存放核销的总次数
        couponOrder.setUseCount(NumberUtil.mul(coupons.getWriteOff(), totalNum).intValue());
        couponOrder.setCouponPrice(coupons.getSellingPrice());
        // 商户ID
        couponOrder.setMerId(yxStoreInfo.getMerId());
        // 推荐人id和类型
        couponOrder.setParentId(userInfo.getParentId());
        couponOrder.setParentType(userInfo.getParentType());
        // 分享人Id
        couponOrder.setShareId(param.getSpread());
        // 获取分享人的推荐人id和类型
        if (null != shareUserInfo) {
            couponOrder.setShareParentId(shareUserInfo.getParentId());
            couponOrder.setShareParentType(shareUserInfo.getParentType());
        }

        if (AppFromEnum.ROUNTINE.getValue().equals(param.getFrom())) {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_1.getValue());
        } else {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_0.getValue());
        }
        couponOrder.setCreateTime(DateTime.now());
        couponOrder.setUnique(key);
        // 购买时的分佣金额
        couponOrder.setCommission(NumberUtil.mul(yxCoupons.getCommission(), totalNum));
        // 分佣状态
        couponOrder.setRebateStatus(0);
        // 订单状态
        couponOrder.setStatus(0);
//        //处理门店 卡券为电子产品不需要判断自提
//        if (OrderInfoEnum.SHIPPIING_TYPE_2.getValue().equals(param.getShippingType())) {
//            YxSystemStoreQueryVo systemStoreQueryVo = systemStoreService.getYxSystemStoreById(param.getStoreId());
//            if (systemStoreQueryVo == null) throw new ErrorRequestException("暂无门店无法选择门店自提");
//            couponOrder.setVerifyCode(StrUtil.sub(orderSn, orderSn.length(), -12));
//        }

        //在线发票（0：不支持，1：支持）
        couponOrder.setOnlineInvoice(coupons.getOnlineInvoice());
        boolean res = save(couponOrder);
        if (!res) throw new ErrorRequestException("订单生成失败");

        int userStatus = 1;
        if (4 == coupons.getCouponType()) {
            //船票券 默认为0：不可用
            userStatus = 0;
        }
        // 插入detail表相关
        List<YxCouponOrderDetail> details = new ArrayList<>();
        for (int row = 0; row < totalNum; row++) {
            YxCouponOrderDetail couponOrderDetail = new YxCouponOrderDetail();
            couponOrderDetail.setOrderId(orderSn);
            couponOrderDetail.setUid(uid);
            couponOrderDetail.setCouponId(couponId);
            couponOrderDetail.setUseCount(coupons.getWriteOff());
            couponOrderDetail.setUsedCount(0);
            couponOrderDetail.setStatus(0);
            // 先用时间戳、扩展字段长度后再用uuid生成核销码
            String verifyCode = SnowflakeUtil.getOrderId(datacenterId);
            couponOrderDetail.setVerifyCode(verifyCode);
            couponOrderDetail.setRemark("");
            couponOrderDetail.setCreateUserId(uid);
            couponOrderDetail.setCreateTime(DateTime.now());
            //核销状态 0：不可用 1：可用
            couponOrderDetail.setUserStatus(userStatus);
            details.add(couponOrderDetail);
        }
        res = this.yxCouponOrderDetailService.saveBatch(details);
        if (!res) throw new ErrorRequestException("订单详情生成失败");
        //减库存加销量
//        coupons.setInventory(coupons.getInventory() - totalNum);
//        coupons.setSales(coupons.getSales() + totalNum);
//        res = this.couponsService.updateById(coupons);
        this.couponsService.updateAddSales(couponId, totalNum);
        if (!res) throw new ErrorRequestException("减库存加销量失败");

        //删除缓存
        delCacheOrderInfo(uid, key);
        //增加状态 (订单操作纪律, 卡券无此表)
//        orderStatusService.create(couponOrder.getId(), "cache_key_create_coupon_order", "订单生成");
        //使用MQ延时消息
        //mqProducer.sendMsg("yshop-topic",storeOrder.getId().toString());
        //log.info("投递延时订单id： [{}]：", storeOrder.getId());

        //加入redis，30分钟自动取消 订单取消放到batch扫描
//        String redisKey = String.valueOf(StrUtil.format("{}{}",
//                LocalLiveConstants.REDIS_COUPON_ORDER_OUTTIME_UNPAY, couponOrder.getId()));
//        redisTemplate.opsForValue().set(redisKey, couponOrder.getOrderId(),
//                LocalLiveConstants.ORDER_OUTTIME_UNPAY, TimeUnit.MINUTES);

        // add 保存购买时费率
        YxNowRate nowRate = setNowRateByCouponId(coupons, couponOrder);
        yxNowRateService.save(nowRate);

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
    public String cacheOrderInfo(int uid, List<YxCouponsQueryVo> couponsQueryVos, Integer quantity, PriceGroupDTO priceGroup) {
        String key = IdUtil.simpleUUID();
        CouponCacheDTO couponCacheDTO = new CouponCacheDTO();
        couponCacheDTO.setCouponsQueryVoList(couponsQueryVos);
        couponCacheDTO.setPriceGroup(priceGroup);
        YxCouponOrder yxCouponOrder = new YxCouponOrder();
        yxCouponOrder.setTotalNum(quantity);
        yxCouponOrder.setCouponId(couponsQueryVos.get(0).getId());
        couponCacheDTO.setYxCouponOrder(yxCouponOrder);
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

        // 等待对接微信公众号,以推送消息
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
        YxStoreInfo storeInfo = storeInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("id", yxCoupons.getStoreId()).eq("del_flag", 0));
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
        //if (order.getStatus() == 1) throw new ErrorRequestException("订单当前无法退款");

        // 根据卡券类型校验下是否可以退款
        YxCoupons yxCoupons = this.couponsService.getById(order.getCouponId());
        // 使用过了 不能退
        if (order.getUsedCount() > 0) {
            throw new ErrorRequestException("已核销过的订单无法退款");
        }
        // 过期不过期   是否过期退  是否随时退
        if (0 == yxCoupons.getAwaysRefund()) {
            // 都不支持不可退款
            if (0 == yxCoupons.getOuttimeRefund()) {
                throw new ErrorRequestException("该订单卡券不支持退款");
            } else {
                // 支持过期退、判断没有过期不可退款
                LocalDateTime expireDate = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
                if (expireDate.isAfter(LocalDateTime.now())) {
                    throw new ErrorRequestException("当前卡券未过期，请及时使用");
                }
            }
        }

        YxCouponOrder storeOrder = new YxCouponOrder();
        storeOrder.setRefundStatus(OrderInfoEnum.REFUND_STATUS_1.getValue());
        storeOrder.setRefundReasonTime(OrderUtil.getSecondTimestampTwo());
        storeOrder.setRefundReasonWapExplain(param.getRefund_reason_wap_explain());
        storeOrder.setRefundReasonWap(param.getText());
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
                wrapper.in("status", 4, 5).eq("refund_status", 0).eq("pay_staus", 1).eq("refund_status", 0);
                break;
            case STATUS_3://已使用
                wrapper.eq("status", 6).eq("refund_status", 0).eq("pay_staus", 1).eq("refund_status", 0);
                break;
            case STATUS_4://已过期
                wrapper.eq("status", 1);
                break;
            case STATUS_5:// 待评价
                wrapper.eq("status", 6).eq("evaluate", 0);
                break;
            case STATUS_MINUS_1://退款售后
                wrapper.in("refund_status", 1, 2);
                break;
            case STATUS_MINUS_2://已取消
                wrapper.eq("status", 10);
                break;
            default:
                throw new BadRequestException("接口异常");
        }

        Page<YxCouponOrder> pageModel = new Page<>(yxCouponOrderQueryParam.getPage(), yxCouponOrderQueryParam.getLimit());
        IPage<YxCouponOrder> pageList = yxCouponOrderMapper.selectPage(pageModel, wrapper);
        if (null == pageList.getRecords() || pageList.getRecords().size() <= 0) {
            return new ArrayList<YxCouponOrderQueryVo>();
        }
        List<YxCouponOrderQueryVo> list = new ArrayList<>();
        for (YxCouponOrder item1 : pageList.getRecords()) {
            YxCouponOrderQueryVo item = new YxCouponOrderQueryVo();
            // 卡券缩略图
            YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item1.getCouponId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                    .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));

            if (thumbnail != null) {
                item.setImage(thumbnail.getImgUrl());
            }

            BeanUtils.copyBeanProp(item, item1);
            // 获取该订单购买的优惠券id
            // 根据优惠券id获取优惠券信息
            YxCoupons yxCoupons = this.couponsService.getOne(new QueryWrapper<YxCoupons>().eq("id", item1.getCouponId()));
            // 拼接有效期
            String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd());
            item.setExpireDate(expireDate);
            item.setAvailableTime(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
            // 根据优惠券所属获取商户信息
            YxStoreInfo yxStoreInfo = this.storeInfoService.getById(yxCoupons.getStoreId());
            // 有效期
            item.setExpireDate(expireDate);
            // 卡券类型;1:代金券, 2:折扣券, 3:满减券.4:船票券
            item.setCouponType(yxCoupons.getCouponType());
            // 优惠券名称
            item.setCouponName(yxCoupons.getCouponName());
            item.setStoreName(yxStoreInfo.getStoreName());
            item.setCreateTimeStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getCreateTime()));
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

        YxUser yxUser = this.userService.getById(yxCouponOrder.getUid());
        // 插入资金明细
        YxUserBill yxUserBill = new YxUserBill();
        yxUserBill.setUid(yxCouponOrder.getUid());
        yxUserBill.setLinkId(yxCouponOrder.getOrderId());
        yxUserBill.setPm(0);
        yxUserBill.setTitle("小程序本地生活购买");
        yxUserBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        yxUserBill.setType(BillDetailEnum.TYPE_8.getValue());
        yxUserBill.setNumber(yxCouponOrder.getTotalPrice());
        // 目前只支持微信付款、没有余额
        yxUserBill.setBalance(yxUser.getNowMoney());
        yxUserBill.setAddTime(DateUtils.getNowTime());
        yxUserBill.setStatus(1);
        yxUserBill.setMerId(yxCouponOrder.getMerId());

        yxUserBill.setUserType(3);
        yxUserBill.setUsername(yxUser.getNickname());
        this.yxUserBillService.save(yxUserBill);
        // 商户收入在核销第一次的时候更新

        // 判断用户是否是分销客、不是更新成分销客
        if (0 == yxUser.getUserRole()) {
            YxUser updateUser = new YxUser();
            updateUser.setUid(yxUser.getUid());
            updateUser.setUserRole(1);
            this.userService.updateById(updateUser);
        }
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
        YxCouponOrder yxCouponOrder = this.yxCouponOrderMapper.selectById(id);
        BeanUtils.copyBeanProp(item, yxCouponOrder);
        // 获取卡券list
        List<YxCouponOrderDetail> detailList = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, item.getOrderId()).eq(YxCouponOrderDetail::getUserStatus, 1));
       /* if (CollectionUtils.isEmpty(detailList)) {
            throw new BadRequestException("根据订单号:" + item.getOrderId() + " 未查询到卡券订单信息");
        }*/
//        Integer couponId = detailList.get(0).getCouponId();
        Integer couponId = yxCouponOrder.getCouponId();

        // 卡券缩略图
        YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", couponId).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));

        if (thumbnail != null) {
            item.setImage(thumbnail.getImgUrl());
        }
        // 根据优惠券id获取优惠券信息
        YxCoupons yxCoupons = this.couponsService.getOne(new QueryWrapper<YxCoupons>().lambda().eq(YxCoupons::getId, couponId));
        // 拼接有效期
        String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd());
        item.setExpireDate(expireDate);
        item.setAvailableTime(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
        // 根据优惠券所属获取商户信息
        YxStoreInfo yxStoreInfo = this.storeInfoService.getById(yxCoupons.getStoreId());
        List<YxCouponOrderDetailQueryVo> voList = new ArrayList<>();
        int shipOrderStatus = 1;

        // 船只订单还有未选择乘客的订单数量
        int userCount = this.yxCouponOrderDetailService.count(new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, item.getOrderId()).eq(YxCouponOrderDetail::getUserStatus, 0));
        if (userCount > 0) {
            // 状态(0：展示立即使用，1：不展示)
            shipOrderStatus = 0;
        }
        item.setOptionalNum(userCount);

        if (!CollectionUtils.isEmpty(detailList)) {
            for (YxCouponOrderDetail yxCouponOrderDetail : detailList) {
                YxCouponOrderDetailQueryVo vo = new YxCouponOrderDetailQueryVo();
                BeanUtils.copyBeanProp(vo, yxCouponOrderDetail);
                // 有效期
                vo.setExpireDate(expireDate);
                // 卡券类型;1:代金券, 2:折扣券, 3:满减券,4:船票券
                vo.setCouponType(yxCoupons.getCouponType());
                // 代金券面额
                vo.setDenomination(yxCoupons.getDenomination());
                // 折扣券折扣率
                vo.setDiscount(yxCoupons.getDiscount());
                // 使用门槛
                vo.setThreshold(yxCoupons.getThreshold());
                // 优惠金额
                vo.setDiscountAmount(yxCoupons.getDiscountAmount());
                // 核销码加密
                vo.setVerifyCode(Base64Utils.encode(vo.getVerifyCode() + "," + vo.getUid()));
                voList.add(vo);
            }
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
        if (StringUtils.isNotBlank(location) && location.split(",").length == 2 && StringUtils.isNotBlank(yxStoreInfo.getCoordinateY()) && StringUtils.isNotBlank(yxStoreInfo.getCoordinateX())) {
            String[] locationArr = location.split(",");
            GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(yxStoreInfo.getCoordinateY()), Double.parseDouble(yxStoreInfo.getCoordinateX()));
            GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
            double distance = DistanceMeterUtil.getDistanceMeter(source, target);
            item.setDistance(new BigDecimal(distance).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).toString() + "km");
        } else {
            item.setDistance("");
        }
        // 卡卷详情
        item.setDetailList(voList);
        // 券面信息
        item.setYxCoupons(yxCoupons);
        item.setBuyTime("");
        if (null != yxCouponOrder.getPayTime()) {
            // 购买时间
            item.setBuyTime(DateUtils.timestampToStr10(yxCouponOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        // 店铺缩略图
        YxImageInfo storeImage = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", yxStoreInfo.getId()).eq("img_type", ShopConstants.IMG_TYPE_STORE)
                .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));

        if (storeImage != null) {
            item.setStoreImage(storeImage.getImgUrl());
        }
        //卡券类型;1:代金券, 2:折扣券, 3:满减券，4:船票券
        item.setCouponType(yxCoupons.getCouponType());
        //船只信息
        if (4 == yxCoupons.getCouponType()) {
            item = setShipDetailInof(item, yxCoupons, shipOrderStatus);
            item.setPassengeList(yxShipPassengerService.getPassengerByOrderId(item.getId()));
        }
        return item;
    }

    private YxCouponOrderQueryVo setShipDetailInof(YxCouponOrderQueryVo item, YxCoupons yxCoupons, int shipOrderStatus) {
        //船票券
        YxShipSeriesQueryVo yxShipSeriesQueryVo = yxShipSeriesMapper.getYxShipSeriesById(yxCoupons.getSeriesId());
        YxShipInfoQueryVo yxShipInfoQueryVo = yxShipInfoMapper.getYxShipInfoById(yxCoupons.getShipId());
        //合同模板
        if (0 != yxCoupons.getTempId()) {
            YxContractTemplateQueryVo yxContractTemplateQueryVo = yxContractTemplateMapper.getYxContractTemplateById(yxCoupons.getTempId());
            if (null == yxContractTemplateQueryVo) {
                throw new BadRequestException("船票券 卡券id：" + yxCoupons.getId() + " 获取合同模板信息失败！tempId = " + yxCoupons.getTempId());
            }
            //合同模板信息
            item.setTempFilePath(yxContractTemplateQueryVo.getFilePath());
            item.setTempName(yxContractTemplateQueryVo.getTempName());
        }

        if (null == yxShipSeriesQueryVo) {
            throw new BadRequestException("船票券 卡券id：" + yxCoupons.getId() + " 获取船只系列信息失败！seriesId " + yxCoupons.getSeriesId());
        }
        if (null == yxShipInfoQueryVo) {
            throw new BadRequestException("船票券 卡券id：" + yxCoupons.getId() + " 获取船只信息失败！shipId" + yxCoupons.getShipId());
        }

        item.setRideLimit(yxCoupons.getPassengersNum());
        //船只名称
        item.setShipName(yxShipInfoQueryVo.getShipName());
        //船只坐标
        item.setShipCoordinate(yxShipSeriesQueryVo.getCoordinate());
        item.setShipCoordinateX(yxShipSeriesQueryVo.getCoordinateX());
        item.setShipCoordinateY(yxShipSeriesQueryVo.getCoordinateY());
        item.setShipAddress(yxShipSeriesQueryVo.getShipAddress());
        item.setSeriesName(yxShipSeriesQueryVo.getSeriesName());
        //健康确认
        if (StringUtils.isNotBlank(yxCoupons.getConfirmation())) {
            List<String> stringList = Arrays.asList(yxCoupons.getConfirmation().split(","));
            item.setConfirmationList(stringList);
        }

        //
        item.setShipOrderStatus(shipOrderStatus);
        //
        item.setUserPhone(yxShipInfoQueryVo.getManagerPhone());
        //

        return item;
    }


    /**
     * 计算卡券各种订单数量
     *
     * @param uid
     * @return
     */
    @Override
    public OrderCountVO orderData(int uid) {
        OrderCountVO countVO = new OrderCountVO();
        // 待付款数量
        QueryWrapper<YxCouponOrder> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("uid", uid);
        wrapper1.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper1.eq("status", 0).eq("refund_status", 0).eq("pay_staus", 0);
        countVO.setWaitPayCount(this.count(wrapper1));

        // 待使用数量
        QueryWrapper<YxCouponOrder> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("uid", uid);
        wrapper2.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper2.in("status", 4, 5).eq("refund_status", 0).eq("pay_staus", 1);
        countVO.setWaitUseCount(this.count(wrapper2));

        // 已使用数量
        QueryWrapper<YxCouponOrder> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("uid", uid);
        wrapper3.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper3.eq("status", 6).eq("refund_status", 0).eq("pay_staus", 1);
        countVO.setUsedCount(this.count(wrapper3));

        // 退款数量
        QueryWrapper<YxCouponOrder> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("uid", uid);
        wrapper4.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper4.in("refund_status", 1, 2);
        countVO.setRefundCount(this.count(wrapper4));

        // 累计订单数量 已支付 未退款的数量
        QueryWrapper<YxCouponOrder> wrapper5 = new QueryWrapper<>();
        wrapper5.eq("uid", uid).eq("refund_status", 0).eq("pay_staus", 1);
        countVO.setTotalCount(this.count(wrapper5));

        // 总消费
        QueryWrapper<YxCouponOrder> wrapper6 = new QueryWrapper<>();
        wrapper6.eq("uid", uid);
        wrapper6.eq("refund_status", 0).eq("pay_staus", 1);
        wrapper6.select("ifnull(sum(total_price),0) as total ");
        Map<String, Object> map = this.getMap(wrapper6);
        countVO.setSumPrice(new BigDecimal(String.valueOf(map.get("total"))));

        // 已过期
        QueryWrapper<YxCouponOrder> wrapper7 = new QueryWrapper<>();
        wrapper7.eq("uid", uid);
        wrapper7.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper7.eq("status", 1);
        countVO.setOutTimeCount(this.count(wrapper7));

        // 已取消
        QueryWrapper<YxCouponOrder> wrapper8 = new QueryWrapper<>();
        wrapper8.eq("uid", uid);
        wrapper8.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue());
        wrapper8.eq("status", 10);
//        countVO.setOutTimeCount(this.count(wrapper8));
        countVO.setCancelCount(this.count(wrapper8));

        // 已待评价
        QueryWrapper<YxCouponOrder> wrapper9 = new QueryWrapper<>();
        wrapper9.lambda().eq(YxCouponOrder::getUid, uid);
        wrapper9.lambda().eq(YxCouponOrder::getDelFlag, CommonEnum.DEL_STATUS_0.getValue());
        wrapper9.lambda().eq(YxCouponOrder::getStatus, 6);
        wrapper9.lambda().eq(YxCouponOrder::getEvaluate, 0);
        countVO.setWaitReplyCount(this.count(wrapper9));

        return countVO;
    }

    /**
     * 取消订单并回滚卡券数量
     *
     * @param id
     * @return
     */
    @Override
    public boolean updateOrderStatusCancel(String id) {
        // 查询卡券订单信息
        YxCouponOrder yxCouponOrder = this.getById(id);
        if (null == yxCouponOrder) {
            throw new BadRequestException("未查询到订单信息");
        }
        if (10 == yxCouponOrder.getStatus()) {
            throw new BadRequestException("订单已是取消状态");
        }
        // 更新卡券信息
        yxCouponOrder.setStatus(10);
        yxCouponOrder.setUpdateTime(new Date());
        this.updateById(yxCouponOrder);
        // 查询卡券信息
        YxCoupons yxCoupons = this.couponsService.getById(yxCouponOrder.getCouponId());
        if (null == yxCoupons) {
            throw new BadRequestException("未查询到卡券信息");
        }
        Integer mulCount = yxCouponOrder.getTotalNum();
        if (yxCoupons.getSales() < yxCouponOrder.getTotalNum()) {
            log.info("卡券id：" + yxCouponOrder.getCouponId() + "销量不足扣减、置0");
            mulCount = yxCoupons.getSales();
        }
        this.couponsService.updateMulSales(yxCouponOrder.getCouponId(), mulCount);
        return true;
    }

    /**
     * 卡券核销
     *
     * @param decodeVerifyCode
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> updateCouponOrder(String decodeVerifyCode, int uid, boolean isAll) {
        // 校验优惠券信息
        Map<String, Object> map = this.couponCheck(decodeVerifyCode, uid, null, null);
        if (map.containsKey("status")) {
            return map;
        }
        YxStoreInfo yxStoreInfo = (YxStoreInfo) map.get("yxStoreInfo");
        YxCouponOrder yxCouponOrder = (YxCouponOrder) map.get("yxCouponOrder");
        YxCouponOrderDetail yxCouponOrderDetail = (YxCouponOrderDetail) map.get("yxCouponOrderDetail");


        // 第一次核销发送分佣mq
        boolean isFirst = false;
        if (0 == yxCouponOrder.getUsedCount() && 0 == yxCouponOrder.getRebateStatus()) {
            isFirst = true;
        }

        // 处理订单表数据
        int usedCount = updateCouponOrder(isAll, yxCouponOrder, yxCouponOrderDetail);
        // 处理店铺核销数据
        updateOrderUse(uid, yxStoreInfo, yxCouponOrder, yxCouponOrderDetail, usedCount);

        if (isFirst) {
            updateMerInfo(yxCouponOrder);
        }

        if (isFirst) {
            // 更新商户余额
            updateMerInfo(yxCouponOrder);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", "1");
        result.put("usedCount", usedCount);
        result.put("statusDesc", "核销成功");
        return result;
    }

    /**
     * 手动核销卡券(废)
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
            throw new BadRequestException("无卡券订单信息");
        }
        if (1 == yxCouponOrder.getRefundStatus()) {
            throw new BadRequestException("卡券退款中不可用");
        }
        // 判断总核销次数
        if (yxCouponOrder.getUseCount() <= yxCouponOrder.getUsedCount()) {
            throw new BadRequestException("该订单卡券已全部核销");
        }

        // 查询优惠券信息
        YxCoupons yxCoupons = this.couponsService.getById(yxCouponOrder.getCouponId());
        if (null == yxCoupons) {
            throw new BadRequestException("卡券不存在，无法核销");
        }
        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getStoreId().equals(uid)) {
            throw new BadRequestException("非本商铺卡券，无法核销");
        }
        YxStoreInfo yxStoreInfo = this.storeInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", yxCoupons.getStoreId()));
        if (null == yxStoreInfo) {
            throw new BadRequestException("店铺信息不存在");
        }

        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
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
            // 卡券核销mq
            mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_COMMISSION_TAG, UUID.randomUUID().toString(), jsonObject));
        }
        return true;
    }

    /**
     * 查询用户易购面张数
     *
     * @param uid
     * @param couponId
     * @return
     */
    @Override
    public Integer getBuyCount(int uid, Integer couponId) {
        Integer count = 0;
        List<YxCouponOrder> list = this.list(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getUid, uid).eq(YxCouponOrder::getCouponId, couponId));
        for (YxCouponOrder item : list) {
            //0:待付款 4:待使用5:已使用6:已核销
            if (0 != item.getStatus() && 4 != item.getStatus() && 5 != item.getStatus() && 6 != item.getStatus()) {
                continue;
            }
            count = count + item.getTotalNum();
        }
        return count;
    }

    /**
     * 船票核销操作
     *
     * @param decodeVerifyCode
     * @param uid
     * @param shipId
     * @param shipUserId
     * @return
     */
    @Override
    public Map<String, Object> updateShipCouponOrder(String decodeVerifyCode, int uid, Integer shipId, Integer shipUserId, SystemUser user) {
        // 校验优惠券信息
        Map<String, Object> map = this.couponCheck(decodeVerifyCode, uid, shipId, shipUserId);
        if (map.containsKey("status")) {
            return map;
        }
        YxStoreInfo yxStoreInfo = (YxStoreInfo) map.get("yxStoreInfo");
        YxCouponOrder yxCouponOrder = (YxCouponOrder) map.get("yxCouponOrder");
        YxCouponOrderDetail yxCouponOrderDetail = (YxCouponOrderDetail) map.get("yxCouponOrderDetail");

        // 处理船只相关操作
        map.put("shipId", shipId);
        map.put("shipUserId", shipUserId);
        map.put("user", user);
        this.updateSuccessShipInfo(map);


        // 第一次核销发送分佣mq
        boolean isFirst = false;
        if (0 == yxCouponOrder.getUsedCount() && 0 == yxCouponOrder.getRebateStatus()) {
            isFirst = true;
        }
        // 处理订单表数据
        int usedCount = updateCouponOrder(true, yxCouponOrder, yxCouponOrderDetail);
        // 处理店铺核销数据
        updateOrderUse(uid, yxStoreInfo, yxCouponOrder, yxCouponOrderDetail, usedCount);

        if (isFirst) {
            updateMerInfo(yxCouponOrder);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", "1");
        result.put("usedCount", usedCount);
        result.put("statusDesc", "核销成功");
        return result;

    }

    // 处理店铺核销数据
    private void updateOrderUse(int uid, YxStoreInfo yxStoreInfo, YxCouponOrder yxCouponOrder, YxCouponOrderDetail yxCouponOrderDetail, int usedCount) {
        // 处理店铺核销数据
        YxCouponOrderUse yxCouponOrderUse = new YxCouponOrderUse();
        yxCouponOrderUse.setCouponId(yxCouponOrderDetail.getCouponId());
        yxCouponOrderUse.setOrderId(yxCouponOrder.getOrderId());
        yxCouponOrderUse.setStoreId(yxStoreInfo.getId());
        yxCouponOrderUse.setStoreName(yxStoreInfo.getStoreName());
        yxCouponOrderUse.setUsedCount(usedCount);
        yxCouponOrderUse.setDelFlag(0);
        yxCouponOrderUse.setCreateUserId(uid);

        // 数据入库
        this.yxCouponOrderUseService.save(yxCouponOrderUse);
    }

    private void updateMerInfo(YxCouponOrder yxCouponOrder) {
        // 更新商户余额
        SystemUser systemUser = this.systemUserService.getById(yxCouponOrder.getMerId());
        if (null != systemUser) {
            // 该笔资金实际到账
            SystemUser updateSystemUser = new SystemUser();
            BigDecimal truePrice = yxCouponOrder.getTotalPrice().subtract(yxCouponOrder.getCommission());
            updateSystemUser.setId(systemUser.getId());
            updateSystemUser.setTotalAmount(truePrice);
            updateSystemUser.setWithdrawalAmount(truePrice);
            this.systemUserService.updateUserTotal(updateSystemUser);

            // 插入商户资金明细
            YxUserBill merBill = new YxUserBill();
            merBill.setUid(yxCouponOrder.getMerId());
            merBill.setLinkId(yxCouponOrder.getOrderId());
            merBill.setPm(1);
            merBill.setTitle("本地生活返现");
            merBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
            merBill.setType(BillDetailEnum.TYPE_8.getValue());
            merBill.setNumber(truePrice);
            // 目前只支持微信付款、没有余额
            merBill.setBalance(systemUser.getWithdrawalAmount().add(truePrice));
            merBill.setAddTime(DateUtils.getNowTime());
            merBill.setStatus(1);
            merBill.setMerId(yxCouponOrder.getMerId());
            merBill.setUserType(1);
            merBill.setUsername(systemUser.getNickName());
            this.yxUserBillService.save(merBill);
        }

        // 分佣mq发送
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", yxCouponOrder.getOrderId());
        jsonObject.put("orderType", "1");
        mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_COMMISSION_TAG, UUID.randomUUID().toString(), jsonObject));
    }

    // 修改订单状态
    private int updateCouponOrder(boolean isAll, YxCouponOrder yxCouponOrder, YxCouponOrderDetail yxCouponOrderDetail) {
        int usedCount = 1;
        List<YxCouponOrderDetail> yxCouponOrderDetails = new ArrayList<>();
        if (isAll) {
            usedCount = 0;
            // 全部核销 查询所有已经能用的详情  user_status 核销状态 0：不可用 1：可用
            yxCouponOrderDetails = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().eq("order_id", yxCouponOrder.getOrderId()).eq("user_status", 1));
            for (YxCouponOrderDetail item : yxCouponOrderDetails) {
                // 核销次数只记录剩余次数
                usedCount = usedCount + item.getUseCount() - item.getUsedCount();
            }
        } else {
            // 单次核销只存放传进来的用核销码查询到的卡券详情
            yxCouponOrderDetails.add(yxCouponOrderDetail);
        }
        // 已用次数+本次使用次数、全部核销的话计算完used应该与use相等
        yxCouponOrder.setUsedCount(yxCouponOrder.getUsedCount() + usedCount);
        if (yxCouponOrder.getUsedCount().equals(yxCouponOrder.getUseCount())) {
            // 次数全部使用完成的状态更新为已核销
            yxCouponOrder.setStatus(6);
        } else {
            yxCouponOrder.setStatus(5);
        }
        // 数据入库
        this.updateById(yxCouponOrder);
        // 处理订单详情表数据
        for (YxCouponOrderDetail item : yxCouponOrderDetails) {
            if (isAll) {
                // 全部核销
                item.setUsedCount(item.getUseCount());
            } else {
                // 单个核销
                item.setUsedCount(item.getUsedCount() + 1);
            }

            if (item.getUsedCount().equals(item.getUseCount())) {
                // 次数全部使用完成的状态更新为已核销
                item.setStatus(6);
            } else {
                item.setStatus(5);
            }
            // 数据入库
            this.yxCouponOrderDetailService.updateById(yxCouponOrderDetail);
        }
        return usedCount;
    }

    // 处理船只操作
    private void updateSuccessShipInfo(Map<String, Object> map) {
        // 先查询这个船只有没有已经分配了   yx_ship_operation_detail
        Integer shipId = (Integer) map.get("shipId");
        Integer shipUserId = (Integer) map.get("shipUserId");
        YxShipOperation yxShipOperation = yxShipOperationService.getShipOperationBySidUid(shipId, shipUserId);
        if (yxShipOperation == null) {
            // 没有分配  就新增一个
            yxShipOperation = yxShipOperationService.insertYxShipOperation(map);
        }
        // 插入详情表
        map.put("yxShipOperation", yxShipOperation);
        YxShipOperationDetail shipOperationDetail = yxShipOperationDetailService.saveShipOperationDetail(map);
        yxShipOperation.setTotalPassenger(yxShipOperation.getTotalPassenger() + shipOperationDetail.getTotalPassenger());
        // 老年人人数
        yxShipOperation.setOldPassenger(yxShipOperation.getOldPassenger() + shipOperationDetail.getOldPassenger());
        // 未成年人数
        yxShipOperation.setUnderagePassenger(yxShipOperation.getUnderagePassenger() + shipOperationDetail.getUnderagePassenger());
        // 修改乘坐人数量之类的
        yxShipOperationService.updateById(yxShipOperation);
    }

    // 校验卡券状态
    private Map couponCheck(String decodeVerifyCode, int uid, Integer shipId, Integer shipUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "99");
        String[] decode = decodeVerifyCode.split(",");
        if (decode.length != 2) {
            map.put("statusDesc", "无效核销码");
            return map;
        }
        // 获取核销码
        String verifyCode = decode[0];
        // 获取核销用户的id
        String useUid = decode[1];
        YxCouponOrderDetail yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        if (null == yxCouponOrderDetail) {
            map.put("statusDesc", "无卡券订单详情信息");
            return map;
        }
        YxCouponOrder yxCouponOrder = this.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", yxCouponOrderDetail.getOrderId()));
        if (null == yxCouponOrder) {
            map.put("statusDesc", "无卡券订单信息");
            return map;
        }
        // 判断订单状态
        if (4 != yxCouponOrder.getStatus() && 5 != yxCouponOrder.getStatus()) {
            map.put("statusDesc", "当前卡券不可用");
            return map;
        }
        if (1 == yxCouponOrder.getRefundStatus()) {
            map.put("statusDesc", "卡券退款中不可用");
            return map;
        }
        if (!useUid.equals(yxCouponOrder.getUid() + "")) {
            map.put("statusDesc", "卡券所属验证失败");
            return map;
        }
        // 查询优惠券信息
        YxCoupons yxCoupons = this.couponsService.getById(yxCouponOrderDetail.getCouponId());
        if (null == yxCoupons) {
            map.put("statusDesc", "卡券不存在");
            return map;
        }
        SystemUser systemUser = this.systemUserService.getById(uid);
        if (null == systemUser) {
            map.put("statusDesc", "核销用户异常");
            return map;
        }
        YxStoreInfo yxStoreInfo = this.storeInfoService.getOne(new QueryWrapper<YxStoreInfo>().lambda().eq(YxStoreInfo::getId, systemUser.getStoreId()));
        if (null == yxStoreInfo) {
            map.put("statusDesc", "卡券店铺异常");
            return map;
        }
        // 判断是否本商铺发放的卡券  11.7修改  校验卡券所属店铺和用户所属店铺是否一致
        if (!yxCoupons.getStoreId().equals(yxStoreInfo.getId())) {
            map.put("statusDesc", "非本商户卡券不可用");
            return map;
        }
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            map.put("statusDesc", "当前卡券已达核销上限");
            return map;
        }
        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
        if (expireDateStart.isAfter(LocalDateTime.now())) {
            map.put("statusDesc", "当前卡券未到使用时间");
            return map;
        }
        if (expireDateEnd.isBefore(LocalDateTime.now())) {

            map.put("statusDesc", "卡券已过期");
            return map;
        }
        // 判断卡券状态
        if (4 != yxCouponOrderDetail.getStatus() && 5 != yxCouponOrderDetail.getStatus()) {
            map.put("statusDesc", "当前卡券不可用");
            return map;
        }
        // 校验船只信息  和船长信息 乘客信息等
        if (SystemConfigConstants.COUPON_TYPE_CP == yxCoupons.getCouponType().intValue()) {
            // 如果是船票券
            YxShipInfo shipInfo = yxShipInfoService.getById(shipId);
            SystemUser captainUser = systemUserService.getById(shipUserId);
            if (shipInfo == null) {
                map.put("statusDesc", "未查询到对应船只");
                return map;
            }
            if (captainUser == null) {
                map.put("statusDesc", "未查询到对应船长");
                return map;
            }
            if (shipInfo.getCurrentStatus().intValue() == 1) {
                map.put("statusDesc", "船只已离港");
                return map;
            }
            if (shipInfo.getCurrentStatus().intValue() == 2) {
                map.put("statusDesc", "船只维修中");
                return map;
            }
            map.put("shipInfo", shipInfo);
            map.put("captainUser", captainUser);
        }

        // 如果校验没问题了  就放进去
        map.remove("status");
        map.put("yxCoupons", yxCoupons);
        map.put("yxCouponOrder", yxCouponOrder);
        map.put("yxCouponOrderDetail", yxCouponOrderDetail);
        map.put("yxStoreInfo", yxStoreInfo);
        return map;
    }

    private YxNowRate setNowRateByCouponId(YxCoupons yxCoupons, YxCouponOrder couponOrder) {
        //
        YxNowRate nowRate = new YxNowRate();
        //本地生活
        nowRate.setRateType(1);
        nowRate.setOrderId(couponOrder.getOrderId());
        switch (yxCoupons.getCustomizeType()) {
            case 0:
                YxCommissionRate commissionRate = commissionRateService.getOne(new QueryWrapper<YxCommissionRate>().eq("del_flag", 0));
                BeanUtils.copyProperties(commissionRate, nowRate);
                break;
            case 1:
                break;
            case 2:
                YxCustomizeRate yxCustomizeRate = yxCustomizeRateService.getCustomizeRateByParam(0, yxCoupons.getId());
                BeanUtils.copyProperties(yxCustomizeRate, nowRate);
                break;
        }
        nowRate.setDelFlag(0);
        nowRate.setCreateUserId(couponOrder.getCreateUserId());
        nowRate.setCreateTime(DateTime.now());
        nowRate.setUpdateUserId(couponOrder.getCreateUserId());
        nowRate.setUpdateTime(DateTime.now());
        return nowRate;
    }

}
