package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 平台资金明细 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxFundsDetailQueryParam对象", description="平台资金明细查询参数")
public class YxFundsDetailQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
