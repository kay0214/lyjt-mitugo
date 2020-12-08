package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxMerchantsSettlement;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.YxMerchantsSettlementQueryParam;
import co.yixiang.modules.user.web.vo.YxMerchantsSettlementQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 商家入驻表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxMerchantsSettlementService extends BaseService<YxMerchantsSettlement> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxMerchantsSettlementQueryVo getYxMerchantsSettlementById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxMerchantsSettlementQueryParam
     * @return
     */
    Paging<YxMerchantsSettlementQueryVo> getYxMerchantsSettlementPageList(YxMerchantsSettlementQueryParam yxMerchantsSettlementQueryParam) throws Exception;

}
