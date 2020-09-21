/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liusy
 * @date 2020-08-19
 */
@Data
@TableName("yx_merchants_detail")
public class YxMerchantsDetail implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 用户id */
    private Integer uid;


    /** 审批状态：0->待审核,1->通过,2->驳回 */
    private Integer examineStatus;

    /** 商户所在省市区 */
    private String province;

    /** 商户地址 */
    private String address;


    /** 联系人 */
    private String contacts;


    /** 联系电话 */
    private String contactMobile;


    /** 邮箱 */
    private String mailbox;


    /** 认证类型：0->个人,1->企业,2->个体商户 */
    private Integer merchantsType;


    /** 银行账号 */
    private String bankNo;


    /** 开户省市 */
    private String openAccountProvince;


    /** 银行卡信息：0->对私账号,1->对公账号 */
    private Integer bankType;


    /** 开户名称 */
    private String openAccountName;


    /** 开户行 */
    private String openAccountBank;


    /** 开户支行 */
    private String openAccountSubbranch;


    /** 企业所在省市区 */
    private String companyProvince;


    /** 企业所在详细地址 */
    private String companyAddress;


    /** 公司名称 */
    private String companyName;


    /** 法定代表人 */
    private String companyLegalPerson;


    /** 公司电话 */
    private String companyPhone;


    /** 经营类目 */
    private String businessCategory;


    /** 主体资质类型 */
    private String qualificationsType;


    /** 是否删除（0：未删除，1：已删除） */
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    /** 商户名称 */
    private String merchantsName;

    /** 商户状态：0启用,1禁用 */
    private Integer status;

    /** 联行号 */
    private String bankCode;

    /** */
    private String qrcode;


    public void copy(YxMerchantsDetail source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
