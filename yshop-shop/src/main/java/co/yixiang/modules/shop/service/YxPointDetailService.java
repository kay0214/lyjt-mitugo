package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxPointDetail;
import co.yixiang.modules.shop.service.dto.YxPointDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
* @author huiy
* @date 2020-08-19
*/
public interface YxPointDetailService  extends BaseService<YxPointDetail>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxPointDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxPointDetailDto>
    */
    List<YxPointDetail> queryAll(YxPointDetailQueryCriteria criteria);

}
