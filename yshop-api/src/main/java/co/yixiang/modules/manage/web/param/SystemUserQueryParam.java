package co.yixiang.modules.manage.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 系统用户 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UserQueryParam对象", description="系统用户查询参数")
public class SystemUserQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
