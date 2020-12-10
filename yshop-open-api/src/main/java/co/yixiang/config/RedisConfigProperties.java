/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * reids相关配置
 * @author hupeng
 * @since 2020-02-27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperties {

	private String host = "host";
	private String port = "port";
	private String password = "password";
	private String database = "database";

}
