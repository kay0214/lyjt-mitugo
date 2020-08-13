package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 积分获取明细 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxPointDetailQueryParam对象", description="积分获取明细查询参数")
public class YxPointDetailQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
