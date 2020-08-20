package co.yixiang.modules.manage.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 数据字典 查询参数对象
 * </p>
 *
 * @author nxl
 * @date 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DictQueryParam对象", description = "数据字典查询参数")
public class DictQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
