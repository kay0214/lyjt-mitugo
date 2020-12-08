package co.yixiang.modules.ship.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.param.YxCouponComfirmRideParam;
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 确认乘坐，保存乘客信息
     * @param couponComfirmRideParam
     */
    boolean saveComfrieRideInfo(YxCouponComfirmRideParam couponComfirmRideParam);

    /**
     * 根据订单编号获取乘客信息
     * @param orderId
     * @return
     */
    List<YxShipPassengerQueryVo> getPassengerByOrderId(int orderId);
}
