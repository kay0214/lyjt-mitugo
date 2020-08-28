package co.yixiang.modules.order.web.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PayDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/7
 **/
@Data
public class PayParam implements Serializable {
    @ApiModelProperty(value = "routine 小程序支付渠道")
    private String from;
    @ApiModelProperty(value = "yue :余额支付，weixin：微信支付")
    private String paytype;
    @ApiModelProperty(value = "订单唯一")
    private String uni;
}
