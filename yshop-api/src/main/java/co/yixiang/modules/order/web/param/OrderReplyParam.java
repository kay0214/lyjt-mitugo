package co.yixiang.modules.order.web.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RefundParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/6
 **/
@Data
public class OrderReplyParam implements Serializable {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "评论内容")
    private List<StoreProductReplyParam> productReplyList;
}
