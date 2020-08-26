package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 卡券订单 实体
 * @Author : huanghui
 */
@Data
public class CouponOrderQueryVo {

    /** 购买数量 */
    private Integer quantity;

    private Double costPrice;

    /** 卡券信息 */
    List<YxCouponsQueryVo> yxCouponsQueryVoList;
}
