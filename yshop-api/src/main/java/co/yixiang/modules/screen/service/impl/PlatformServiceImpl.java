package co.yixiang.modules.screen.service.impl;

import co.yixiang.modules.couponUse.dto.ShipUserLeaveVO;
import co.yixiang.modules.couponUse.dto.TodayDataDto;
import co.yixiang.modules.screen.dto.PlatformDto;
import co.yixiang.modules.screen.service.PlatformService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private YxUserBillService userBillService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxStoreProductService productService;


    /**
     * 查询今天的数据
     *
     * @return
     */
    @Override
    public PlatformDto getTodayData() {
        PlatformDto result = new PlatformDto();
        // 查询线下支付的信息
        Map<String, Object> offPay = userBillService.getTodayOffPayData();
        // 查询线上支付的信息
        Map<String, Object> onlinePay = userBillService.getOnlinePayData();
        // 所有商户数量
        Integer allMer = userService.getAllMer();
        // 认证通过商户数量
        Integer okMer = userService.getAllOkMer();
        // 用户数量
        Integer allUser = userService.getAllUser();
        // 分销客数量
        Integer fxUser = userService.getAllFxUser();

        // 本地生活商品数量
        Integer localProduct = productService.getLocalProduct();

        // 所有商品数量
        Integer product = productService.getAllProduct();


        result.setTodayOffPay((BigDecimal) offPay.get("todayOffPay"));

        result.setTodayOnlinePay((BigDecimal) onlinePay.get("todayOnlinePay"));

        result.setTodayOffPayCount((Long) offPay.get("todayOffPayCount"));

        result.setTodayOnlinePayCount((Long) onlinePay.get("todayOnlinePayCount"));

        result.setAllMer(allMer);

        result.setOkMer(okMer);

        result.setAllUser(allUser);

        result.setFxUser(fxUser);

        result.setLocalProduct(localProduct);

        result.setProduct(product);

        return result;
    }

    /**
     * 核销端今日数据统计
     *
     * @param storeId
     * @return
     */
    @Override
    public TodayDataDto getWorkDataBySid(Integer storeId) {

        TodayDataDto result = new TodayDataDto();
        // 本地生活相关-----------------------
        Map<String, Long> localProductCount = productService.getLocalProductCount(storeId);
        // 卡券数量
        result.setLocalProduct(localProductCount.get("localProduct").intValue());
        // 待上架卡券数量
        result.setLocalProductUnder(localProductCount.get("localProductUnder").intValue());
        // 卡券订单相关（当天数据、待退款的有点问题）
        Map<String, Long> localProductOrderCount = productService.getLocalProductOrderCount(storeId);
        // 卡券订单相关
        Map<String, Long> localProductOrderCountAll = productService.getLocalProductOrderCountAll(storeId);

        // 今日订单数
        result.setLocalOrderCount(localProductOrderCount.get("localOrderCount").intValue());
        // 未核销订单（取总值）
        result.setLocalOrderWait(localProductOrderCountAll.get("localOrderWait").intValue());
        // 待处理退款（取总值）
        result.setLocalOrderRefund(localProductOrderCountAll.get("localOrderRefund").intValue());

        BigDecimal localSumPrice = productService.getLocalSumPrice(storeId);
        // 今日营业额
        result.setLocalSumPrice(localSumPrice);
        // 本地生活相关----------------------- end

        // 商城相关===========================
        Map<String, Long> shopProductCount = productService.getShopProductCount(storeId);
        // 商品数量
        result.setShopProduct(shopProductCount.get("shopProduct").intValue());
        // 待上架商品
        result.setShopProductUnder(shopProductCount.get("shopProductUnder").intValue());

        // 商城订单数量相关
        Map<String, Long> shopOrders = productService.getShopOrderCount(storeId);
        // 商城订单数量相关(全部)
        Map<String, Long> shopOrdersAll = productService.getShopOrderCountAll(storeId);
        // 今日订单数
        result.setShopOrderCount(shopOrders.get("shopOrderCount").intValue());
        // 待发货订单(全部)
        result.setShopOrderSend(shopOrdersAll.get("shopOrderSend").intValue());
        // 待处理退款
        result.setShopOrderRefund(shopOrdersAll.get("shopOrderRefund").intValue());

        // 今日营业额
        BigDecimal shopSumPrice = productService.getShopSumPrice(storeId);
        result.setShopSumPrice(shopSumPrice);

        // 船只相关
        // 船只出港次数最多的船只
        List<ShipUserLeaveVO> shipLeaves = productService.getTopShipLeaves(storeId);

        // 出港最多的船长
        List<ShipUserLeaveVO> shipUserLeaveS = productService.getShipUserLeaveS(storeId);
        result.setShipLeaves(shipLeaves);
        result.setShipUserLeaveS(shipUserLeaveS);
        // 今日出港次数
        Integer leaveCount = productService.getShipLeaveCount(storeId);

        // 今日运营船只
        Integer shipCount = productService.getShipCount(storeId);
        result.setLeaveCount(leaveCount);
        result.setShipCount(shipCount);

        return result;
    }

    private TodayDataDto getFalseData() {
        TodayDataDto result = new TodayDataDto();

        // 本地生活相关-----------------------
        result.setLocalProduct(999);

        result.setLocalProductUnder(975);

        result.setLocalSumPrice(new BigDecimal("12210.21"));

        result.setLocalOrderCount(1234);

        result.setLocalOrderWait(2);

        result.setLocalOrderRefund(222);


        // 商城相关-----------------------
        result.setShopProduct(222);

        result.setShopProductUnder(111);

        result.setShopSumPrice(new BigDecimal("23345.99"));

        result.setShopOrderCount(6666);

        result.setShopOrderSend(6678);

        result.setShopOrderRefund(12345);


        // 船只信息---------------
        result.setLeaveCount(222221);

        result.setShipCount(11234);

        List<ShipUserLeaveVO> shipUserLeaveS = new ArrayList<>();
        ShipUserLeaveVO s1 = new ShipUserLeaveVO();
        s1.setCounts(222);
        s1.setName("战士王1");
        ShipUserLeaveVO s2 = new ShipUserLeaveVO();
        s2.setCounts(1);
        s2.setName("战士王2");
        ShipUserLeaveVO s3 = new ShipUserLeaveVO();
        s3.setCounts(44);
        s3.setName("战士王3");
        ShipUserLeaveVO s4 = new ShipUserLeaveVO();
        s4.setCounts(321);
        s4.setName("战士王4");
        ShipUserLeaveVO s5 = new ShipUserLeaveVO();
        s5.setCounts(211);
        s5.setName("战士王5");
        shipUserLeaveS.add(s1);
        shipUserLeaveS.add(s2);
        shipUserLeaveS.add(s3);
        shipUserLeaveS.add(s4);
        shipUserLeaveS.add(s5);


        result.setShipUserLeaveS(shipUserLeaveS);
        List<ShipUserLeaveVO> shipLeaves = new ArrayList<>();

        ShipUserLeaveVO sa1 = new ShipUserLeaveVO();
        sa1.setCounts(222);
        sa1.setName("爱仕达25");
        ShipUserLeaveVO sa2 = new ShipUserLeaveVO();
        sa2.setCounts(1);
        sa2.setName("爱仕达35");
        ShipUserLeaveVO sa3 = new ShipUserLeaveVO();
        sa3.setCounts(44);
        sa3.setName("爱仕达王1");
        ShipUserLeaveVO sa4 = new ShipUserLeaveVO();
        sa4.setCounts(3213);
        sa4.setName("ss王4");
        ShipUserLeaveVO sa5 = new ShipUserLeaveVO();
        sa5.setCounts(2111);
        sa5.setName("爱仕达王5");
        shipLeaves.add(sa1);
        shipLeaves.add(sa2);
        shipLeaves.add(sa3);
        shipLeaves.add(sa4);
        shipLeaves.add(sa5);
        result.setShipLeaves(shipLeaves);
        return result;
    }
}
