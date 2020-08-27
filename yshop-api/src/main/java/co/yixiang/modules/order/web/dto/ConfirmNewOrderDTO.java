package co.yixiang.modules.order.web.dto;

import co.yixiang.modules.shop.web.vo.YxStoreStoreCartQueryVo;
import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ConfirmOrderDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class ConfirmNewOrderDTO implements Serializable {
    //地址信息
    @ApiModelProperty(value = "地址信息")
    private YxUserAddress addressInfo;


    @ApiModelProperty(value = "店铺&购物车产品信息")
    private List<YxStoreStoreCartQueryVo> cartInfo;

/*
    //优惠券减
    private Boolean deduction = false;

    private Boolean enableIntegral = true;

    private Double enableIntegralNum = 0d;

    //积分抵扣
    private Integer integralRatio = 0;*/
    @ApiModelProperty(value = "orderKey，创建订单时传值")
    private String orderKey;

    @ApiModelProperty(value = "用户信息")
    private YxUserQueryVo userInfo;

}
