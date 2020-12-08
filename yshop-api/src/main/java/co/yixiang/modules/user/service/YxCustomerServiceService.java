package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxCustomerService;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.YxCustomerServiceQueryParam;
import co.yixiang.modules.user.web.vo.YxCustomerServiceQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 机器人客服表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxCustomerServiceService extends BaseService<YxCustomerService> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCustomerServiceQueryVo getYxCustomerServiceById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCustomerServiceQueryParam
     * @return
     */
    Paging<YxCustomerServiceQueryVo> getYxCustomerServicePageList(YxCustomerServiceQueryParam yxCustomerServiceQueryParam) throws Exception;

}
