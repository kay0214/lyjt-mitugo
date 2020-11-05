/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
* @author liusy
* @date 2020-08-18
*/
@Data
public class UserDto implements Serializable {

    /** ID */
    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String nickName;

    /** 头像 */
    private Long avatarId;

    private String sex;

    private String avatar;

    /** 邮箱 */
    private String email;

    private String phone;

    private Boolean enabled;

    /** 密码 */
    @JsonIgnore
    private String password;

    private Timestamp lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDto> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDto job;


    /** 部门名称 */

    private Long deptId;

    /** 岗位名称 */

    private Long jobId;


    /** 创建日期 */

    private Timestamp createTime;

    /** 用户角色：0->平台运营,1->合伙人,2->商户 */

    private Integer userRole;

    // 下级用户id
    private List<Long> childUser;


    /** 商户联系人 */

    private String merchantsContact;


    /** 联系电话 */

    private String contactPhone;


    /** 商户状态：0->启用,1->禁用 */

    private Integer merchantsStatus;


    /** 总积分 */

    private BigDecimal totalScore;


    /** 推荐用二维码地址 */

    private String qrCodeUrl;


    private DeptSmallDto dept;
    /** 上级id */
    private Integer parentId;


    /** 商户是否认证通过 0:未认证 1：已认证 */

    private Integer examineStatus;

    private String userpassword;

    // 所属店铺id
    private Integer storeId;

    // 是否允许提现（0：允许，1：拒绝）
    private Integer withdrawalFlg;

    // 启用收款码（0：启用，1：禁用）
    private Integer useCodeFlg;

    // 是否是船只用户（0：是，1：否）
    private Integer shipUser;
}
