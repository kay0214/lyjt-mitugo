package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : huanghui
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="首页tab数据", description="首页tab数据")
public class IndexTabQueryParam extends QueryParam {

    @ApiModelProperty(value = "1    2    3  ")
    private Integer type;
}
