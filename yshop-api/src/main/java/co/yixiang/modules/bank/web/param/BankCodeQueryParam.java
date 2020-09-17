package co.yixiang.modules.bank.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 联行号表 查询参数对象
 * </p>
 *
 * @author sss
 * @date 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BankCodeQueryParam对象", description="联行号表查询参数")
public class BankCodeQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
