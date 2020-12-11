package co.yixiang.modules.test;

import co.yixiang.util.DateUtils;
import co.yixiang.util.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    /**
     * 加签
     */
    public void getSign(){
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("version","V1.0");
        requestMap.put("timestamp", DateUtils.localDateTime2Second(LocalDateTime.now()));
        String signature = SignatureUtil.getSignature(requestMap,"appKey");
    }
}
