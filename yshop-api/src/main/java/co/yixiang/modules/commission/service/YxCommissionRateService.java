package co.yixiang.modules.commission.service;

import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.commission.web.param.YxCommissionRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCommissionRateQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 分佣配置表 服务类
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
public interface YxCommissionRateService extends BaseService<YxCommissionRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCommissionRateQueryVo getYxCommissionRateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCommissionRateQueryParam
     * @return
     */
    Paging<YxCommissionRateQueryVo> getYxCommissionRatePageList(YxCommissionRateQueryParam yxCommissionRateQueryParam) throws Exception;

}
