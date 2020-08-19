
package co.yixiang.test;

import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    @Autowired
    YxStoreInfoMapper yxStoreInfoMapper;


    @Test
    public void select() {
        YxStoreInfoQueryVo a = yxStoreInfoMapper.getYxStoreInfoByPoint(new BigDecimal("108.9498710632"), new BigDecimal("34.2588125935"));
    }
}
