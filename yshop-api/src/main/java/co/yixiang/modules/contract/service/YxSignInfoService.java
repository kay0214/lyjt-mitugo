package co.yixiang.modules.contract.service;

import co.yixiang.modules.contract.entity.YxSignInfo;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.contract.web.param.YxSignInfoQueryParam;
import co.yixiang.modules.contract.web.vo.YxSignInfoQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 签章信息表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-11
 */
public interface YxSignInfoService extends BaseService<YxSignInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSignInfoQueryVo getYxSignInfoById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxSignInfoQueryParam
     * @return
     */
    Paging<YxSignInfoQueryVo> getYxSignInfoPageList(YxSignInfoQueryParam yxSignInfoQueryParam) throws Exception;

}
