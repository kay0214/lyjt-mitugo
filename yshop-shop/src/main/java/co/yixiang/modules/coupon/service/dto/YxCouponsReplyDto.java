/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Data
public class YxCouponsReplyDto implements Serializable {

    /** 评论ID */

    private Integer id;


    /** 用户ID */

    private Integer uid;


    /** 订单ID */

    private Integer oid;


    /** 卡券id */

    private Integer couponId;


    /** 总体感觉 */

    private Integer generalScore;


    /** 评论内容 */

    private String comment;


    /** 评论时间 */

    private Integer addTime;


    /** 管理员回复时间 */

    private Integer merchantReplyTime;


    /** 0：未回复，1：已回复 */

    private Integer isReply;


    /** 商户id */

    private Integer merId;


    /** 管理员回复内容 */

    private String merchantReplyContent;


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


    // 用户名处理
    private String username;

    // 所在商户nickname
    private String nickName;

    // 所在商户username
    private String merUsername;

    // 评论图片
    private List<String> images;

    // 评论时间
    private String addTimeStr;

    // 卡券信息
    private String couponName;

    // 订单编号
    private String orderNo;
}
