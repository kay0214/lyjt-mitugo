package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.web.param.YxCouponComfirmRideParam;
import co.yixiang.modules.coupons.web.param.YxCouponOrderPassDetailParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.mapper.YxShipPassengerMapper;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
import co.yixiang.modules.user.mapper.YxUsedContactsMapper;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.utils.CardNumUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 本地生活帆船订单乘客表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipPassengerServiceImpl extends BaseServiceImpl<YxShipPassengerMapper, YxShipPassenger> implements YxShipPassengerService {

    @Autowired
    private YxShipPassengerMapper yxShipPassengerMapper;

    @Autowired
    private YxCouponOrderMapper yxCouponOrderMapper;
    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;
    @Autowired
    private YxUsedContactsMapper yxUsedContactsMapper;
    @Autowired
    private YxCouponsMapper yxCouponsMapper;

    @Override
    public YxShipPassengerQueryVo getYxShipPassengerById(Serializable id) throws Exception{
        return yxShipPassengerMapper.getYxShipPassengerById(id);
    }

    @Override
    public Paging<YxShipPassengerQueryVo> getYxShipPassengerPageList(YxShipPassengerQueryParam yxShipPassengerQueryParam) throws Exception{
        Page page = setPageParam(yxShipPassengerQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipPassengerQueryVo> iPage = yxShipPassengerMapper.getYxShipPassengerPageList(page,yxShipPassengerQueryParam);
        return new Paging(iPage);
    }

    //

    /**
     * 确认乘坐，保存乘客信息
     * @param couponComfirmRideParam
     */
    @Transactional
    @Override
    public boolean saveComfrieRideInfo(YxCouponComfirmRideParam couponComfirmRideParam){
        //
        List<YxShipPassenger> yxShipPassengerList = new ArrayList<>();
        List<YxCouponOrderPassDetailParam> passDetailParamList = couponComfirmRideParam.getPassengerList();
        if(CollectionUtils.isEmpty(passDetailParamList)){
            throw new BadRequestException("乘客信息错误！");
        }
        YxCouponOrder couponOrder = yxCouponOrderMapper.selectById(couponComfirmRideParam.getOrderId());
        if(null == couponOrder){
            throw new BadRequestException("获取订单信息失败！");
        }
        YxCouponsQueryVo yxCouponsQueryVo = yxCouponsMapper.getYxCouponsById(couponOrder.getCouponId());
        if(null == yxCouponsQueryVo){
            throw new BadRequestException("获取卡券信息失败！");
        }
        for(YxCouponOrderPassDetailParam yxCouponOrderPassDetailParam:passDetailParamList){
            YxShipPassenger yxShipPassenger = new YxShipPassenger();
            YxUsedContactsQueryVo usedContactsQueryVo =  yxUsedContactsMapper.getYxUsedContactsById(yxCouponOrderPassDetailParam.getId());
            yxShipPassenger.setPassengerName(usedContactsQueryVo.getUserName());
            yxShipPassenger.setPhone(usedContactsQueryVo.getUserPhone());
            yxShipPassenger.setIdCard(usedContactsQueryVo.getCardId());
            yxShipPassenger.setIsAdult(yxCouponOrderPassDetailParam.getIsAdult());
            yxShipPassenger.setSignStatus(yxCouponOrderPassDetailParam.getSignStatus());
            yxShipPassenger.setCouponOrderId(couponComfirmRideParam.getOrderId());
            yxShipPassenger.setShipId(yxCouponsQueryVo.getShipId());
            yxShipPassenger.setBatchNo("");
            yxShipPassengerList.add(yxShipPassenger);
        }
        //批量保存乘客信息
       boolean instFlg = this.saveBatch(yxShipPassengerList);

        //使用张数
        QueryWrapper<YxCouponOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponOrderDetail::getOrderId,couponOrder.getOrderId()).eq(YxCouponOrderDetail::getDelFlag,0);
        List<YxCouponOrderDetail> detailList = yxCouponOrderDetailService.list(queryWrapper);
        if(CollectionUtils.isEmpty(detailList)){
            throw new BadRequestException("获取卡券订单详情数据错误！订单id："+couponOrder.getOrderId());
        }
        for(int i=0;i<couponComfirmRideParam.getUsedNum();i++) {
            YxCouponOrderDetail orderDetail = detailList.get(i);
            //可用
            orderDetail.setUserStatus(1);
        }
        boolean updFlg = yxCouponOrderDetailService.saveOrUpdateBatch(detailList);
        if (instFlg && updFlg) {
            return true;
        }
        return false;
    }

    /**
     * 根据订单编号获取乘客信息
     * @param orderId
     * @return
     */
    @Override
    public List<YxShipPassengerQueryVo> getPassengerByOrderId(int orderId) {
        List<YxShipPassengerQueryVo> passengerQueryVoList = new ArrayList<>();
        QueryWrapper<YxShipPassenger> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipPassenger::getCouponOrderId, orderId).eq(YxShipPassenger::getDelFlag, 0);
        List<YxShipPassenger> shipPassengerList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(shipPassengerList)) {
            for (YxShipPassenger shipPassenger : shipPassengerList) {
                YxShipPassengerQueryVo passengerQueryVo = new YxShipPassengerQueryVo();
                BeanUtils.copyProperties(shipPassenger, passengerQueryVo);
                passengerQueryVo.setIdCard(CardNumUtil.idEncrypt(shipPassenger.getIdCard()));
                passengerQueryVo.setPhone(CardNumUtil.mobileEncrypt(shipPassenger.getPhone()));
                passengerQueryVoList.add(passengerQueryVo);
            }
        }
        return passengerQueryVoList;
    }
}
