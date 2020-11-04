package co.yixiang.modules.commission.service;

import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.commission.web.param.YxCustomizeRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCustomizeRateQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 自定义分佣配置表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxCustomizeRateService extends BaseService<YxCustomizeRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCustomizeRateQueryVo getYxCustomizeRateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCustomizeRateQueryParam
     * @return
     */
    Paging<YxCustomizeRateQueryVo> getYxCustomizeRatePageList(YxCustomizeRateQueryParam yxCustomizeRateQueryParam) throws Exception;

}
