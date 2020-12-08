package co.yixiang.modules.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户角色关联
 * </p>
 *
 * @author lsy
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UsersRoles对象", description="用户角色关联")
public class UsersRoles extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "用户ID")
@TableId(value = "user_id", type = IdType.ID_WORKER)
    private Long userId;

@ApiModelProperty(value = "角色ID")
private Long roleId;

}
