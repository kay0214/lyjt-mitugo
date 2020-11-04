package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 机器人客服表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCustomerServiceQueryParam对象", description="机器人客服表查询参数")
public class YxCustomerServiceQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
