package co.yixiang.modules.bill.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 用户账单明细表 查询参数对象
 * </p>
 *
 * @author zqq
 * @date 2020-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxWxUserBillQueryParam对象", description="用户账单明细表查询参数")
public class YxWxUserBillQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
