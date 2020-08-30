package co.yixiang.modules.coupon.service.dto;

import co.yixiang.annotation.Query;
import co.yixiang.modules.shop.service.dto.BaseCriteria;
import lombok.Data;

/**
* @author huiy
* @date 2020-08-14
*/
@Data
public class YxCouponOrderQueryCriteria extends BaseCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String realName;

    @Query(type = Query.Type.EQUAL)
    private String orderId;

    @Query(type = Query.Type.EQUAL)
    private Integer status;

}
