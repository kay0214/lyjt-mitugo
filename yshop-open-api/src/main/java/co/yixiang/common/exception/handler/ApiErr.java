/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.common.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hupeng
 * @since 2019-10-02
 */
@Data
public class ApiErr {

    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String msg;

    private ApiErr() {
        timestamp = LocalDateTime.now();
    }

    public ApiErr(Integer status, String message) {
        this();
        this.status = status;
        this.msg = message;
    }
}


