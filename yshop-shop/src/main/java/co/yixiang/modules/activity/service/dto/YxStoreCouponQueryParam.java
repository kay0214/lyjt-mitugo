package co.yixiang.modules.activity.service.dto;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 优惠券表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreCouponQueryParam对象", description="优惠券表查询参数")
public class YxStoreCouponQueryParam extends QueryParam {
    private String userName;
    private String couponTitle;
    private Integer pageStart;
    private Integer pageEnd;

}
