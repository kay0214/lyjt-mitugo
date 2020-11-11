package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value="YxCouponOrderQueryParam对象", description="卡券订单表查询参数")
public class YxCouponOrderPassDetailParam extends QueryParam {

    @ApiModelProperty(value = "联系人id")
    private Integer contactsId;
    @ApiModelProperty(value = "状态：0:未成年 1:成年人 2：老年人")
    private Integer isAdult;
    @ApiModelProperty(value = "是否合同签订人 0:否 1:是")
    private Integer signStatus;
}
