package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 系统用户 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value="UserQueryVo对象", description="系统用户查询参数")
public class SystemUserQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "ID")
private Long id;

@ApiModelProperty(value = "头像")
private Long avatarId;

@ApiModelProperty(value = "邮箱")
private String email;

@ApiModelProperty(value = "状态：1启用、0禁用")
private Long enabled;

@ApiModelProperty(value = "密码")
private String password;

@ApiModelProperty(value = "用户名")
private String username;

@ApiModelProperty(value = "部门名称")
private Long deptId;

@ApiModelProperty(value = "手机号码")
private String phone;

@ApiModelProperty(value = "岗位名称")
private Long jobId;

@ApiModelProperty(value = "创建日期")
private Date createTime;

@ApiModelProperty(value = "最后修改密码的日期")
private Date lastPasswordResetTime;

private String nickName;

private String sex;

@ApiModelProperty(value = "用户角色：0->平台运营,1->合伙人,2->商户")
private Integer userRole;

@ApiModelProperty(value = "商户联系人")
private String merchantsContact;

@ApiModelProperty(value = "联系电话")
private String contactPhone;

@ApiModelProperty(value = "商户状态：0->启用,1->禁用")
private Integer merchantsStatus;

@ApiModelProperty(value = "总积分")
private Integer totalScore;

@ApiModelProperty(value = "推荐用二维码地址")
private String qrCodeUrl;

}