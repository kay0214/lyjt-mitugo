/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

/**
 * @author liusy
 * @date 2020-08-19
 */
@Data
public class YxMerchantsDetailQueryCriteria extends BaseCriteria {

    // 登陆用户名
    private String username;

    // 商户名称
    private String merchantsName;

    // 联系人电话
    private String contactMobile;

    // 状态
    private Integer status;

    // 审核状态
    private Integer examineStatus;
}
