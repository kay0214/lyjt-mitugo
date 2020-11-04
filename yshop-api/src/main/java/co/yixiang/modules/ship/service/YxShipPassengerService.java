package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 本地生活帆船订单乘客表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipPassengerService extends BaseService<YxShipPassenger> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipPassengerQueryVo getYxShipPassengerById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipPassengerQueryParam
     * @return
     */
    Paging<YxShipPassengerQueryVo> getYxShipPassengerPageList(YxShipPassengerQueryParam yxShipPassengerQueryParam) throws Exception;

}
