package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 用户角色关联 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-12
 */
@Data
@ApiModel(value="UsersRolesQueryVo对象", description="用户角色关联查询参数")
public class UsersRolesQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "用户ID")
private Long userId;

@ApiModelProperty(value = "角色ID")
private Long roleId;

}