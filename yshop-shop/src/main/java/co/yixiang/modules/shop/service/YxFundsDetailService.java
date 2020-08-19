package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxFundsDetail;
import co.yixiang.modules.shop.service.dto.YxFundsDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
* @author huiy
* @date 2020-08-19
*/
public interface YxFundsDetailService extends BaseService<YxFundsDetail>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxFundsDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FundsDetailDto>
    */
    List<YxFundsDetail> queryAll(YxFundsDetailQueryCriteria criteria);

}
