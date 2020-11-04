package co.yixiang.modules.commission.service;

import co.yixiang.modules.commission.entity.YxNowRate;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.commission.web.param.YxNowRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxNowRateQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 购买时费率记录表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxNowRateService extends BaseService<YxNowRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxNowRateQueryVo getYxNowRateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxNowRateQueryParam
     * @return
     */
    Paging<YxNowRateQueryVo> getYxNowRatePageList(YxNowRateQueryParam yxNowRateQueryParam) throws Exception;

}
