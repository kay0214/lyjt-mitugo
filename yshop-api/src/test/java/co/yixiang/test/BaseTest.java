
package co.yixiang.test;

import co.yixiang.modules.couponUse.dto.YxCouponsDto;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.user.service.YxUserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BaseTest {

    @Autowired
    YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private YxCouponsService yxCouponsService;

    @Test
    public void select() {
        SystemUser aa = yxUserService.getSystemUserById(122);
        aa.setStoreId(62);
        YxCouponsDto zzz = this.yxCouponsService.getShipCouponInfo("1309409793014595584,9", aa);
        log.info(JSONObject.toJSONString(zzz));
    }
}
