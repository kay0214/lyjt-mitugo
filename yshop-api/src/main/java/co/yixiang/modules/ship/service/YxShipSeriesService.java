package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipSeriesQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 船只系列表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipSeriesService extends BaseService<YxShipSeries> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipSeriesQueryVo getYxShipSeriesById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipSeriesQueryParam
     * @return
     */
    Paging<YxShipSeriesQueryVo> getYxShipSeriesPageList(YxShipSeriesQueryParam yxShipSeriesQueryParam) throws Exception;

}
