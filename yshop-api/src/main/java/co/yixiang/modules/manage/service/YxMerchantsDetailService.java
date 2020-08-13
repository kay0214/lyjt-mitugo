package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.YxMerchantsDetailQueryParam;
import co.yixiang.modules.manage.web.vo.YxMerchantsDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 商户详情 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxMerchantsDetailService extends BaseService<YxMerchantsDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxMerchantsDetailQueryVo getYxMerchantsDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxMerchantsDetailQueryParam
     * @return
     */
    Paging<YxMerchantsDetailQueryVo> getYxMerchantsDetailPageList(YxMerchantsDetailQueryParam yxMerchantsDetailQueryParam) throws Exception;

}
