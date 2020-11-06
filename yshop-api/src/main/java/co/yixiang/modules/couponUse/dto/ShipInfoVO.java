/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.couponUse.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有船只
 * @author sss
 */
@Data
public class ShipInfoVO implements Serializable {


    @ApiModelProperty(value = "船只id")
    private Integer id;

    @ApiModelProperty(value = "船只名称")
    private String shipName;

    @ApiModelProperty(value = "船只系列id")
    private Integer seriesId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "所属商铺")
    private Integer storeId;

    @ApiModelProperty(value = "帆船所属商户名")
    private String merName;

    @ApiModelProperty(value = "帆船负责人")
    private String managerName;

    @ApiModelProperty(value = "负责人电话")
    private String managerPhone;

    @ApiModelProperty(value = "船只当前状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;

    @ApiModelProperty(value = "最近一次出港时间")
    private Integer lastLeaveTime;

    @ApiModelProperty(value = "最近一次返港时间")
    private Integer lastReturnTime;

}
