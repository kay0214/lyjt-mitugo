package co.yixiang.modules.system.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 退款理由配置表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxRefundReasonQueryParam对象", description="退款理由配置表查询参数")
public class YxRefundReasonQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
