/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author nxl
 * @date 2020-08-14
 */
@Data
public class YxStoreInfoResponse implements Serializable {

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
    @TableField(value = "`status`")
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
    private String imageArr;
    //轮播图
    private String sliderImageArr;

    //营业时间
    private List<Map<String, Object>> openTime;
    //店铺服务
    private List<String> storeService;


    public void copy(YxStoreInfoResponse source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
