package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 船只运营记录详情 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipOperationDetailService extends BaseService<YxShipOperationDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipOperationDetailQueryVo getYxShipOperationDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipOperationDetailQueryParam
     * @return
     */
    Paging<YxShipOperationDetailQueryVo> getYxShipOperationDetailPageList(YxShipOperationDetailQueryParam yxShipOperationDetailQueryParam) throws Exception;

    /**
     * 保存乘客详情
     * @param map
     */
    YxShipOperationDetail saveShipOperationDetail(Map<String, Object> map);
}
