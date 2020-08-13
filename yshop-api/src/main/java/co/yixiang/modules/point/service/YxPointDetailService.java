package co.yixiang.modules.point.service;

import co.yixiang.modules.point.entity.YxPointDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.point.web.param.YxPointDetailQueryParam;
import co.yixiang.modules.point.web.vo.YxPointDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 积分获取明细 服务类
 * </p>
 *
 * @author zqq
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
