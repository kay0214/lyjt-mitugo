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
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class YxStoreInfoRequest implements Serializable {

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
//    @NotNull
    private Integer merId;


    /** 合伙人id */
//    @NotNull
    private Integer partnerId;


    /** 管理人电话 */
    @NotBlank
    private String manageMobile;


    /** 店铺电话 */
    @NotBlank
    private String storeMobile;


    /** 状态：0：上架，1：下架 */
//    @NotNull
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
    private String imageArr;
    //轮播图
    private String sliderImageArr;

    //营业时间
    private String openDay;
    private String openTime;
    //店铺服务
    private String storeService;

    public void copy(YxStoreInfoRequest source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
