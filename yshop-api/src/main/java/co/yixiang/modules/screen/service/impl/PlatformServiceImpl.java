package co.yixiang.modules.screen.service.impl;

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
}
