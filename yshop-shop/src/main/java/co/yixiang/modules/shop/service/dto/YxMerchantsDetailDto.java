/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author liusy
* @date 2020-08-19
*/
@Data
public class YxMerchantsDetailDto implements Serializable {


    private Integer id;


    /** 用户id */

    private Integer uid;


    /** 审批状态：0->待审核,1->通过,2->驳回 */

    private Integer examineStatus;


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

    // ------------------- 认证类型为个人的 ---------------------
    /**
     * 手持证件照
     */
    private String personIdCard;

    /**
     * 证件照人像面
     */
    private String personIdCardFace;

    /**
     * 证件照国徽面
     */
    private String personIdCardBack;
    // ------------------- 认证类型为个人的 ---------------------

    // ------------------- 认证类型为企业 ------------------
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
    // ------------------- 认证类型为企业 ------------------
    // --------------------认证类型为企业或个体户---------------------------
    /** 经营类目 */

    private Integer businessCategory;


    /** 主体资质类型 */

    private Integer qualificationsType;


    /**
     * 营业执照
     */
    private String businessLicenseImg;

    /**
     * 银行开户证明
     */
    private String bankOpenProveImg;

    /**
     * 法人身份证头像面
     */
    private String legalIdCardFace;
    /**
     * 法人身份证国徽面
     */
    private String legalIdCardBack;
    /**
     * 门店照及经营场所
     */
    private String storeImg;
    /**
     * 医疗机构许可证
     */
    private String licenceImg;
    // --------------------认证类型为企业或个体户---------------------------

    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;

    /**  审核意见 */
    private String examineRemark;

    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;


    /** 商户名称 */

    private String merchantsName;

}
