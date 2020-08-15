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
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-08-14
*/
@Data
public class YxStoreInfoDto implements Serializable {

    private Integer id;

    /** 店铺编号 */
    private String storeNid;

    /** 店铺名称 */
    private String storeName;

    /** 管理人用户名 */
    private String manageUserName;

    /** 商户id */
    private Integer merId;

    /** 合伙人id */
    private Integer partnerId;

    /** 管理人电话 */
    private String manageMobile;

    /** 店铺电话 */
    private String storeMobile;

    /** 状态：0：上架，1：下架 */
    private Integer status;

    /** 人均消费 */
    private BigDecimal perCapita;

    /** 行业类别 */
    private Integer industryCategory;

    /** 店铺省市区 */
    private String storeProvince;

    /** 店铺详细地址 */
    private String storeAddress;

    /** 创建人 */
    private Integer createUserId;

    /** 修改人 */
    private Integer updateUserId;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 店铺介绍 */
    private String introduction;

    /** 地图坐标经度 */
    private String coordinateX;

    /** 地图坐标纬度 */
    private String coordinateY;
    //图片
    //轮播图
    //店铺服务
    //营业时间



}
