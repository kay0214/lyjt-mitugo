/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author zhangyk
* @date 2020-08-15
*/
@Data
public class UserDto implements Serializable {

    /** ID */

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

    private Timestamp createTime;


    /** 最后修改密码的日期 */

    private Timestamp lastPasswordResetTime;



    private String nickName;



    private String sex;


    /** 用户角色：0->平台运营,1->合伙人,2->商户 */

    private Integer userRole;


    /** 商户联系人 */

    private String merchantsContact;


    /** 联系电话 */

    private String contactPhone;


    /** 商户状态：0->启用,1->禁用 */

    private Integer merchantsStatus;


    /** 总积分 */

    private Integer totalScore;


    /** 推荐用二维码地址 */

    private String qrCodeUrl;

}
