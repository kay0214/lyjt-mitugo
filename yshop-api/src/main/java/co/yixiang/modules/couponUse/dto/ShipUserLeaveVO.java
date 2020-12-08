/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.couponUse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 船长出港次数
 * @author sss
 */
@Data
public class ShipUserLeaveVO implements Serializable {

    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "出港次数")
    private Integer counts;
}
