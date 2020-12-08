package co.yixiang.modules.manage.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 系统用户头像 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UserAvatarQueryParam对象", description="系统用户头像查询参数")
public class UserAvatarQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
