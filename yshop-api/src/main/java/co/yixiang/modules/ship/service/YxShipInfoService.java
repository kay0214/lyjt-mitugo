package co.yixiang.modules.ship.service;

import co.yixiang.modules.couponUse.param.ShipInfoQueryParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 船只表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipInfoService extends BaseService<YxShipInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipInfoQueryVo getYxShipInfoById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipInfoQueryParam
     * @return
     */
    Paging<YxShipInfoQueryVo> getYxShipInfoPageList(YxShipInfoQueryParam yxShipInfoQueryParam) throws Exception;

    Map<String,Object> getShipInfoList(ShipInfoQueryParam shipInfoQueryParam, SystemUser user);
}
