package co.yixiang.modules.contract.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 合同签署表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxContractSignQueryParam对象", description="合同签署表查询参数")
public class YxContractSignQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
