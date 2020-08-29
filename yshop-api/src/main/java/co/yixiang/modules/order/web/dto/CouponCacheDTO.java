package co.yixiang.modules.order.web.dto;

import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import lombok.Data;

import java.util.List;

/**
 * @Author : huanghui
 */
@Data
public class CouponCacheDTO {

    private List<YxCouponsQueryVo> couponsQueryVoList;
    private PriceGroupDTO priceGroup;
    private YxCouponOrder yxCouponOrder;
}
