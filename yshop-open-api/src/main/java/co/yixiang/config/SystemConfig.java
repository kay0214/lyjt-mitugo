package co.yixiang.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SystemConfig {

    public static String appKey;

    @Value("${online.appKey}")
    public void setAppKey(String appKey) {
        SystemConfig.appKey = appKey;
    }


}