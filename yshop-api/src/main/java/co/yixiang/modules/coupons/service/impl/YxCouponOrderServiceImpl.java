package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.CouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.monitor.service.RedisService;
import co.yixiang.modules.order.mapping.OrderMap;
import co.yixiang.modules.order.web.dto.CouponCacheDTO;
import co.yixiang.modules.order.web.dto.PriceGroupDTO;
import co.yixiang.modules.order.web.param.OrderParam;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
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
    private YxSystemConfigService systemConfigService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private YxUserService userService;

    @Autowired
    private YxUserAddressService userAddressService;

    @Autowired
    private YxSystemStoreService systemStoreService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderMap orderMap;

    @Autowired
    private YxUserBillService billService;

    @Override
    public YxCouponOrderQueryVo getYxCouponOrderById(Serializable id) throws Exception{
        return yxCouponOrderMapper.getYxCouponOrderById(id);
    }

    @Override
    public Paging<YxCouponOrderQueryVo> getYxCouponOrderPageList(YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception{
        Page page = setPageParam(yxCouponOrderQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponOrderQueryVo> iPage = yxCouponOrderMapper.getYxCouponOrderPageList(page,yxCouponOrderQueryParam);
        return new Paging(iPage);
    }


    @Override
    public PriceGroupDTO getOrderPriceGroup(CouponOrderQueryVo couponOrderQueryVo){
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
        if (ObjectUtil.isNull(userInfo)) throw new ErrorRequestException("用户不存在");

        CouponCacheDTO cacheDTO = getCacheOrderInfo(uid, key);
        if (ObjectUtil.isNull(cacheDTO)) {
            throw new ErrorRequestException("订单已过期,请刷新当前页面");
        }

        Double totalPrice = cacheDTO.getPriceGroup().getTotalPrice();
        Double payPrice = cacheDTO.getPriceGroup().getTotalPrice();
        Double payPostage = cacheDTO.getPriceGroup().getStorePostage();

        YxUserAddressQueryVo userAddress = null;
        if (OrderInfoEnum.SHIPPIING_TYPE_1.getValue().equals(param.getShippingType())) {
            if (StrUtil.isEmpty(param.getAddressId())) throw new ErrorRequestException("请选择收货地址");
            userAddress = userAddressService.getYxUserAddressById(param.getAddressId());
            if (ObjectUtil.isNull(userAddress)) throw new ErrorRequestException("地址选择有误");
        } else { //门店
            if (StrUtil.isBlank(param.getRealName()) || StrUtil.isBlank(param.getPhone())) {
                throw new ErrorRequestException("请填写姓名和电话");
            }
            userAddress = new YxUserAddressQueryVo();
            userAddress.setRealName(param.getRealName());
            userAddress.setPhone(param.getPhone());
            userAddress.setProvince("");
            userAddress.setCity("");
            userAddress.setDistrict("");
            userAddress.setDetail("");
        }

        Integer totalNum = 0;
        Integer gainIntegral = 0;
        List<String> cartIds = new ArrayList<>();
        int combinationId = 0;
        int seckillId = 0;
        int bargainId = 0;

//        for (YxStoreCartQueryVo cart : cartInfo) {
//            yxStoreCartService.checkProductStock(uid, cart.getProductId(), cart.getCartNum(),
//                    cart.getProductAttrUnique(), cart.getCombinationId(), cart.getSeckillId(), cart.getBargainId());
//
//
//            combinationId = cart.getCombinationId();
//            seckillId = cart.getSeckillId();
//            bargainId = cart.getBargainId();
//            cartIds.add(cart.getId().toString());
//            totalNum += cart.getCartNum();
//            //计算积分
//            BigDecimal cartInfoGainIntegral = BigDecimal.ZERO;
//            if (combinationId == 0 && seckillId == 0 && bargainId == 0) {//拼团等活动不参与积分
//                if (cart.getProductInfo().getGiveIntegral().intValue() > 0) {
//                    cartInfoGainIntegral = NumberUtil.mul(cart.getCartNum(), cart.
//                            getProductInfo().getGiveIntegral());
//                }
//                gainIntegral = NumberUtil.add(gainIntegral, cartInfoGainIntegral).intValue();
//            }
//
//        }


        //门店

        if (OrderInfoEnum.SHIPPIING_TYPE_1.getValue().equals(param.getShippingType())) {
            payPrice = NumberUtil.add(payPrice, payPostage);
        } else {
            payPostage = 0d;
        }

        if (payPrice <= 0) payPrice = 0d;

        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        //组合数据
        YxCouponOrder couponOrder = new YxCouponOrder();
        couponOrder.setUid(uid);
        couponOrder.setOrderId(orderSn);
        couponOrder.setRealName(userAddress.getRealName());
        couponOrder.setUserPhone(userAddress.getPhone());
//        couponOrder.setUserAddress(userAddress.getProvince() + " " + userAddress.getCity() +
//                " " + userAddress.getDistrict() + " " + userAddress.getDetail());
//        couponOrder.setCartId(StrUtil.join(",", cartIds));
        couponOrder.setTotalNum(totalNum);
        couponOrder.setTotalPrice(BigDecimal.valueOf(totalPrice));
//        couponOrder.setTotalPostage(BigDecimal.valueOf(payPostage));
//        couponOrder.setCouponId(couponId);
//        couponOrder.setCouponPrice(BigDecimal.valueOf(couponPrice));
        couponOrder.setTotalPrice(BigDecimal.valueOf(payPrice));
//        couponOrder.setPayPostage(BigDecimal.valueOf(payPostage));
//        couponOrder.setDeductionPrice(BigDecimal.valueOf(deductionPrice));
        couponOrder.setPayStaus(OrderInfoEnum.PAY_STATUS_0.getValue());

        couponOrder.setPayType(param.getPayType());
//        couponOrder.setUseIntegral(BigDecimal.valueOf(usedIntegral));
//        couponOrder.setGainIntegral(BigDecimal.valueOf(gainIntegral));
        couponOrder.setMark(param.getMark());
//        couponOrder.setCombinationId(combinationId);
//        couponOrder.setPinkId(param.getPinkId());
//        couponOrder.setSeckillId(seckillId);
//        couponOrder.setBargainId(bargainId);
//        couponOrder.setCost(BigDecimal.valueOf(cacheDTO.getPriceGroup().getCostPrice()));

        if (AppFromEnum.ROUNTINE.getValue().equals(param.getFrom())) {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_1.getValue());
        } else {
            couponOrder.setIsChannel(OrderInfoEnum.PAY_CHANNEL_0.getValue());
        }
        couponOrder.setCreateTime(DateTime.now());
        couponOrder.setUnique(key);
//        couponOrder.setShippingType(param.getShippingType());
        //处理门店
        if (OrderInfoEnum.SHIPPIING_TYPE_2.getValue().equals(param.getShippingType())) {
            YxSystemStoreQueryVo systemStoreQueryVo = systemStoreService.getYxSystemStoreById(param.getStoreId());
            if (systemStoreQueryVo == null) throw new ErrorRequestException("暂无门店无法选择门店自提");
            couponOrder.setVerifyCode(StrUtil.sub(orderSn, orderSn.length(), -12));
//            couponOrder.setStoreId(systemStoreQueryVo.getId());
        }

        boolean res = save(couponOrder);
        if (!res) throw new ErrorRequestException("订单生成失败");

        //减库存加销量

//        for (YxStoreCartQueryVo cart : cartInfo) {
//            if (combinationId > 0) {
//                combinationService.decStockIncSales(cart.getCartNum(), combinationId);
//            } else if (seckillId > 0) {
//                storeSeckillService.decStockIncSales(cart.getCartNum(), seckillId);
//            } else if (bargainId > 0) {
//                storeBargainService.decStockIncSales(cart.getCartNum(), bargainId);
//            } else {
//                productService.decProductStock(cart.getCartNum(), cart.getProductId(),
//                        cart.getProductAttrUnique());
//            }
//
//        }

        //保存购物车商品信息
//        orderCartInfoService.saveCartInfo(storeOrder.getId(), cartInfo);

        //购物车状态修改
//        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
//        wrapper.in("id", cartIds);
//        YxStoreCart cartObj = new YxStoreCart();
//        cartObj.setIsPay(1);
//        storeCartMapper.update(cartObj, wrapper);

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
        YxCouponOrderQueryVo orderInfo = getOrderInfo(orderId, uid);
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
    public YxCouponOrderQueryVo getOrderInfo(String unique, int uid) {
        QueryWrapper<YxCouponOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0).and(
                i -> i.eq("order_id", unique).or().eq("`unique`", unique));
        if (uid > 0) wrapper.eq("uid", uid);

//        return orderMap.toDto(yxCouponOrderMapper.selectOne(wrapper));
        return new YxCouponOrderQueryVo();
    }

    /**
     * 支付成功后操作
     *
     * @param orderId 订单号
     * @param payType 支付方式
     */
    @Override
    public void paySuccess(String orderId, String payType) {
        YxCouponOrderQueryVo orderInfo = getOrderInfo(orderId, 0);

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
}
