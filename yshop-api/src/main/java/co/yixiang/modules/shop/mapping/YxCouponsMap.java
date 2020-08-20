package co.yixiang.modules.shop.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-19
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxCouponsMap extends EntityMapper<YxCouponsQueryVo, YxCoupons> {

}