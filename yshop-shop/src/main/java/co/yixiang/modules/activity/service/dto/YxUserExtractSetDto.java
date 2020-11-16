/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lsy
 * @date 2020-11-16
 */
@Data
public class YxUserExtractSetDto implements Serializable {
    /* 商户提现费率 */
    private String storeExtractRate;
    /* 商户最低提现金额 */
    private String storeExtractMinPrice;
    /* 用户提现费率 */
    private String userExtractRate;
    /* 用户最低提现金额 */
    private String userExtractMinPrice;
}
