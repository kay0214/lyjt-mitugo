package co.yixiang.modules.coupons.mapper;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-26
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CouponOrderMap extends EntityMapper<YxCouponOrderQueryVo, YxCouponOrder> {

}