package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 船只运营记录 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipOperationService extends BaseService<YxShipOperation> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipOperationQueryVo getYxShipOperationById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipOperationQueryParam
     * @return
     */
    Paging<YxShipOperationQueryVo> getYxShipOperationPageList(YxShipOperationQueryParam yxShipOperationQueryParam) throws Exception;

}
