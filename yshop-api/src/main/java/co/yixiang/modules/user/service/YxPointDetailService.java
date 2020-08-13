package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxPointDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.YxPointDetailQueryParam;
import co.yixiang.modules.user.web.vo.YxPointDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 积分获取明细 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxPointDetailService extends BaseService<YxPointDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxPointDetailQueryVo getYxPointDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxPointDetailQueryParam
     * @return
     */
    Paging<YxPointDetailQueryVo> getYxPointDetailPageList(YxPointDetailQueryParam yxPointDetailQueryParam) throws Exception;

}
