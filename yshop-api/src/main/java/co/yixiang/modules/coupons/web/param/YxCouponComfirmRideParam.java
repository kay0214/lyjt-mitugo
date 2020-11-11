package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 卡券订单表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponComfirmRideParam对象", description="确认乘坐参数对象")
public class YxCouponComfirmRideParam extends QueryParam {

    @ApiModelProperty(value = "订单号")
    private Integer orderId;
    @ApiModelProperty(value = "使用数量")
    private Integer usedNum;
    @ApiModelProperty(value = "乘客信息列表")
    private List<YxCouponOrderPassDetailParam> passengerList;
}
