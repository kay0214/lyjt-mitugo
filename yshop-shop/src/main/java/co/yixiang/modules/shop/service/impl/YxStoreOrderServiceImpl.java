/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.EntityExistException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.domain.YxStoreCouponUser;
import co.yixiang.modules.activity.domain.YxStorePink;
import co.yixiang.modules.activity.service.YxStorePinkService;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponUserMapper;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.service.dto.*;
import co.yixiang.modules.shop.service.mapper.*;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.mp.service.YxPayService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author hupeng
 * @date 2020-05-12
 */
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxStoreOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreOrderServiceImpl extends BaseServiceImpl<StoreOrderMapper, YxStoreOrder> implements YxStoreOrderService {

    private final IGenerator generator;
    private YxUserService userService;
    private UserMapper userMapper;
    private YxStorePinkService storePinkService;
    private YxStoreOrderCartInfoService storeOrderCartInfoService;
    private final YxUserBillService yxUserBillService;
    private final YxStoreOrderStatusService yxStoreOrderStatusService;
    private final YxPayService payService;
    private final YxMiniPayService miniPayService;
    private final YxSystemStoreService systemStoreService;
    private final YxStoreCartService storeCartService;
    private final StoreOrderMapper yxStoreOrderMapper;
    private final StoreProductMapper yxStoreProductMapper;
    private final YxStoreInfoMapper yxStoreInfoMapper;

    private YxStoreOrderCartInfoService orderCartInfoService;
    private YxStoreCouponUserMapper yxStoreCouponUserMapper;
    private StoreProductAttrValueMapper storeProductAttrValueMapper;

    @Override
    public OrderCountDto getOrderCount() {
        //获取所有订单转态为已支付的
        List<CountDto> nameList = storeCartService.findCateName();
        log.info("nameList:" + nameList);
        List<OrderCountDto.OrderCountData> list = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        nameList.forEach(data -> {
            OrderCountDto.OrderCountData orderCountData = new OrderCountDto.OrderCountData();
            orderCountData.setName(data.getCatename());
            orderCountData.setValue(data.getCount());
            columns.add(data.getCatename());
            list.add(orderCountData);
        });
        OrderCountDto orderCountDto = new OrderCountDto();
        orderCountDto.setColumn(columns);
        orderCountDto.setOrderCountDatas(list);
        return orderCountDto;
    }

    @Override
    public OrderTimeDataDto getOrderTimeData() {
        int today = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        int yesterday = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.
                yesterday()));
        int lastWeek = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.lastWeek()));
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));
        OrderTimeDataDto orderTimeDataDTO = new OrderTimeDataDto();

        orderTimeDataDTO.setTodayCount(yxStoreOrderMapper.countByPayTimeGreaterThanEqual(today));
        //orderTimeDataDTO.setTodayPrice(yxStoreOrderMapper.sumPrice(today));

        orderTimeDataDTO.setProCount(yxStoreOrderMapper
                .countByPayTimeLessThanAndPayTimeGreaterThanEqual(today, yesterday));
        //orderTimeDataDTO.setProPrice(yxStoreOrderMapper.sumTPrice(today,yesterday));

        orderTimeDataDTO.setLastWeekCount(yxStoreOrderMapper.countByPayTimeGreaterThanEqual(lastWeek));
        //orderTimeDataDTO.setLastWeekPrice(yxStoreOrderMapper.sumPrice(lastWeek));

        orderTimeDataDTO.setMonthCount(yxStoreOrderMapper.countByPayTimeGreaterThanEqual(nowMonth));
        //orderTimeDataDTO.setMonthPrice(yxStoreOrderMapper.sumPrice(nowMonth));

        orderTimeDataDTO.setUserCount(userMapper.selectCount(new QueryWrapper<YxUser>()));
        orderTimeDataDTO.setOrderCount(yxStoreOrderMapper.selectCount(new QueryWrapper<YxStoreOrder>()));
        orderTimeDataDTO.setPriceCount(yxStoreOrderMapper.sumTotalPrice());
        orderTimeDataDTO.setGoodsCount(yxStoreProductMapper.selectCount(new QueryWrapper<YxStoreProduct>()));

        return orderTimeDataDTO;
    }

    @Override
    public Map<String, Object> chartCount() {
        Map<String, Object> map = new LinkedHashMap<>();
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));

        map.put("chart", yxStoreOrderMapper.chartList(nowMonth));
        map.put("chartT", yxStoreOrderMapper.chartListT(nowMonth));

        return map;
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreOrderQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxStoreOrder> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxStoreOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxStoreOrder::getAddTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxStoreOrder::getMerId, criteria.getChildUser());
        }
        //

        if (null != criteria.getIsDel()) {
            queryWrapper.lambda().eq(YxStoreOrder::getIsDel, criteria.getIsDel());
        }
        if (null != criteria.getPaid()) {
            queryWrapper.lambda().eq(YxStoreOrder::getPaid, criteria.getPaid());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxStoreOrder::getStatus, criteria.getStatus());
        }
        if (null != criteria.getRefundStatus()) {
            queryWrapper.lambda().eq(YxStoreOrder::getRefundStatus, criteria.getRefundStatus());
        }
        if (null != criteria.getBargainId()) {
            queryWrapper.lambda().eq(YxStoreOrder::getBargainId, criteria.getBargainId());
        }
        if (null != criteria.getCombinationId()) {
            queryWrapper.lambda().eq(YxStoreOrder::getCombinationId, criteria.getCombinationId());
        }
        if (null != criteria.getSeckillId()) {
            queryWrapper.lambda().eq(YxStoreOrder::getSeckillId, criteria.getSeckillId());
        }
        if (null != criteria.getShippingType()) {
            queryWrapper.lambda().eq(YxStoreOrder::getShippingType, criteria.getShippingType());
        }
        // getNewCombinationId getNewSeckillId getNewBargainId  getShippingType
        if (null != criteria.getOrderId()) {
            queryWrapper.lambda().like(YxStoreOrder::getOrderId, criteria.getOrderId());
        }
        if (null != criteria.getUserPhone()) {
            queryWrapper.lambda().like(YxStoreOrder::getUserPhone, criteria.getUserPhone());
        }
        //
        if (null != criteria.getRealName()) {
            queryWrapper.lambda().like(YxStoreOrder::getRealName, criteria.getRealName());
        }
        if (!CollectionUtils.isEmpty(criteria.getAddTime())) {
            List<String> listAddTime = criteria.getAddTime();
            Integer addTimeStart = 0;
            Integer addTimeEnd = 0;
            try {
                Date date = new Date();
                Date dateEnd = new Date();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sf.parse(listAddTime.get(0));// 日期转换为时间戳
                dateEnd = sf.parse(listAddTime.get(1));// 日期转换为时间戳
                long longDate = date.getTime()/1000;
                long longDateEnd = dateEnd.getTime()/1000;
                addTimeStart =(int)longDate;
                addTimeEnd =(int)longDateEnd;
            } catch (ParseException e) {e.printStackTrace();}
            if(addTimeEnd!=0&&addTimeStart!=0){
                queryWrapper.lambda().ge(YxStoreOrder::getAddTime, addTimeStart).le(YxStoreOrder::getAddTime, addTimeEnd);
            }
        }


        IPage<YxStoreOrder> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            Map<String, Object> map = new LinkedHashMap<>(2);
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxStoreOrderDto> storeOrderDTOS = new ArrayList<>();
        for (YxStoreOrder yxStoreOrder : ipage.getRecords()) {
            orderList(storeOrderDTOS, yxStoreOrder);
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeOrderDTOS);
        map.put("totalElements", ipage.getTotal());
        return map;
    }

    /**
     * 代码提取
     *
     * @param storeOrderDTOS
     * @param yxStoreOrder
     */
    private void orderList(List<YxStoreOrderDto> storeOrderDTOS, YxStoreOrder yxStoreOrder) {
        YxStoreOrderDto yxStoreOrderDto = generator.convert(yxStoreOrder, YxStoreOrderDto.class);
        Integer _status = OrderUtil.orderStatus(yxStoreOrder.getPaid(), yxStoreOrder.getStatus(),
                yxStoreOrder.getRefundStatus());

        if (yxStoreOrder.getStoreId() > 0) {
            String storeName = yxStoreInfoMapper.selectById(yxStoreOrder.getStoreId()).getStoreName();
            yxStoreOrderDto.setStoreName(storeName);
        }

        //订单状态
        String orderStatusStr = OrderUtil.orderStatusStr(yxStoreOrder.getPaid()
                , yxStoreOrder.getStatus(), yxStoreOrder.getShippingType()
                , yxStoreOrder.getRefundStatus());

        if (_status == 3) {
            String refundTime = OrderUtil.stampToDate(String.valueOf(yxStoreOrder
                    .getRefundReasonTime()));
            String str = "<b style='color:#f124c7'>申请退款</b><br/>" +
                    "<span>退款原因：" + yxStoreOrder.getRefundReasonWap() + "</span><br/>" +
                    "<span>备注说明：" + yxStoreOrder.getRefundReasonWapExplain() + "</span><br/>" +
                    "<span>退款时间：" + refundTime + "</span><br/>";
            orderStatusStr = str;
        }
        yxStoreOrderDto.setStatusName(orderStatusStr);

        yxStoreOrderDto.set_status(_status);

        String payTypeName = OrderUtil.payTypeName(yxStoreOrder.getPayType()
                , yxStoreOrder.getPaid());
        yxStoreOrderDto.setPayTypeName(payTypeName);

        yxStoreOrderDto.setPinkName(orderType(yxStoreOrder.getId()
                , yxStoreOrder.getPinkId(), yxStoreOrder.getCombinationId()
                , yxStoreOrder.getSeckillId(), yxStoreOrder.getBargainId(),
                yxStoreOrder.getShippingType()));

        List<YxStoreOrderCartInfo> cartInfos = storeOrderCartInfoService.list(
                new QueryWrapper<YxStoreOrderCartInfo>().eq("oid", yxStoreOrder.getId()));
        List<StoreOrderCartInfoDto> cartInfoDTOS = new ArrayList<>();
        for (YxStoreOrderCartInfo cartInfo : cartInfos) {
            StoreOrderCartInfoDto cartInfoDTO = new StoreOrderCartInfoDto();
            cartInfoDTO.setCartInfoMap(JSON.parseObject(cartInfo.getCartInfo()));

            cartInfoDTOS.add(cartInfoDTO);
        }
        yxStoreOrderDto.setCartInfoList(cartInfoDTOS);
        yxStoreOrderDto.setUserDTO(generator.convert(userService.getById(yxStoreOrder.getUid()), YxUserDto.class));
        if (yxStoreOrderDto.getUserDTO() == null) {
            yxStoreOrderDto.setUserDTO(new YxUserDto());
        }
        storeOrderDTOS.add(yxStoreOrderDto);
    }


    @Override
    //@Cacheable
    public List<YxStoreOrder> queryAll(YxStoreOrderQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreOrder.class, criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxStoreOrderDto create(YxStoreOrder resources) {
        if (this.getOne(new QueryWrapper<YxStoreOrder>().eq("`unique`", resources.getUnique())) != null) {
            throw new EntityExistException(YxStoreOrder.class, "unique", resources.getUnique());
        }
        this.save(resources);
        return generator.convert(resources, YxStoreOrderDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxStoreOrder resources) {
        YxStoreOrder yxStoreOrder = this.getById(resources.getId());
        YxStoreOrder yxStoreOrder1 = this.getOne(new QueryWrapper<YxStoreOrder>().eq("`unique`", resources.getUnique()));
        if (yxStoreOrder1 != null && !yxStoreOrder1.getId().equals(yxStoreOrder.getId())) {
            throw new EntityExistException(YxStoreOrder.class, "unique", resources.getUnique());
        }
        yxStoreOrder.copy(resources);
        this.saveOrUpdate(yxStoreOrder);
    }


    @Override
    public void download(List<YxStoreOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreOrderDto yxStoreOrder : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单号", yxStoreOrder.getOrderId());
            map.put("用户id", yxStoreOrder.getUid());
            map.put("用户姓名", yxStoreOrder.getRealName());
            map.put("用户电话", yxStoreOrder.getUserPhone());
            map.put("详细地址", yxStoreOrder.getUserAddress());
            map.put("购物车id", yxStoreOrder.getCartId());
//            map.put("运费金额", yxStoreOrder.getFreightPrice());
            map.put("订单商品总数", yxStoreOrder.getTotalNum());
            map.put("订单总价", yxStoreOrder.getTotalPrice());
            map.put("邮费", yxStoreOrder.getTotalPostage());
            map.put("实际支付金额", yxStoreOrder.getPayPrice());
            map.put("支付邮费", yxStoreOrder.getPayPostage());
            map.put("抵扣金额", yxStoreOrder.getDeductionPrice());
            map.put("优惠券id", yxStoreOrder.getCouponId());
            map.put("优惠券金额", yxStoreOrder.getCouponPrice());
            map.put("支付状态", yxStoreOrder.getPaid());
//            map.put("支付时间", yxStoreOrder.getPayTime());
            map.put("支付时间", DateUtils.timestampToStr10(yxStoreOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));

            map.put("支付方式", yxStoreOrder.getPayType());
//            map.put("创建时间", yxStoreOrder.getAddTime());
            map.put("创建时间", DateUtils.timestampToStr10(yxStoreOrder.getAddTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
            String strStatus ="";
            switch (yxStoreOrder.getStatus()){
                /*case 0:strStatus="待发货";break;
                case 1:strStatus="待收货";break;
                case 2:strStatus="已收";break;
                case 3:strStatus="待评价";break;*/

                case 0:strStatus = yxStoreOrder.getPaid().equals(0)?"未支付":"未发货";break;
                case 1:strStatus="待收货";break;
                case 2:strStatus="待评价";break;
                case 3:strStatus="交易完成";break;

            }
            map.put("订单状态",strStatus);
            String strFund="";
            switch (yxStoreOrder.getRefundStatus()){
                case 0:strFund="未退款";break;
                case 1:strFund="申请中";break;
                case 2:strFund="已退款";break;

            }//0 未退款 1 申请中 2 已退款)
            map.put("退款状态", strFund);
            map.put("退款图片", yxStoreOrder.getRefundReasonWapImg());
            map.put("退款用户说明", yxStoreOrder.getRefundReasonWapExplain());
//            map.put("退款时间", yxStoreOrder.getRefundReasonTime());
            map.put("退款时间", DateUtils.timestampToStr10(yxStoreOrder.getRefundReasonTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.put("前台退款原因", yxStoreOrder.getRefundReasonWap());
            map.put("不退款的理由", yxStoreOrder.getRefundReason());
            map.put("退款金额", yxStoreOrder.getRefundPrice());
            map.put("快递公司编号", yxStoreOrder.getDeliverySn());
            map.put("快递名称/送货人姓名", yxStoreOrder.getDeliveryName());
            map.put("发货类型", yxStoreOrder.getDeliveryType());
            map.put("快递单号/手机号", yxStoreOrder.getDeliveryId());
            map.put("消费赚取积分", yxStoreOrder.getGainIntegral());
            map.put("使用积分", yxStoreOrder.getUseIntegral());
            map.put("给用户退了多少积分", yxStoreOrder.getBackIntegral());
            map.put("备注", yxStoreOrder.getMark());
            map.put("是否删除", yxStoreOrder.getIsDel()==1?"已删除":"未删除");
//            map.put("唯一id(md5加密)类似id", yxStoreOrder.getUnique());
            map.put("管理员备注", yxStoreOrder.getRemark());
            map.put("商户ID", yxStoreOrder.getMerId());
//            map.put(" isMerCheck", yxStoreOrder.getIsMerCheck());
//            map.put("拼团产品id0一般产品", yxStoreOrder.getCombinationId());
//            map.put("拼团id 0没有拼团", yxStoreOrder.getPinkId());
//            map.put("成本价", yxStoreOrder.getCost());
//            map.put("秒杀产品ID", yxStoreOrder.getSeckillId());
//            map.put("砍价id", yxStoreOrder.getBargainId());
            map.put("核销码", yxStoreOrder.getVerifyCode());
            map.put("店铺id", yxStoreOrder.getStoreId());
            map.put("配送方式", yxStoreOrder.getShippingType().equals(1)?"快递":"门店自提");
            map.put("支付渠道", yxStoreOrder.getIsChannel().equals(1)?"小程序":"公众号");
            /*map.put(" isRemind", yxStoreOrder.getIsRemind());
            map.put(" isSystemDel", yxStoreOrder.getIsSystemDel());*/
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> queryAll(List<String> ids) {
        List<YxStoreOrder> yxStoreOrders = this.list(new QueryWrapper<YxStoreOrder>().in("order_id", ids));
        List<YxStoreOrderDto> storeOrderDTOS = new ArrayList<>();
        for (YxStoreOrder yxStoreOrder : yxStoreOrders) {
            YxStoreOrderDto yxStoreOrderDto = generator.convert(yxStoreOrder, YxStoreOrderDto.class);

            Integer _status = OrderUtil.orderStatus(yxStoreOrder.getPaid(), yxStoreOrder.getStatus(),
                    yxStoreOrder.getRefundStatus());

            /*if (yxStoreOrder.getStoreId() > 0) {
                String storeName = systemStoreService.getById(yxStoreOrder.getStoreId()).getName();
                yxStoreOrderDto.setStoreName(storeName);
            }*/

            //订单状态
            String orderStatusStr = OrderUtil.orderStatusStr(yxStoreOrder.getPaid()
                    , yxStoreOrder.getStatus(), yxStoreOrder.getShippingType()
                    , yxStoreOrder.getRefundStatus());

            if (_status == 3) {
                String refundTime = OrderUtil.stampToDate(String.valueOf(yxStoreOrder
                        .getRefundReasonTime()));
                String str = "<b style='color:#f124c7'>申请退款</b><br/>" +
                        "<span>退款原因：" + yxStoreOrder.getRefundReasonWap() + "</span><br/>" +
                        "<span>备注说明：" + yxStoreOrder.getRefundReasonWapExplain() + "</span><br/>" +
                        "<span>退款时间：" + refundTime + "</span><br/>";
                orderStatusStr = str;
            }
            yxStoreOrderDto.setStatusName(orderStatusStr);

            yxStoreOrderDto.set_status(_status);

            String payTypeName = OrderUtil.payTypeName(yxStoreOrder.getPayType()
                    , yxStoreOrder.getPaid());
            yxStoreOrderDto.setPayTypeName(payTypeName);

            yxStoreOrderDto.setPinkName(orderType(yxStoreOrder.getId()
                    , yxStoreOrder.getPinkId(), yxStoreOrder.getCombinationId()
                    , yxStoreOrder.getSeckillId(), yxStoreOrder.getBargainId(),
                    yxStoreOrder.getShippingType()));

            List<YxStoreOrderCartInfo> cartInfos = storeOrderCartInfoService.list(new QueryWrapper<YxStoreOrderCartInfo>().eq("oid", yxStoreOrder.getId()));
            List<StoreOrderCartInfoDto> cartInfoDTOS = new ArrayList<>();
            for (YxStoreOrderCartInfo cartInfo : cartInfos) {
                StoreOrderCartInfoDto cartInfoDTO = new StoreOrderCartInfoDto();
                cartInfoDTO.setCartInfoMap(JSON.parseObject(cartInfo.getCartInfo()));

                cartInfoDTOS.add(cartInfoDTO);
            }
            yxStoreOrderDto.setCartInfoList(cartInfoDTOS);
            yxStoreOrderDto.setUserDTO(generator.convert(userService.getOne(new QueryWrapper<YxUser>().eq("uid", yxStoreOrder.getUid())), YxUserDto.class));

            storeOrderDTOS.add(yxStoreOrderDto);

        }

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeOrderDTOS);

        return map;
    }


    @Override
    public String orderType(int id, int pinkId, int combinationId, int seckillId,
                            int bargainId, int shippingType) {
        String str = "[普通订单]";
        if (pinkId > 0 || combinationId > 0) {
            YxStorePink storePink = storePinkService.getOne(new QueryWrapper<YxStorePink>().
                    eq("order_id_key", id));
            if (ObjectUtil.isNull(storePink)) {
                str = "[拼团订单]";
            } else {
                switch (storePink.getStatus()) {
                    case 1:
                        str = "[拼团订单]正在进行中";
                        break;
                    case 2:
                        str = "[拼团订单]已完成";
                        break;
                    case 3:
                        str = "[拼团订单]未完成";
                        break;
                    default:
                        str = "[拼团订单]历史订单";
                        break;
                }
            }

        } else if (seckillId > 0) {
            str = "[秒杀订单]";
        } else if (bargainId > 0) {
            str = "[砍价订单]";
        }
        if (shippingType == 2) str = "[核销订单]";
        return str;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(YxStoreOrder resources) {
        if (resources.getPayPrice().doubleValue() <= 0) {
            throw new BadRequestException("请输入退款金额");
        }

        YxStoreOrder order = yxStoreOrderMapper.selectById(resources.getId());
        if(ObjectUtil.isNull(order)){
            return;
        }

        if (resources.getPayPrice().doubleValue() > order.getPayPrice().doubleValue()) {
            throw new BadRequestException("退款金额不能超过支付金额！");
        }
        if (resources.getPayType().equals("yue")) {
            //修改状态
            resources.setRefundStatus(2);
            resources.setRefundPrice(resources.getPayPrice());
            this.updateById(resources);

            //退款到余额
            YxUserDto userDTO = generator.convert(userService.getOne(new QueryWrapper<YxUser>().eq("uid", resources.getUid())), YxUserDto.class);
            userMapper.updateMoney(resources.getPayPrice().doubleValue(),
                    resources.getUid());

            YxUserBill userBill = new YxUserBill();
            userBill.setUid(resources.getUid());

            userBill.setLinkId(resources.getId().toString());
            userBill.setPm(1);
            userBill.setTitle("商品退款");
            userBill.setCategory("now_money");
            userBill.setType("pay_product_refund");
            userBill.setNumber(resources.getPayPrice());
            userBill.setBalance(NumberUtil.add(resources.getPayPrice(), userDTO.getNowMoney()));
            userBill.setMark("订单退款到余额");
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setStatus(1);
            yxUserBillService.save(userBill);


            YxStoreOrderStatus storeOrderStatus = new YxStoreOrderStatus();
            storeOrderStatus.setOid(resources.getId());
            storeOrderStatus.setChangeType("refund_price");
            storeOrderStatus.setChangeMessage("退款给用户：" + resources.getPayPrice() + "元");
            storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

            yxStoreOrderStatusService.save(storeOrderStatus);
        } else {
            BigDecimal bigDecimal = new BigDecimal("100");
            BigDecimal bigSumPrice = new BigDecimal(Double.toString(yxStoreOrderMapper.sumPayPrice(order.getPaymentNo())));
            try {
                if (OrderInfoEnum.PAY_CHANNEL_1.getValue().equals(resources.getIsChannel())) {
                    //修改->多个订单，同一个付款单号，orderId为退款单号
                   /* miniPayService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());*/
                    miniPayService.refundOrderNew(resources.getOrderId(), bigDecimal.multiply(resources.getPayPrice()).intValue(), order.getPaymentNo(), bigDecimal.multiply(bigSumPrice).intValue());
                } else {
                    /*payService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());*/
                    payService.refundOrderNew(resources.getOrderId(), bigDecimal.multiply(resources.getPayPrice()).intValue(), resources.getPaymentNo(), bigDecimal.multiply(bigSumPrice).intValue());
                }

            } catch (WxPayException e) {
                log.info("refund-error:{}", e.getMessage());
                throw new BadRequestException("退款失败:" + e.getMessage());
            }

        }
    }

    /**
     * 查找超时未付款的订单，将其状态设置为已取消
     */
    @Override
    public void cancelOrder() {
        Date now = new Date();
        long time = 30 * 60 * 1000;
        Date beforeDate = new Date(now.getTime() - time);//30分钟前的时间
        int intBeforeDate = OrderUtil.dateToTimestamp(beforeDate);
        QueryWrapper<YxStoreOrder> orderQueryWrapper = new QueryWrapper<YxStoreOrder>();
        orderQueryWrapper.eq("is_del", 0).eq("paid", 0).eq("status", 0).eq("refund_status", 0).le("add_time", intBeforeDate);
        List<YxStoreOrder> listOrder = yxStoreOrderMapper.selectList(orderQueryWrapper);
        if (CollectionUtils.isEmpty(listOrder)) {
            return;
        }
        for (YxStoreOrder order : listOrder) {
            cancelOrderByTask(order);
        }
    }

    /**
     * 系统自动主动取消未付款取消订单
     *
     * @param order
     */
    public void cancelOrderByTask(YxStoreOrder order) {
        try {
            if (ObjectUtil.isNull(order)) throw new ErrorRequestException("订单不存在");

            if (order.getIsDel() == OrderInfoEnum.CANCEL_STATUS_1.getValue()) throw new ErrorRequestException("订单已取消");

            //回退积分
            regressionIntegral(order);
            //退回库存
            regressionStock(order);
            //退回优惠券
            regressionCoupon(order);

            YxStoreOrder storeOrder = new YxStoreOrder();
            storeOrder.setIsDel(OrderInfoEnum.CANCEL_STATUS_1.getValue());
            storeOrder.setId(order.getId());
            yxStoreOrderMapper.updateById(storeOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void regressionIntegral(YxStoreOrder order) {
        if (order.getPaid() > 0 || order.getStatus() == -2 || order.getIsDel() == 1) {
            return;
        }
        if (order.getUseIntegral().doubleValue() <= 0) return;

        if (order.getStatus() != -2 && order.getRefundStatus() != 2
                && ObjectUtil.isNotNull(order.getBackIntegral())
                && order.getBackIntegral().doubleValue() >= order.getUseIntegral().doubleValue()) {
            return;
        }

        YxUser userQueryVo = userService.getById(order.getUid());

        //增加积分
        userService.incIntegral(order.getUid(), order.getUseIntegral().doubleValue());

        //增加流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(order.getUid());
        userBill.setTitle("积分回退");
        userBill.setLinkId(order.getId().toString());
        userBill.setCategory("integral");
        userBill.setType("deduction");
        userBill.setNumber(order.getUseIntegral());
        userBill.setBalance(userQueryVo.getIntegral());
        userBill.setMark("购买商品失败,回退积分");
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        userBill.setPm(BillEnum.PM_1.getValue());
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserBillService.save(userBill);

        //更新回退积分
        YxStoreOrder storeOrder = new YxStoreOrder();
        storeOrder.setBackIntegral(order.getUseIntegral());
        storeOrder.setId(order.getId());
        yxStoreOrderMapper.updateById(storeOrder);
    }

    /**
     * 退回库存
     *
     * @param order
     */
    public void regressionStock(YxStoreOrder order) {
        if (order.getPaid() > 0 || order.getStatus() == -2 || order.getIsDel() == 1) {
            return;
        }
        QueryWrapper<YxStoreOrderCartInfo> wrapper = new QueryWrapper<>();
        wrapper.in("cart_id", Arrays.asList(order.getCartId().split(",")));

        List<YxStoreOrderCartInfo> cartInfoList = orderCartInfoService.list(wrapper);
        for (YxStoreOrderCartInfo cartInfo : cartInfoList) {
            YxStoreCart cart = JSONObject.parseObject(cartInfo.getCartInfo()
                    , YxStoreCart.class);
            /*if (order.getCombinationId() > 0) {//拼团
                combinationService.incStockDecSales(cart.getCartNum(), order.getCombinationId());
            } else if (order.getSeckillId() > 0) {//秒杀
                storeSeckillService.incStockDecSales(cart.getCartNum(), order.getSeckillId());
            } else if (order.getBargainId() > 0) {//砍价
                storeBargainService.incStockDecSales(cart.getCartNum(), order.getBargainId());
            } else {
                productService.incProductStock(cart.getCartNum(), cart.getProductId()
                        , cart.getProductAttrUnique());
            }*/
            incProductStock(cart.getCartNum(), cart.getProductId()
                    , cart.getProductAttrUnique());
        }
    }

    /**
     * 退回优惠券
     *
     * @param order
     */
    public void regressionCoupon(YxStoreOrder order) {
        if (order.getPaid() > 0 || order.getStatus() == -2 || order.getIsDel() == 1) {
            return;
        }

        QueryWrapper<YxStoreCouponUser> wrapper = new QueryWrapper<>();
        if (order.getCouponId() > 0) {
            wrapper.eq("id", order.getCouponId()).eq("status", 1).eq("uid", order.getUid());
            YxStoreCouponUser couponUser = yxStoreCouponUserMapper.selectOne(wrapper);

            if (ObjectUtil.isNotNull(couponUser)) {
                YxStoreCouponUser storeCouponUser = new YxStoreCouponUser();
                QueryWrapper<YxStoreCouponUser> wrapperT = new QueryWrapper<>();
                wrapperT.eq("id", order.getCouponId()).eq("uid", order.getUid());
                storeCouponUser.setStatus(0);
                storeCouponUser.setUseTime(0);
                yxStoreCouponUserMapper.update(storeCouponUser, wrapperT);
            }
        }

    }

    public void incProductStock(int num, int productId, String unique) {
        if (StrUtil.isNotEmpty(unique)) {
            storeProductAttrValueMapper.incStockDecSales(num, productId, unique);
            yxStoreProductMapper.decSales(num, productId);
        } else {
            yxStoreProductMapper.incStockDecSales(num, productId);
        }
    }
}
