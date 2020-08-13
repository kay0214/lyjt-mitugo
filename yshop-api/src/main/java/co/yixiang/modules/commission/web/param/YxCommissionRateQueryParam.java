package co.yixiang.modules.commission.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 分佣配置表 查询参数对象
 * </p>
 *
 * @author zqq
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCommissionRateQueryParam对象", description="分佣配置表查询参数")
public class YxCommissionRateQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
