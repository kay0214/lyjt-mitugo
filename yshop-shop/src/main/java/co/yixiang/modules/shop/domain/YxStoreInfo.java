/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.modules.mybatis.GeoPoint;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* @author nxl
* @date 2020-08-14
*/
@Data
@TableName("yx_store_info")
public class YxStoreInfo implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 店铺编号 */
    @NotBlank
    private String storeNid;


    /** 店铺名称 */
    @NotBlank
    private String storeName;


    /** 管理人用户名 */
    @NotBlank
    private String manageUserName;


    /** 商户id */
    @NotNull
    private Integer merId;


    /** 合伙人id */
    @NotNull
    private Integer partnerId;


    /** 管理人电话 */
    @NotBlank
    private String manageMobile;


    /** 店铺电话 */
    @NotBlank
    private String storeMobile;


    /** 状态：0：上架，1：下架 */
    @NotNull
    @TableField(value = "`status`")
    private Integer status;


    /** 人均消费 */
    private BigDecimal perCapita;


    /** 行业类别 */
    private Integer industryCategory;


    /** 店铺省市区 */
    @NotBlank
    private String storeProvince;


    /** 店铺详细地址 */
    @NotBlank
    private String storeAddress;


    /** 是否删除（0：未删除，1：已删除） */
    @NotNull
    @TableLogic
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    /** 店铺介绍 */
    private String introduction;

    @Column
    private GeoPoint coordinate;


    /** 地图坐标经度 */
    private String coordinateX;


    /** 地图坐标纬度 */
    private String coordinateY;


    public void copy(YxStoreInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
