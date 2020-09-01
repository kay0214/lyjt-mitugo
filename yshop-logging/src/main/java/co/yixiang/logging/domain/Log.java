/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.logging.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author hupeng
 * @date 2018-11-24
 */
@Data
@TableName("log")
@NoArgsConstructor
public class Log  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 操作用户 */
    private String username;

    @TableField(exist = false)
    private String nickname;

    /** 描述 */
    private String description;

    /** 方法名 */
    private String method;

    private Long uid;

    private Integer type;

    /** 参数 */
    private String params;

    /** 日志类型 */
    private String logType;

    /** 请求ip */
    private String requestIp;

    /** 地址 */
    private String address;

    /** 浏览器  */
    private String browser;

    /** 请求耗时 */
    private Long time;

    /** 异常详细  */
    private byte[] exceptionDetail;

    /**
     * 商品类型 1:商品 2：卡券
     */
    private Integer productType;

    /**
     * 商品Id
     */
    private Integer productId;


    /** 创建日期 */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
