/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author liusy
 * @date 2020-08-19
 */
@Data
public class YxExamineLogDto implements Serializable {

    /** 主键 */

    private Integer id;


    /** 审批类型 1:提现 2:商户信息 */

    private Integer type;


    /** 审核数据关联id */

    private Integer typeId;


    /** 审批状态：0->待审核,1->通过,2->驳回 */

    private Integer status;


    /** 审核说明 */

    private String remark;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人(审核人) */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;


    /** 冗余字段：被审核人id */

    private Integer uid;


    /** 冗余字段：被审核人信息 */

    private String username;

    /** 商户联系人 */
    private String contacts;

    /** 商户联系电话 */
    private String contactMobile;
    /** 商户名称 */
    private String merchantsName;

    @ApiModelProperty(value = "微信号")
    private String wechat;

    @ApiModelProperty(value = "0:预留 1:前台用户 2：后台商户 3：后台合伙人")
    private Integer userType;

    /** 提现金额 */
    private BigDecimal extractPrice;


    /** bank = 银行卡 alipay = 支付宝wx=微信 */
    private String extractType;
}
