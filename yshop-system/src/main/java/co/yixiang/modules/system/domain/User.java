/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.system.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;
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
    @TableId
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
    @NotBlank(message = "请填写用户名称")
    private String username;


    /** 部门名称 */
    private Long deptId;


    /** 手机号码 */
    @NotBlank(message = "请输入手机号码")
    private String phone;


    /** 岗位名称 */
    private Long jobId;


    /** 创建日期 */
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 最后修改密码的日期 */
    private Timestamp lastPasswordResetTime;


    /** 昵称 */
    private String nickName;


    /** 性别 */
    private String sex;

    public @interface Update {}

    /** 用户角色：0->平台运营,1->合伙人,2->商户 */
    @NotNull
    private Integer userRole;


    /** 商户联系人 */
    @NotBlank
    private String merchantsContact;


    /** 联系电话 */
    @NotBlank
    private String contactPhone;


    /** 商户状态：0->启用,1->禁用 */
    @NotNull
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
    private Integer totalScore;


    /** 推荐用二维码地址 */
    private String qrCodeUrl;


    /** 上级id */
    private Integer parentId;


    /** 商户是否认证通过 0:未认证 1：已认证 */
    private Integer examineStatus;


    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
    public void copy(User source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
