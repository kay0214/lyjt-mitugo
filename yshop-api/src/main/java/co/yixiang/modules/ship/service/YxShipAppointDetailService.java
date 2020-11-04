package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxShipAppointDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipAppointDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 船只预约表详情 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxShipAppointDetailService extends BaseService<YxShipAppointDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipAppointDetailQueryVo getYxShipAppointDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxShipAppointDetailQueryParam
     * @return
     */
    Paging<YxShipAppointDetailQueryVo> getYxShipAppointDetailPageList(YxShipAppointDetailQueryParam yxShipAppointDetailQueryParam) throws Exception;

}
