package co.yixiang.modules.ship.service;

import co.yixiang.modules.couponUse.param.*;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;
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

    public Map<String, Object> getShipOperationList(YxShipOperationQueryParam yxShipOperationQueryParam, ShipOperationParam shipOperationParam, Integer captionId, Integer storeId);
    /**
     * 确认出港
     * @param uid
     * @param batchNo
     * @return
     */
    Map<String,Object> updateCaptainLeave(int uid, ShipOutInParam param);

    /**
     * 根据订单号获取详情
     * @param batchNo
     * @return
     */
    Map<String,Object> getShipOperationDeatalList(YxShipOperationDetailQueryParam param);

    /**
     * 修改船只状态
     * @param shipInfoChangeParam
     * @param uid
     * @return
     */
    Map<String,Object> changeStatus(ShipInfoChangeParam shipInfoChangeParam, int uid);
    /**
     * 修改船长信息
     * @param param
     * @param uid
     * @return
     */
    Map<String, Object> modifyCaptain(ShipCaptainModifyParam param, int uid);

    /**
     * 获取船长列表
     * @param storeId
     * @return
     */
    Map<String, Object> getCaptainList(int storeId);

    List<Integer> shipIdList(Integer seriesId, Integer storeId);

}
