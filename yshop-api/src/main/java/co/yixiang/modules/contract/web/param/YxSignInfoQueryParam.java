package co.yixiang.modules.contract.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 签章信息表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxSignInfoQueryParam对象", description="签章信息表查询参数")
public class YxSignInfoQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
