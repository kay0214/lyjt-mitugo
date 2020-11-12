package co.yixiang.modules.manage.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 用户角色关联 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UsersRolesQueryParam对象", description="用户角色关联查询参数")
public class UsersRolesQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
