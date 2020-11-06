/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.couponUse.dto;

import co.yixiang.modules.ship.entity.YxShipInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 所有船只
 * @author sss
 */
@Data
public class AllShipsVO implements Serializable {

    @ApiModelProperty(value = "系列id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "系列名称")
    private String seriesName;

    @ApiModelProperty(value = "所属商铺")
    private Integer storeId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "状态：0：启用，1：禁用")
    private Integer status;
    private List<YxShipInfo> shipInfos;
}
