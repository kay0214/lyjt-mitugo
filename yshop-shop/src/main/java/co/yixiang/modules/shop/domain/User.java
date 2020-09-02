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
 * @author zhangyk
 * @date 2020-08-15
 */
@Data
@TableName("user")
public class User implements Serializable {

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /** 头像 */
    private Long avatarId;


    /** 邮箱 */
    private String email;


    /** 状态：1启用、0禁用 */
    private Long enabled;


    /** 密码 */
    private String password;


    /** 用户名 */
    private String username;


    /** 部门名称 */
    private Long deptId;


    /** 手机号码 */
    private String phone;


    /** 岗位名称 */
    private Long jobId;


    /** 创建日期 */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    /** 最后修改密码的日期 */
    private Timestamp lastPasswordResetTime;


    private String nickName;


    private String sex;


    /** 用户角色：0->平台运营,1->合伙人,2->商户 */
    @NotNull
    private Integer userRole;

    /** 上级id */
    private Integer parentId;


    /** 商户是否认证通过 0:未认证 1：已认证 */
    private Integer examineStatus;


    /** 商户联系人 */
    @NotBlank
    private String merchantsContact;


    /** 联系电话 */
    @NotBlank
    private String contactPhone;


    /** 商户状态：0->启用,1->禁用 */
    @NotNull
    private Integer merchantsStatus;


    /** 总积分 */
    private Integer totalScore;

    /** 可提现金额 */
    private BigDecimal withdrawalAmount;

    /** 累计获取金额 */
    private BigDecimal totalAmount;


    /** 推荐用二维码地址 */
    private String qrCodeUrl;


    public void copy(User source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
