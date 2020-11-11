/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.config;

import co.yixiang.modules.esign.core.ClientHelper;
import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author PC-LIUSHOUYI
 * @version WuKongConfig, v0.1 2019/12/24 15:43
 */
@Slf4j
@Data
@Configuration
public class WuKongConfig {

    @Value("${wukong.project.id}")
    private String projectId;
    @Value("${wukong.project.secret}")
    private String projectSecret;
    @Value("${wukong.url}")
    private String wukongUrl;
    @Value("${wukong.cunzheng.url}")
    private String wukongCunzhengUrl;

    @Bean
    public ServiceClient serviceClient() throws DefineException {
        log.info("【悟空接口调用】启动客户端连接url为：" + wukongUrl);
        //1、注册客户端，全局使用，只需注册一次
        ClientHelper.registClient(projectId, projectSecret, wukongUrl);
        //2、获取已初始化的客户端，以便后续正常调用SDK提供的各种服务，全局使用，只需获取一次
        ServiceClient serviceClient = ClientHelper.getServiceClient(projectId);
        return serviceClient;
    }


}
