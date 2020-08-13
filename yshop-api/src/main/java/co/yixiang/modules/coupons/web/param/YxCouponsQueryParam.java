package co.yixiang.modules.coupons.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 本地生活, 卡券表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponsQueryParam对象", description="本地生活, 卡券表查询参数")
public class YxCouponsQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
