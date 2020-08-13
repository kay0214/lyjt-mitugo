package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxFundsDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.YxFundsDetailQueryParam;
import co.yixiang.modules.user.web.vo.YxFundsDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 平台资金明细 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxFundsDetailService extends BaseService<YxFundsDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxFundsDetailQueryVo getYxFundsDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxFundsDetailQueryParam
     * @return
     */
    Paging<YxFundsDetailQueryVo> getYxFundsDetailPageList(YxFundsDetailQueryParam yxFundsDetailQueryParam) throws Exception;

}
