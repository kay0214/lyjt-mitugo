package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderCountDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/30
 **/
@Data
public class OrderCountVO implements Serializable {
    @ApiModelProperty(value = "待付款数量")
    private Integer waitPayCount;
    @ApiModelProperty(value = "待使用数量")
    private Integer waitUseCount;
    @ApiModelProperty(value = "已使用数量")
    private Integer usedCount;
    @ApiModelProperty(value = "退款数量")
    private Integer refundCount;
    @ApiModelProperty(value = "累计订单")
    private Integer totalCount;
    @ApiModelProperty(value = "总消费")
    private BigDecimal sumPrice;
}
