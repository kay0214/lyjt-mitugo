/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang;

import co.yixiang.modules.mybatis.GeoPoint;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.mapper.YxStoreInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author zhangqingqing
 * @version YxStoreInfoTest, v0.1 2020/8/19 17:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class YxStoreInfoTest {
    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Test
    public void insert() {
      YxStoreInfo yxStoreInfo = new YxStoreInfo();
        yxStoreInfo.setStoreNid("123");
        GeoPoint geoPoint = new GeoPoint(new BigDecimal("120.374461"),new BigDecimal("36.062144"));
        yxStoreInfo.setCoordinate(geoPoint);
        yxStoreInfoMapper.insertSelective(yxStoreInfo);
    }

    @Test
    public void update() {
        YxStoreInfo yxStoreInfo = yxStoreInfoMapper.selectById(7);
        GeoPoint geoPoint = new GeoPoint(new BigDecimal("120.374462"),new BigDecimal("36.062122"));
        yxStoreInfo.setCoordinate(geoPoint);
        yxStoreInfoMapper.updateByPrimaryKey(yxStoreInfo);
    }

}
