package co.yixiang.modules.bank.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 银行机构编码 查询参数对象
 * </p>
 *
 * @author sss
 * @date 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BankCnapsQueryParam对象", description="银行机构编码查询参数")
public class BankCnapsQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
