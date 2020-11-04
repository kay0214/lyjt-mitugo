package co.yixiang.modules.system.service;

import co.yixiang.modules.system.entity.YxRefundReason;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.system.web.param.YxRefundReasonQueryParam;
import co.yixiang.modules.system.web.vo.YxRefundReasonQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 退款理由配置表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxRefundReasonService extends BaseService<YxRefundReason> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxRefundReasonQueryVo getYxRefundReasonById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxRefundReasonQueryParam
     * @return
     */
    Paging<YxRefundReasonQueryVo> getYxRefundReasonPageList(YxRefundReasonQueryParam yxRefundReasonQueryParam) throws Exception;

}
