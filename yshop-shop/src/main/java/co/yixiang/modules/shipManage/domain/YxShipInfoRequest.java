/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxShipInfoRequest implements Serializable {


    private Integer id;
    /** 船只名称 */
    @NotBlank
    private String shipName;


    /** 船只系列id */
    private Integer seriesId;


    /** 商户id */
    private Integer merId;


    /** 所属商铺 */
    private Integer storeId;


    /** 帆船所属商户名 */
    @NotBlank
    private String merName;


    /** 帆船负责人 */
    @NotBlank
    private String managerName;


    /** 负责人电话 */
    @NotBlank
    private String managerPhone;


    /** 船只状态：0：启用，1：禁用 */
    private Integer shipStatus;


    /** 船只当前状态：0：在港，1：离港。2：维修中 */
    private Integer currentStatus;


    /** 最近一次出港时间 */
    private Integer lastLeaveTime;


    /** 最近一次返港时间 */
    private Integer lastReturnTime;


    public void copy(YxShipInfoRequest source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
