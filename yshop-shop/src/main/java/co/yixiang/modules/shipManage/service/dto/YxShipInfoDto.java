/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxShipInfoDto implements Serializable {

    /** 船只id */

    private Integer id;


    /** 船只名称 */

    private String shipName;


    /** 船只系列id */

    private Integer seriesId;


    /** 商户id */

    private Integer merId;


    /** 所属商铺 */

    private Integer storeId;


    /** 帆船所属商户名 */

    private String merName;


    /** 帆船负责人 */

    private String managerName;


    /** 负责人电话 */

    private String managerPhone;


    /** 船只状态：0：启用，1：禁用 */

    private Integer shipStatus;


    /** 船只当前状态：0：在港，1：离港。2：维修中 */

    private Integer currentStatus;


    /** 最近一次出港时间 */

    private Integer lastLeaveTime;


    /** 最近一次返港时间 */

    private Integer lastReturnTime;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;

    private String imageUrl;

}
