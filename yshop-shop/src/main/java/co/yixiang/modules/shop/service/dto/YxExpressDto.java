/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author hupeng
* @date 2020-05-12
*/
@Data
public class YxExpressDto implements Serializable {

    /** 快递公司id */
    private Integer id;

    /** 快递公司简称 */
    private String code;

    /** 快递公司全称 */
    private String name;

    /** 排序 */
    private Integer sort;

    /** 是否显示 */
    private Integer isShow;
}
