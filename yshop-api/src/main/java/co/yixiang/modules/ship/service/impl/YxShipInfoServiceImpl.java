package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.couponUse.param.ShipInfoQueryParam;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.mapper.YxShipInfoMapper;
import co.yixiang.modules.ship.mapper.YxShipSeriesMapper;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.DateUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Override
    public YxShipInfoQueryVo getYxShipInfoById(Serializable id) throws Exception{
        return yxShipInfoMapper.getYxShipInfoById(id);
    }

    @Override
    public Paging<YxShipInfoQueryVo> getYxShipInfoPageList(YxShipInfoQueryParam yxShipInfoQueryParam){
        Page page = setPageParam(yxShipInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipInfoQueryVo> iPage = yxShipInfoMapper.getYxShipInfoPageList(page,yxShipInfoQueryParam);
        return new Paging(iPage);
    }

    @Override
    public Map<String,Object> getShipInfoList(ShipInfoQueryParam shipInfoQueryParam, SystemUser user){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "99");
        map.put("statusDesc", "暂无船只信息！");

        YxShipInfoQueryParam yxShipInfoQueryParam = new YxShipInfoQueryParam();
        BeanUtils.copyProperties(shipInfoQueryParam,yxShipInfoQueryParam);
        yxShipInfoQueryParam.setShipName(shipInfoQueryParam.getKeyword());
        yxShipInfoQueryParam.setStoreId(user.getStoreId());
        Paging<YxShipInfoQueryVo> paging = this.getYxShipInfoPageList(yxShipInfoQueryParam);
        if(paging.getTotal()>0){
            List<YxShipInfoQueryVo> infoQueryVoList = paging.getRecords();
            for(YxShipInfoQueryVo shipInfoQueryVo:infoQueryVoList){
                //0：在港，1：离港。2：维修中
                String statusFor="";
                switch (shipInfoQueryVo.getCurrentStatus()){
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
                String leaveTimeStr ="";
                String returnTimeStr="";
                if(0!=shipInfoQueryVo.getLastLeaveTime()){
                    leaveTimeStr = DateUtils.timestampToStr10(shipInfoQueryVo.getLastLeaveTime());
                }
                if(0!=shipInfoQueryVo.getLastReturnTime()){
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
            map.put("status", "1");
            map.put("statusDesc", "成功！");
            map.put("data",paging);
        }
        return map;
    }


    private String getShipImg(int shipId){
        QueryWrapper<YxImageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxImageInfo::getTypeId,shipId).eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_SHIPINFO).eq(YxImageInfo::getDelFlag,0);
        List<YxImageInfo> listImgs = yxImageInfoService.list(queryWrapper);
        if(CollectionUtils.isEmpty(listImgs)){
            return null;
        }
        return listImgs.get(0).getImgUrl();
    }


}
