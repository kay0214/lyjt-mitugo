package co.yixiang.modules.manage.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@ApiModel(value = "User对象", description = "系统用户")
public class SystemUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "上级id")
    private Integer parentId;

    @ApiModelProperty(value = "商户联系人")
    private String merchantsContact;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "商户状态：0->启用,1->禁用")
    private Integer merchantsStatus;

    @ApiModelProperty(value = "总积分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "推荐用二维码地址")
    private String qrCodeUrl;

    @ApiModelProperty(value = "商户可提金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "商户累计金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "前端密码")
    private String userpassword ;
}
