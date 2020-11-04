package co.yixiang.modules.commission.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 购买时费率记录表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxNowRateQueryParam对象", description="购买时费率记录表查询参数")
public class YxNowRateQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
