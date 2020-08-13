package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 店铺属性表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreAttributeQueryParam对象", description="店铺属性表查询参数")
public class YxStoreAttributeQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
