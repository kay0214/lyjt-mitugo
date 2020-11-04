package co.yixiang.modules.contract.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 合同模板 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxContractTemplateQueryParam对象", description="合同模板查询参数")
public class YxContractTemplateQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
