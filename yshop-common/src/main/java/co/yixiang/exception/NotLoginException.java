/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 统一异常处理
 */
@Getter
public class NotLoginException extends RuntimeException{

    private Integer status = 999;

    public NotLoginException(String msg){
        super(msg);
    }

    public NotLoginException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
