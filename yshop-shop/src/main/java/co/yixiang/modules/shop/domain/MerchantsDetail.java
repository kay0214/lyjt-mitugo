/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author liusy
* @date 2020-08-14
*/
@Data
@TableName("yx_merchants_detail")
public class MerchantsDetail implements Serializable {

    @TableId
    private Integer id;


    /** 用户id */
    @NotNull
    private Integer uid;


    /** 审批状态：0->待审核,1->通过,2->驳回 */
    @NotNull
    private Integer examineStatus;


    /** 商户地址 */
    @NotBlank
    private String address;


    /** 联系人 */
    @NotBlank
    private String contacts;


    /** 联系电话 */
    @NotBlank
    private String contactMobile;


    /** 邮箱 */
    @NotBlank
    private String mailbox;


    /** 认证类型：0->个人,1->企业,2->个体商户 */
    @NotNull
    private Integer merchantsType;


    /** 银行账号 */
    @NotBlank
    private String bankNo;


    /** 开户省市 */
    @NotBlank
    private String openAccountProvince;


    /** 银行卡信息：0->对私账号,1->对公账号 */
    @NotNull
    private Integer bankType;


    /** 开户名称 */
    @NotBlank
    private String openAccountName;


    /** 开户行 */
    @NotBlank
    private String openAccountBank;


    /** 开户支行 */
    @NotBlank
    private String openAccountSubbranch;


    /** 企业所在省市区 */
    @NotBlank
    private String companyProvince;


    /** 企业所在详细地址 */
    @NotBlank
    private String companyAddress;


    /** 公司名称 */
    @NotBlank
    private String companyName;


    /** 法定代表人 */
    @NotBlank
    private String companyLegalPerson;


    /** 公司电话 */
    @NotBlank
    private String companyPhone;


    /** 经营类目 */
    @NotNull
    private Integer businessCategory;


    /** 主体资质类型 */
    @NotNull
    private Integer qualificationsType;


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
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    public void copy(MerchantsDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
