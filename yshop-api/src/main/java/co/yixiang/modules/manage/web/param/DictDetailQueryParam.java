package co.yixiang.modules.manage.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 数据字典详情 查询参数对象
 * </p>
 *
 * @author nxl
 * @date 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DictDetailQueryParam对象", description = "数据字典详情查询参数")
public class DictDetailQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
