package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.mapper.YxShipOperationDetailMapper;
import co.yixiang.modules.ship.service.YxShipOperationDetailService;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationDetailQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 船只运营记录详情 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipOperationDetailServiceImpl extends BaseServiceImpl<YxShipOperationDetailMapper, YxShipOperationDetail> implements YxShipOperationDetailService {

    @Autowired
    private YxShipOperationDetailMapper yxShipOperationDetailMapper;
    @Autowired
    private YxShipPassengerService yxShipPassengerService;


    @Override
    public YxShipOperationDetailQueryVo getYxShipOperationDetailById(Serializable id) throws Exception{
        return yxShipOperationDetailMapper.getYxShipOperationDetailById(id);
    }

    @Override
    public Paging<YxShipOperationDetailQueryVo> getYxShipOperationDetailPageList(YxShipOperationDetailQueryParam yxShipOperationDetailQueryParam) throws Exception{
        Page page = setPageParam(yxShipOperationDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipOperationDetailQueryVo> iPage = yxShipOperationDetailMapper.getYxShipOperationDetailPageList(page,yxShipOperationDetailQueryParam);
        return new Paging(iPage);
    }

    /**
     * 保存乘客详情
     * @param map
     */
    @Override
    public YxShipOperationDetail saveShipOperationDetail(Map<String, Object> map) {
        YxShipOperation yxShipOperation = (YxShipOperation)map.get("yxShipOperation");
        // 查询所有已经完善乘坐人的订单
        YxCouponOrder yxCouponOrder = (YxCouponOrder)map.get("yxCouponOrder");
        QueryWrapper<YxShipPassenger> wrapper = new QueryWrapper<>();
        wrapper.eq("coupon_order_id", yxCouponOrder.getId())
                .eq("delFlag", 0);
        List<YxShipPassenger> list = yxShipPassengerService.list(wrapper);

        YxShipOperationDetail shipOperationDetail = new YxShipOperationDetail();

        shipOperationDetail.setBatchNo(yxShipOperation.getBatchNo());
        shipOperationDetail.setTotalPassenger(list.size());
        // 未成年
        int underagePassenger =0;
        // 老年人
        int oldPassenger =1;
        for (YxShipPassenger item : list) {
            if(item.getIsAdult().intValue()==0){
                underagePassenger++;
            }else if(item.getIsAdult().intValue()==2){
                oldPassenger++;
            }
        }
        shipOperationDetail.setUnderagePassenger(underagePassenger);
        shipOperationDetail.setOldPassenger(oldPassenger);
        shipOperationDetail.setCouponOrderId(yxCouponOrder.getId()+"");
        YxShipInfo shipInfo = (YxShipInfo)map.get("shipInfo");
        SystemUser captainUser = (SystemUser)map.get("captainUser");
        shipOperationDetail.setShipId(shipInfo.getId());

        shipOperationDetail.setShipId(shipInfo.getId());
        shipOperationDetail.setShipName(shipInfo.getShipName());
        shipOperationDetail.setCaptainId(captainUser.getId().intValue());
        shipOperationDetail.setCaptainName(captainUser.getNickName());

        SystemUser user = (SystemUser)map.get("user");
        // 核销人id
        shipOperationDetail.setUseId(user.getId().intValue());
        shipOperationDetail.setUseName(user.getNickName());
        // 乘客身体状况
        shipOperationDetail.setHealthStatus("");
        shipOperationDetail.setCreateUserId(user.getId().intValue());
        shipOperationDetail.setUpdateUserId(user.getId().intValue());
        yxShipOperationDetailMapper.insert(shipOperationDetail);
        map.put("shipOperationDetail",shipOperationDetail);
        return shipOperationDetail;
    }

}
