package co.yixiang.modules.coupon.service.dto;

import co.yixiang.annotation.Query;
import co.yixiang.modules.shop.service.dto.BaseCriteria;
import lombok.Data;

import java.util.List;

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

    @Query(type = Query.Type.EQUAL)
    private Integer orderStatus;

    @Query(type = Query.Type.EQUAL)
    private String orderType;

    @Query(type = Query.Type.EQUAL)
    private String value;

    @Query(type = Query.Type.EQUAL)
    private String storeName;

    private List<String> createTime;
}
