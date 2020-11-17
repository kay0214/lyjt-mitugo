package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.couponUse.dto.YxShipOperationDetailVO;
import co.yixiang.modules.couponUse.dto.YxShipPassengerVO;
import co.yixiang.modules.couponUse.param.ShipCaptainModifyParam;
import co.yixiang.modules.couponUse.param.ShipInOperationParam;
import co.yixiang.modules.couponUse.param.ShipInfoChangeParam;
import co.yixiang.modules.couponUse.param.ShipInfoQueryParam;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.mapper.YxCouponOrderUseMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.mapper.SystemUserMapper;
import co.yixiang.modules.manage.web.vo.SystemUserQueryVo;
import co.yixiang.modules.ship.entity.*;
import co.yixiang.modules.ship.mapper.YxShipInfoMapper;
import co.yixiang.modules.ship.mapper.YxShipOperationDetailMapper;
import co.yixiang.modules.ship.mapper.YxShipOperationMapper;
import co.yixiang.modules.ship.mapper.YxShipSeriesMapper;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.service.YxShipOperationService;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.modules.ship.web.vo.YxShipOpeartionVo;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 船只表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipInfoServiceImpl extends BaseServiceImpl<YxShipInfoMapper, YxShipInfo> implements YxShipInfoService {

    @Autowired
    private YxShipInfoMapper yxShipInfoMapper;
    @Autowired
    private YxShipSeriesMapper yxShipSeriesMapper;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxShipOperationDetailMapper yxShipOperationDetailMapper;
    @Autowired
    private YxShipOperationMapper yxShipOperationMapper;
    @Autowired
    private YxCouponOrderUseMapper yxCouponOrderUseMapper;
    @Autowired
    private YxShipOperationService yxShipOperationService;
    @Autowired
    private YxShipPassengerService yxShipPassengerService;

    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private SystemUserMapper systemUserMapper;


    @Override
    public YxShipInfoQueryVo getYxShipInfoById(Serializable id) throws Exception {
        return yxShipInfoMapper.getYxShipInfoById(id);
    }

    @Override
    public Paging<YxShipInfoQueryVo> getYxShipInfoPageList(YxShipInfoQueryParam yxShipInfoQueryParam) {
        Page page = setPageParam(yxShipInfoQueryParam, OrderItem.desc("create_time"));
        IPage<YxShipInfoQueryVo> iPage = yxShipInfoMapper.getYxShipInfoPageList(page, yxShipInfoQueryParam);
        return new Paging(iPage);
    }

    @Override
    public Map<String, Object> getShipInfoList(ShipInfoQueryParam shipInfoQueryParam, SystemUser user) {
        Map<String, Object> map = new HashMap<>();
        YxShipInfoQueryParam yxShipInfoQueryParam = new YxShipInfoQueryParam();
        BeanUtils.copyProperties(shipInfoQueryParam, yxShipInfoQueryParam);
        yxShipInfoQueryParam.setShipName(shipInfoQueryParam.getKeyword());
        yxShipInfoQueryParam.setStoreId(user.getStoreId());
        Paging<YxShipInfoQueryVo> paging = this.getYxShipInfoPageList(yxShipInfoQueryParam);
        if (paging.getTotal() > 0) {
            List<YxShipInfoQueryVo> infoQueryVoList = paging.getRecords();
            for (YxShipInfoQueryVo shipInfoQueryVo : infoQueryVoList) {
                //0：在港，1：离港。2：维修中
                String statusFor = "";
                switch (shipInfoQueryVo.getCurrentStatus()) {
                    case 0:
                        statusFor = "在港";
                        break;
                    case 1:
                        statusFor = "离港";
                        break;
                    case 2:
                        statusFor = "维修中";
                        break;
                }
                shipInfoQueryVo.setCurrentStatusFormat(statusFor);
                String leaveTimeStr = "";
                String returnTimeStr = "";
                if (0 != shipInfoQueryVo.getLastLeaveTime()) {
                    leaveTimeStr = DateUtils.timestampToStr10(shipInfoQueryVo.getLastLeaveTime());
                }
                if (0 != shipInfoQueryVo.getLastReturnTime()) {
                    returnTimeStr = DateUtils.timestampToStr10(shipInfoQueryVo.getLastReturnTime());
                }
                shipInfoQueryVo.setLastReturnTimeFormat(returnTimeStr);
                shipInfoQueryVo.setLastLeaveTimeFormat(leaveTimeStr);
                //
                YxShipSeriesQueryVo yxShipSeriesQueryVo = yxShipSeriesMapper.getYxShipSeriesById(shipInfoQueryVo.getSeriesId());
                shipInfoQueryVo.setSeriesName(yxShipSeriesQueryVo.getSeriesName());
                //图片
                shipInfoQueryVo.setShipImageUrl(getShipImg(shipInfoQueryVo.getId()));

            }
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        map.put("data", paging);
        return map;
    }


    private String getShipImg(int shipId) {
        QueryWrapper<YxImageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxImageInfo::getTypeId, shipId).eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_SHIPINFO).eq(YxImageInfo::getDelFlag, 0);
        List<YxImageInfo> listImgs = yxImageInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(listImgs)) {
            return null;
        }
        return listImgs.get(0).getImgUrl();
    }


    @Override
    public Map<String, Object> getShipOperationList(YxShipOperationQueryParam yxShipOperationQueryParam,ShipInOperationParam shipInOperationParam, Integer captionId, Integer storeId) {
        Map<String, Object> map = new HashMap<>();
       /* map.put("status", "99");
        map.put("statusDesc", "暂无船只运营信息！");*/

        if (null != captionId) {
            //船长id = null
            yxShipOperationQueryParam.setCaptainId(captionId);
        }
        BeanUtils.copyProperties(shipInOperationParam, yxShipOperationQueryParam);
        Page page = setPageParam(yxShipOperationQueryParam, OrderItem.desc("create_time"));
        //
        //根据系列id获取船只id
        List<Integer> shipIds = shipIdList(shipInOperationParam.getSeriesId(), storeId);
        yxShipOperationQueryParam.setShipIdList(shipIds);

        if (StringUtils.isNotBlank(shipInOperationParam.getDateStatus())) {
            // 日期
            Map<String, String> mapParam = getDateFormat(shipInOperationParam.getDateStatus());
            yxShipOperationQueryParam.setEndTime(mapParam.get("endDate"));
            yxShipOperationQueryParam.setStartTime(mapParam.get("startDate"));
        }
        IPage<YxShipOpeartionVo> shipOpertionPage = yxShipOperationMapper.getYxShipOpeartionVoPageList(page, yxShipOperationQueryParam);
        Paging<YxShipOpeartionVo> infoQueryVoPaging = new Paging(shipOpertionPage);
        if (infoQueryVoPaging.getTotal() > 0) {
            List<YxShipOpeartionVo> queryVoList = shipOpertionPage.getRecords();
            for (YxShipOpeartionVo queryVo : queryVoList) {
                //格式化出港&离港日期
                String leaveTimeStr = "";
                String returnTimeStr = "";
                if (null != queryVo.getLeaveTime() && 0 != queryVo.getLeaveTime()) {
                    leaveTimeStr = DateUtils.timestampToStr10(queryVo.getLeaveTime());
                }
                if (null != queryVo.getReturnTime() && 0 != queryVo.getReturnTime()) {
                    returnTimeStr = DateUtils.timestampToStr10(queryVo.getReturnTime());
                }
                queryVo.setLeaveTimeFormat(leaveTimeStr);
                queryVo.setReturnTimeFormat(returnTimeStr);
                String strStatus = "";
                switch (queryVo.getStatus()) {
                    //船只状态 0:待出港 1：出港 2：回港
                    case 0:
                        strStatus = "待出港";
                        break;
                    case 1:
                        strStatus = "出港";
                        break;
                    case 2:
                        strStatus = "回港";
                        break;
                }
                queryVo.setStatusFormat(strStatus);
                queryVo.setUserdUserName(systemUserMapper.getUserById(queryVo.getCreateUserId()).getNickName());
                queryVo.setUserdTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,queryVo.getCreateTime()));
            }
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        map.put("data", infoQueryVoPaging);
        return map;
    }

    @Override
    public List<Integer> shipIdList(Integer seriesId, Integer storeId) {
        List<Integer> listIds = new ArrayList<>();
        QueryWrapper<YxShipInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipInfo::getDelFlag, 0);
        if (null != seriesId) {
            queryWrapper.lambda().eq(YxShipInfo::getSeriesId, seriesId);
        }
        if (null != storeId) {
            queryWrapper.lambda().eq(YxShipInfo::getStoreId, storeId);
        }
        List<YxShipInfo> shipInfoList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(shipInfoList)) {
            return listIds;
        }
        for (YxShipInfo shipInfo : shipInfoList) {
            listIds.add(shipInfo.getId());
        }
        return listIds;
    }

    public Map<String, String> getDateFormat(String strFlg) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(new Date());
        Date dateBase = calendarDate.getTime();
        String strDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = format.format(dateBase) + " 23:59:59";
        Map<String, String> mapEnd = new HashMap<>();
        mapEnd.put("endDate", endDate);
        switch (strFlg) {
            case "1":
                //今日
                strDate = format.format(dateBase) + " 00:00:00";
                break;
            case "2":
                //近七天
                calendarDate.add(Calendar.DATE, -7);
                Date sevenDay = calendarDate.getTime();
                strDate = format.format(sevenDay) + " 00:00:00";
                break;
            case "3":
                //近一个月
                calendarDate.add(Calendar.MONTH, -1);
                Date lastMonthDay = calendarDate.getTime();
                strDate = format.format(lastMonthDay);
                break;
        }
        mapEnd.put("startDate", strDate);
        return mapEnd;
    }

    /**
     * 确认出港
     *
     * @param uid
     * @param batchNo
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updateCaptainLeave(int uid, String batchNo) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<YxShipOperation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipOperation::getBatchNo, batchNo);
        YxShipOperation yxShipOperation = yxShipOperationMapper.selectOne(queryWrapper);
        // 出港
        yxShipOperation.setStatus(1);
        yxShipOperation.setUpdateUserId(uid);
        yxShipOperation.setUpdateTime(new Date());
        yxShipOperation.setLeaveTime(DateUtils.getNowTime());
        yxShipOperationService.updateById(yxShipOperation);
        yxShipOperation.getShipId();
        YxShipInfo yxShipInfo = this.getById(yxShipOperation.getShipId());
        yxShipInfo.setLastLeaveTime(DateUtils.getNowTime());
        this.updateById(yxShipInfo);
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return map;
    }

    /**
     * 根据订单号获取详情
     *
     * @param batchNo
     * @return
     */
    @Override
    public Map<String, Object> getShipOperationDeatalList(String batchNo) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<YxShipOperationDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipOperationDetail::getBatchNo, batchNo);
        List<YxShipOperationDetail> shipOperationDetailList = yxShipOperationDetailMapper.selectList(queryWrapper);
        List<YxShipOperationDetailVO> detailVOList = new ArrayList<>();
        for (YxShipOperationDetail yxShipOperationDetail : shipOperationDetailList) {
            YxShipOperationDetailVO yxShipOperationDetailVO = new YxShipOperationDetailVO();
            BeanUtils.copyProperties(yxShipOperationDetail, yxShipOperationDetailVO);
            YxCouponOrder yxCouponOrder = yxCouponOrderService.getById(yxShipOperationDetail.getCouponOrderId());
            // 购买时间
            yxShipOperationDetailVO.setBuyDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yxCouponOrder.getCreateTime()));
            //系列名& 限乘人数
            YxShipSeries yxShipSeries = getShpSeriesByShipId(yxShipOperationDetailVO.getShipId());
            yxShipOperationDetailVO.setSeriesName(yxShipSeries.getSeriesName());
            yxShipOperationDetailVO.setRideLimit(yxShipSeries.getRideLimit());
            //核销时间
            yxShipOperationDetailVO.setUserdTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,yxShipOperationDetail.getCreateTime()));
            SystemUserQueryVo systemUser = systemUserMapper.getUserById(yxShipOperationDetail.getCreateUserId());
            // 核销人
            yxShipOperationDetailVO.setUserdUserName(systemUser.getNickName());

            QueryWrapper<YxShipPassenger> queryWrapperPasseng = new QueryWrapper<>();
            queryWrapperPasseng.lambda().eq(YxShipPassenger::getBatchNo, batchNo)
                    .eq(YxShipPassenger::getCouponOrderId, yxShipOperationDetail.getCouponOrderId())
                    .eq(YxShipPassenger::getDelFlag, 0);
            List<YxShipPassenger> passengerList = yxShipPassengerService.list(queryWrapperPasseng);
            List<YxShipPassengerVO> passengerVOList = CommonsUtils.convertBeanList(passengerList, YxShipPassengerVO.class);
            yxShipOperationDetailVO.setPassengerVOList(passengerVOList);
            detailVOList.add(yxShipOperationDetailVO);
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        map.put("data", detailVOList);
        return map;
    }

    private YxShipSeries getShpSeriesByShipId(int shipId) {
        YxShipInfo yxShipInfo = this.getById(shipId);
        YxShipSeries yxShipSeries = yxShipSeriesService.getById(yxShipInfo.getSeriesId());
        return yxShipSeries;
    }

    @Transactional
    @Override
    public Map<String, Object> changeStatus(ShipInfoChangeParam shipInfoChangeParam, int uid) {
        Map<String, Object> map = new HashMap<>();
        YxShipInfo shipInfo = this.getById(shipInfoChangeParam.getShipId());
        YxShipOperation yxShipOperation = new YxShipOperation();
        shipInfo.setCurrentStatus(shipInfoChangeParam.getCurrentStatus());
        if (0 == shipInfoChangeParam.getCurrentStatus()) {
            shipInfo.setLastReturnTime(DateUtils.getNowTime());
            yxShipOperation.setReturnTime(DateUtils.getNowTime());
            yxShipOperation.setUpdateUserId(uid);
            yxShipOperation.setUpdateTime(new Date());
            yxShipOperation.setStatus(2);
            QueryWrapper<YxShipOperation> queryWrapper = new QueryWrapper<>();
            //查询该船只状态为出港
            queryWrapper.lambda().eq(YxShipOperation::getShipId, shipInfoChangeParam.getShipId()).eq(YxShipOperation::getStatus, 1);
            yxShipOperationService.update(yxShipOperation, queryWrapper);
        }
        shipInfo.setUpdateTime(new Date());
        shipInfo.setUpdateUserId(uid);
        this.updateById(shipInfo);

        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return map;
    }

    /**
     * 获取船长列表
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Object> getCaptainList(int storeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "99");
        map.put("statusDesc", "暂无船长信息！");
        List<SystemUserQueryVo> systemUserQueryVoList = systemUserMapper.getCaptionListByStoreId(storeId);
        if(CollectionUtils.isEmpty(systemUserQueryVoList)){
            return map;
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        map.put("data", systemUserQueryVoList);
        return map;
    }

    /**
     * 修改船长信息
     * @param param
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> modifyCaptain(ShipCaptainModifyParam param,int uid) {
        Map<String, Object> map = new HashMap<>();

        QueryWrapper<YxShipOperation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipOperation::getBatchNo, param.getBatchNo());
        YxShipOperation yxShipOperation = yxShipOperationMapper.selectOne(queryWrapper);
        SystemUser systemUser = systemUserMapper.selectById(param.getCaptainId());
        yxShipOperation.setCaptainId(param.getCaptainId());
        yxShipOperation.setUpdateUserId(uid);
        yxShipOperation.setUpdateTime(new Date());
        yxShipOperation.setCaptainName(systemUser.getNickName());
        yxShipOperationService.updateById(yxShipOperation);

        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return map;
    }
}
