package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 店铺表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreInfoQueryParam对象", description="店铺表查询参数")
public class YxStoreInfoQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
