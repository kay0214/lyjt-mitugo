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
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PlatformServiceImpl  implements PlatformService {

    @Autowired
    private YxUserBillService userBillService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxStoreProductService productService;


    /**
     * 查询今天的数据
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
     * @param storeId
     * @return
     */
    @Override
    public TodayDataDto getWorkDataBySid(Integer storeId) {

        TodayDataDto result = new TodayDataDto();
        // 本地生活相关-----------------------
        Map<String,Integer> localProductCount = productService.getLocalProductCount(storeId);
        // 卡券数量
        result.setLocalProduct(localProductCount.get("localProduct"));
        // 待上架卡券数量
        result.setLocalProductUnder(localProductCount.get("localProductUnder"));
        // 卡券订单相关
        Map<String,Integer> localProductOrderCount = productService.getLocalProductOrderCount(storeId);

        // 今日订单数
        result.setLocalOrderCount(localProductOrderCount.get("localOrderCount"));
        // 未核销订单
        result.setLocalOrderWait(localProductOrderCount.get("localOrderWait"));
        // 待处理退款
        result.setLocalOrderRefund(localProductOrderCount.get("localOrderRefund"));

        BigDecimal localSumPrice = productService.getLocalSumPrice(storeId);
        // 今日营业额
        result.setLocalSumPrice(localSumPrice);
        // 本地生活相关----------------------- end

        // 商城相关===========================
        Map<String,Integer> shopProductCount = productService.getShopProductCount(storeId);
        // 商品数量
        result.setShopProduct(shopProductCount.get("shopProduct"));
        // 待上架商品
        result.setShopProductUnder(shopProductCount.get("shopProductUnder"));

        // 商城订单数量相关
        Map<String,Integer> shopOrders = productService.getShopOrderCount(storeId);
        // 今日订单数
        result.setShopOrderCount(shopOrders.get("hopOrderCount"));
        // 待发货订单
        result.setShopOrderSend(shopOrders.get("shopOrderSend"));
        // 待处理退款
        result.setShopOrderRefund(shopOrders.get("shopOrderRefund"));

        // 今日营业额
        BigDecimal shopSumPrice = productService.getLocalSumPrice(storeId);
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
}
