package co.yixiang.modules.image.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 图片表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxImageInfoQueryParam对象", description="图片表查询参数")
public class YxImageInfoQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
