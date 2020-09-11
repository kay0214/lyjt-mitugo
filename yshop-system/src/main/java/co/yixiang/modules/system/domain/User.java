/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author liusy
 * @date 2020-08-18
 */
@Data
@TableName("user")
public class User implements Serializable {

    /** 系统用户ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /** 头像 */
    private Long avatarId;


    /** 邮箱 */
    private String email;


    /** 状态：1启用、0禁用 */
    private Boolean enabled;

    /** 用户头像 */
    @TableField(exist = false)
    private String avatar;

    /** 用户角色 */
    @TableField(exist = false)
    private Set<Role> roles;

    /** 用户职位*/
    @TableField(exist = false)
    private Job job;

    /** 用户部门*/
    @TableField(exist = false)
    private Dept dept;

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


    /** 昵称 */
    private String nickName;


    /** 性别 */
    private String sex;

    public @interface Update {
    }

    /** 用户角色：0->平台运营,1->合伙人,2->商户 */
    private Integer userRole;

    /* 下级用户*/
    @TableField(exist = false)
    private List<Long> childUser;


    /** 商户联系人 */
    private String merchantsContact;


    /** 联系电话 */
    private String contactPhone;


    /** 商户状态：0->启用,1->禁用 */
    private Integer merchantsStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username);
    }

    /** 总积分 */
    private BigDecimal totalScore;


    /** 推荐用二维码地址 */
    private String qrCodeUrl;


    /** 上级id */
    private Integer parentId;


    /** 商户是否认证通过 0:未认证 1：已认证 */
    private Integer examineStatus;

    /**
     * 前端密码
     */
    private String userpassword ;

    //可提现金额
//    private BigDecimal withdrawalAmount;


    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public void copy(User source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
