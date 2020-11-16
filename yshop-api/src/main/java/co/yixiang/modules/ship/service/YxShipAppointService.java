package co.yixiang.modules.ship.service;

import co.yixiang.modules.couponUse.dto.YxShipAppointResultVo;
import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 船只预约表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipAppointService extends BaseService<YxShipAppoint> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipAppointQueryVo getYxShipAppointById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipAppointQueryParam
     * @return
     */
    Paging<YxShipAppointQueryVo> getYxShipAppointPageList(YxShipAppointQueryParam yxShipAppointQueryParam) throws Exception;

    List<String> getMonthAllDays(String strDate);

    List<YxShipAppointResultVo> getAppointByDate(List<String> dateList, Integer storeId);

}
