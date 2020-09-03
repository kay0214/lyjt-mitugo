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
@ApiModel(value="本地生活分类列表", description="本地生活分类列表")
public class LocalLiveQueryParam extends QueryParam {

    @ApiModelProperty(value = "距离排序(asc或desc)")
    private String distanceOrder;

    @ApiModelProperty(value = "销量排序(asc或desc)")
    private String salesOrder;

    @ApiModelProperty(value = "价格排序(asc或desc)")
    private String priceOrder;

    @ApiModelProperty(value = "关键字")
    private String keyword;
}
