package co.yixiang.modules.funds.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 平台账户表 查询参数对象
 * </p>
 *
 * @author zqq
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxFundsAccountQueryParam对象", description="平台账户表查询参数")
public class YxFundsAccountQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
