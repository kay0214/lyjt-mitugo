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
public class YxShipPassengerDto implements Serializable {

    /** id */

    private Integer id;


    /** 卡券订单id */

    private Integer couponOrderId;


    /** 船只出港批次号 */

    private String batchNo;


    /** 船只id */

    private Integer shipId;


    /** 乘客姓名 */

    private String passengerName;


    /** 乘客身份证 */

    private String idCard;


    /** 乘客电话 */

    private String phone;


    /** 0:未成年 1:成年人 2：老年人 */

    private Integer isAdult;

    /** 是否合同签订人 0:否 1:是 */
    private Integer signStatus;

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

}
