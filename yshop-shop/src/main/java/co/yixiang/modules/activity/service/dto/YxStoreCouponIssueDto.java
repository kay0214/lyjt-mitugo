/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author hupeng
* @date 2020-05-13
*/
@Data
public class YxStoreCouponIssueDto implements Serializable {

    private Integer id;

    private String cname;

    /** 优惠券ID */
    private Integer cid;

    /** 优惠券领取开启时间 */
    private Integer startTime;

    /** 优惠券领取结束时间 */
    private Integer endTime;

    /** 优惠券领取数量 */
    private Integer totalCount;

    /** 优惠券剩余领取数量 */
    private Integer remainCount;

    /** 是否无限张数 */
    private Integer isPermanent;

    /** 1 正常 0 未开启 -1 已无效 */
    private Integer status;

    private Integer isDel;

    /** 优惠券添加时间 */
    private Integer addTime;

    private Timestamp endTimeDate;

    private Timestamp startTimeDate;
}
