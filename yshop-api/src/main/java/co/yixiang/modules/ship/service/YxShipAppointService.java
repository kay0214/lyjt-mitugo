package co.yixiang.modules.ship.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.YxShipAppointResultVo;
import co.yixiang.modules.couponUse.param.ShipInAppotionDaysParam;
import co.yixiang.modules.couponUse.param.ShipInAppotionParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    List<YxShipAppointResultVo> getAppointByDate(List<String> dateList,Integer storeId,Integer shipId);
    /**
     * 新增船只预约
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> saveAppotionInfo(ShipInAppotionParam param, SystemUser user);

    /**
     * 显示预约详情
     * @param param
     * @param user
     * @return
     */
    Map<String,Object> getAppotionByDate(ShipInAppotionDaysParam param, SystemUser user);
}
