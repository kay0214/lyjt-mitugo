/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Data
public class YxLeaveMessageDto implements Serializable {

    /** id */

    private Integer id;


    /** 订单号 */

    private Integer linkId;


    /** 商户id */

    private Integer merId;


    /** 联系人 */

    private String userName;


    /** 电话 */

    private String userPhone;


    /** 留言信息 */

    private String message;


    /** 状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理 */

    private Integer status;

    /** 处理时间 */
    private Integer takeTime;

    /** 处理时间 */
    private String takeTimeStr;

    /** 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台 */

    private Integer messageType;


    /** 备注 */

    private String remark;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 修改人 */

    private String updateUsername;


    private String updateNickname;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;

    /** 商品名称 */
    private String goodsName;

    /** 订单号 */
    private String orderId;
}
