package co.yixiang.modules.order.web.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName OrderParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class OrderParam implements Serializable {
    @ApiModelProperty(value = "地址id")
    private String addressId;
    private Integer bargainId;
    private Integer combinationId;
    @ApiModelProperty(value = "优惠券id")
    private Integer couponId;
    @ApiModelProperty(value = "routine 小程序支付渠道")
    private String from;
    @ApiModelProperty(value = "备注")
    private String mark;
    @NotBlank(message = "请选择支付方式")
    @ApiModelProperty(value = "yue :余额支付，weixin：微信支付")
    private String payType;
    @ApiModelProperty(value = "电话")
    private String phone;
    private Integer pinkId = 0;
    @ApiModelProperty(value = "姓名")
    private String realName;
    private Integer seckillId;
    @ApiModelProperty(value = "配送方式 1：快递 ，2=门店自提")
    private Integer shippingType;
    @ApiModelProperty(value = "使用的积分")
    private Double useIntegral;
    private Integer isChannel = 1;
    private Integer storeId;
    @ApiModelProperty(value = "优惠券id(多个）")
    private List<Integer> couponIdList;
    @ApiModelProperty(value = "分享人id")
    private Integer spread;
}
