package co.yixiang.listener;

import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.modules.shop.entity.YxSystemConfig;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * api服务启动初始化reids
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisKeyInitialization {


    private final YxSystemConfigService systemConfigService;

    private final RedisTemplate<Object, Object> redisTemplate;

    @PostConstruct
    public void redisKeyInitialization(){
        try {
            List<RedisKeyEnum> redisKeyEnums =  Stream.of(RedisKeyEnum.values()).collect(Collectors.toList());
            List<YxSystemConfig> systemConfigs = systemConfigService.list();
            for (RedisKeyEnum redisKeyEnum : redisKeyEnums) {
                Object redisKey  = redisTemplate.opsForValue().get(redisKeyEnum.getValue());
                if(redisKey == null){
                    String dbKey = "";
                    for (YxSystemConfig systemConfig : systemConfigs) {
                        if(systemConfig.getMenuName().equals(redisKeyEnum.getValue())){
                            dbKey = systemConfig.getValue();
                        }
                    }
                    redisTemplate.opsForValue().set(redisKeyEnum.getValue(),dbKey);
                }
            }
            log.info("---------------redisKey初始化成功---------------");
        }catch (Exception e){
            log.error("redisKey初始化失败: {}",e);
        }

    }
}
