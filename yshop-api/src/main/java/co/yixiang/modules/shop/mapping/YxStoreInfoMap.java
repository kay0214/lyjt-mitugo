package co.yixiang.modules.shop.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-19
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxStoreInfoMap extends EntityMapper<YxStoreInfoQueryVo, YxStoreInfo> {

}