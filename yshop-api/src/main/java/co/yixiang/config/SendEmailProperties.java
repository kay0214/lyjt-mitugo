/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "yshop.mailsconfig")
@Data
@Component
public class SendEmailProperties {

    private Integer port;
    private String comHost;
    private String sendUser;
    private String sendFrom;
    private String sendPassword;
    private boolean sslEnable;
}
