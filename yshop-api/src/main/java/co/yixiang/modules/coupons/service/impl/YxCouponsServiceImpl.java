package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.CommonEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.couponUse.dto.YxCouponsDto;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsPriceConfigService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveCouponsVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.shop.mapping.YxCouponsMap;
import co.yixiang.utils.Base64Utils;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 本地生活, 卡券表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-08-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsServiceImpl extends BaseServiceImpl<YxCouponsMapper, YxCoupons> implements YxCouponsService
{

    @Autowired
    private YxCouponsMapper yxCouponsMapper;
    @Autowired
    private YxCouponsMap yxCouponsMap;

    @Autowired
    private YxImageInfoMapper yxImageInfoMapper;
    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;

    private final IGenerator generator;
    @Autowired
    private YxCouponsPriceConfigService yxCouponsPriceConfigService;

    public YxCouponsServiceImpl(IGenerator generator) {
        this.generator = generator;
    }

    @Override
    public YxCouponsQueryVo getYxCouponsById(Serializable id) throws Exception {
        return yxCouponsMapper.getYxCouponsById(id);
    }

    @Override
    public Paging<YxCouponsQueryVo> getYxCouponsPageList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        Page page = setPageParam(yxCouponsQueryParam, OrderItem.desc("sort desc,create_time"));
        IPage<YxCouponsQueryVo> iPage = yxCouponsMapper.getYxCouponsPageList(page, yxCouponsQueryParam);
        iPage.setTotal(yxCouponsMapper.getCount(yxCouponsQueryParam));
        return new Paging(iPage);
    }

    @Override
    public Paging<YxCouponsQueryVo> getCouponsHotList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        IPage<YxCouponsQueryVo> iPage = yxCouponsMapper.getCouponsHotList(new Page<>(yxCouponsQueryParam.getPage(), yxCouponsQueryParam.getLimit()), yxCouponsQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据商户id获取卡券信息
     *
     * @param storeId
     * @return
     */
    @Override
    public List<YxCouponsQueryVo> getCouponsInfoByStoreId(int storeId) {
        QueryWrapper<YxCoupons> wrapper = new QueryWrapper<YxCoupons>();
        wrapper.lambda().eq(YxCoupons::getDelFlag, CommonEnum.DEL_STATUS_0.getValue()).eq(YxCoupons::getIsShow, 1).eq(YxCoupons::getStoreId, storeId);
//        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("is_show",1).eq("store_id",storeId);
        wrapper.lambda().orderByAsc(YxCoupons::getSort).orderByDesc(YxCoupons::getCreateTime);
        List<YxCoupons> storeProductList = this.list(wrapper);
        List<YxCouponsQueryVo> queryVoList = yxCouponsMap.toDto(storeProductList);
        return queryVoList;
    }

    /**
     * 通过店铺ID获取店铺卡券
     *
     * @param id
     * @return
     */
    @Override
    public List<LocalLiveCouponsVo> getCouponsLitByBelog(int id) {
        QueryWrapper queryWrapper = new QueryWrapper<YxCoupons>().last("limit 3").eq("store_id", id).eq("del_flag", 0).eq("is_show", 1);
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_time");
        List<YxCoupons> yxCoupons = baseMapper.selectList(queryWrapper);
        List<LocalLiveCouponsVo> localLiveCouponsVoList = new ArrayList<>();
        for (YxCoupons coupons : yxCoupons) {
            LocalLiveCouponsVo localLiveCouponsVo = new LocalLiveCouponsVo();
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda()
                    .eq(YxImageInfo::getDelFlag, 0)
                    .eq(YxImageInfo::getTypeId, coupons.getId())
                    .eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS).eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC);
            YxImageInfo yxImageInfo = yxImageInfoMapper.selectOne(imageInfoQueryWrapper);

            BeanUtil.copyProperties(coupons, localLiveCouponsVo);
            if (yxImageInfo != null) {
                localLiveCouponsVo.setImg(yxImageInfo.getImgUrl());
            }
            localLiveCouponsVoList.add(localLiveCouponsVo);
        }
        return localLiveCouponsVoList;
    }

    @Override
    public YxCoupons getCouponsById(Integer id) {
        YxCoupons yxCoupons = yxCouponsMapper.selectById(id);
        if (ObjectUtil.isNull(yxCoupons)) {
            throw new ErrorRequestException("卡券不存在或已下架");
        }
        return yxCoupons;
    }

    @Override
    public Paging<YxCouponsQueryVo> getYxCouponsPageListByStoreId(YxCouponsQueryParam yxCouponsQueryParam) {
        Page page = setPageParam(yxCouponsQueryParam, OrderItem.desc("sort desc,create_time"));
        IPage<YxCouponsQueryVo> iPage = yxCouponsMapper.getYxCouponsPageListByStoreId(page, yxCouponsQueryParam.getStoreId());
        iPage.setTotal(yxCouponsMapper.getCountByStoreId(yxCouponsQueryParam.getStoreId()));
        return new Paging(iPage);
    }

    @Override
    public List<LocalLiveCouponsVo> getCouponsListByPram(Integer id, String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper<YxCoupons>().last("limit 3").eq("store_id", id).eq("del_flag", 0).eq("is_show", 1).ge("expire_date_end", LocalDateTime.now());
        // 输入查询文字的模糊查询卡券名称
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("coupon_name", keyword);
        }
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_time");
        List<YxCoupons> yxCoupons = baseMapper.selectList(queryWrapper);
        List<LocalLiveCouponsVo> localLiveCouponsVoList = new ArrayList<>();
        for (YxCoupons coupons : yxCoupons) {
            LocalLiveCouponsVo localLiveCouponsVo = new LocalLiveCouponsVo();
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda()
                    .eq(YxImageInfo::getDelFlag, 0)
                    .eq(YxImageInfo::getTypeId, coupons.getId())
                    .eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS).eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC);
            YxImageInfo yxImageInfo = yxImageInfoMapper.selectOne(imageInfoQueryWrapper);

            BeanUtil.copyProperties(coupons, localLiveCouponsVo);
            if (yxImageInfo != null) {
                localLiveCouponsVo.setImg(yxImageInfo.getImgUrl());
            }
            localLiveCouponsVoList.add(localLiveCouponsVo);
        }
        return localLiveCouponsVoList;
    }

    /**
     * 根据核销码查询卡券信息
     *
     * @param decodeVerifyCode
     * @return
     */
    @Override
    public YxCouponsDto getCouponByVerifyCode(String decodeVerifyCode, SystemUser user) {

        // 检查优惠券信息
        YxCouponsDto yxCouponsDto = null;
        YxCouponOrderDetail yxCouponOrderDetail = null;
        YxCouponOrder yxCouponOrder = null;
        YxCoupons yxCoupons = null ;
        yxCouponsDto = checkCouponInfo(decodeVerifyCode,user,yxCouponOrderDetail,yxCouponOrder,yxCoupons);
        if(yxCouponsDto.getStatus().intValue()==99){
            return yxCouponsDto;
        }

        // 组装返回参数
        yxCouponsDto = getYxCouponsDto(yxCouponOrderDetail, yxCouponOrder, yxCoupons);


        return yxCouponsDto;
    }

    // 检查票务信息
    private YxCouponsDto checkCouponInfo(String decodeVerifyCode, SystemUser user, YxCouponOrderDetail yxCouponOrderDetail, YxCouponOrder yxCouponOrder, YxCoupons yxCoupons) {
        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        yxCouponsDto.setStatus(99);
        String[] decode = decodeVerifyCode.split(",");
        if (decode.length != 2) {
            yxCouponsDto.setStatusDesc("无效核销码");
            return yxCouponsDto;
        }
        // 获取核销码
        String verifyCode = decode[0];
        // 获取核销用户的id
        String useUid = decode[1];
        yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        if (null == yxCouponOrderDetail) {
            yxCouponsDto.setStatusDesc("无效卡券");
            return yxCouponsDto;
        }
        yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", yxCouponOrderDetail.getOrderId()));
        if (null == yxCouponOrder) {
            yxCouponsDto.setStatusDesc("无卡券订单信息");
            return yxCouponsDto;
        }
        if (!useUid.equals(yxCouponOrder.getUid() + "")) {
            yxCouponsDto.setStatusDesc("卡券所属验证失败");
            return yxCouponsDto;
        }
        // 查询优惠券信息
        yxCoupons = this.getById(yxCouponOrderDetail.getCouponId());
        if (null == yxCoupons) {
            yxCouponsDto.setStatusDesc("卡券已失效");
            return yxCouponsDto;
        }
        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        if (expireDateStart.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("未到使用日期");
            return yxCouponsDto;
        }
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
        if (expireDateEnd.isBefore(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("卡券已过期");
            return yxCouponsDto;
        }


        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getStoreId().equals(user.getStoreId())) {
            yxCouponsDto.setStatusDesc("非本商户卡券");
            return yxCouponsDto;
        }
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            yxCouponsDto.setStatus(99);
            yxCouponsDto.setStatusDesc("当前卡券已达核销上限");
        }
        // 判断卡券状态
        switch (yxCouponOrderDetail.getStatus()) {
            case 0:
                yxCouponsDto.setStatusDesc("卡券未支付");
                break;
            case 1:
                yxCouponsDto.setStatusDesc("卡券已过期");
                break;
            case 2:
                yxCouponsDto.setStatusDesc("卡券待发放");
                break;
            case 3:
                yxCouponsDto.setStatusDesc("卡券支付失败");
                break;
            case 4:
                yxCouponsDto.setStatus(1);
                yxCouponsDto.setStatusDesc("待使用");
                break;
            case 5:
                yxCouponsDto.setStatus(1);
                yxCouponsDto.setStatusDesc("已使用");
                break;
            case 6:
                yxCouponsDto.setStatusDesc("卡券已核销");
                break;
            case 7:
                yxCouponsDto.setStatusDesc("卡券退款中");
                break;
            case 8:
                yxCouponsDto.setStatusDesc("卡券已退款");
                break;
            case 9:
                yxCouponsDto.setStatusDesc("卡券退款驳回");
                break;
            default:
                yxCouponsDto.setStatusDesc("未知状态");
                break;
        }

        yxCouponsDto.setStatus(1);
        return yxCouponsDto;
    }

    /**
     * 根据订单编号查询卡券信息
     *
     * @param orderId
     * @return
     */
    @Override
    public YxCouponsDto getCouponByOrderId(String orderId, SystemUser user) {
        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId, orderId));
        yxCouponsDto.setStatus(99);
        if (null == yxCouponOrder) {
            yxCouponsDto.setStatusDesc("无卡券订单信息");
            return yxCouponsDto;
        }

        // 查询优惠券信息
        YxCoupons yxCoupons = this.getById(yxCouponOrder.getCouponId());
        if (null == yxCoupons) {
            yxCouponsDto.setStatusDesc("卡券已失效");
            return yxCouponsDto;
        }
        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        if (expireDateStart.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("未到使用日期");
            return yxCouponsDto;
        }
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
        if (expireDateEnd.isBefore(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("卡券已过期");
            return yxCouponsDto;
        }
        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getStoreId().equals(user.getStoreId())) {
            yxCouponsDto.setStatusDesc("非本商户卡券");
            return yxCouponsDto;
        }
        // 可核销次数已核销次数
        if (yxCouponOrder.getUsedCount() >= yxCouponOrder.getUseCount()) {
            yxCouponsDto.setStatusDesc("当前订单已达核销上限");
        }

        List<YxCouponOrderDetail> list = this.yxCouponOrderDetailService.list(new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, orderId));
        YxCouponOrderDetail yxCouponOrderDetail = null;
        for (YxCouponOrderDetail item : list) {
            // 可核销次数已核销次数
            if (item.getUsedCount() >= item.getUseCount()) {
                continue;
            }
            // 卡券状态判断
            if (4 != item.getStatus() && 5 != item.getStatus()) {
                continue;
            }
            // 核销上限
            if (item.getUsedCount() >= item.getUseCount()) {
                continue;
            }
            yxCouponOrderDetail = item;
            break;
        }
        if (null == yxCouponOrderDetail) {
            yxCouponsDto.setStatusDesc("无效订单号");
            return yxCouponsDto;
        }
        // 组装返回参数
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        yxCouponsDto.setVerifyCode(Base64Utils.encode(yxCouponOrderDetail.getVerifyCode() + "," + yxCouponOrderDetail.getUid()));
        yxCouponsDto.setOrderId(yxCouponOrder.getOrderId());
        yxCouponsDto.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponsDto.setUseCount(yxCouponOrderDetail.getUseCount());
        yxCouponsDto.setBuyTime(DateUtils.timestampToStr10(yxCouponOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        yxCouponsDto.setAvailableTimeStr(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
        yxCouponsDto.setExpireDateStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd()));
        yxCouponsDto.setStatus(1);
        yxCouponsDto.setStatusDesc("可核销");
        return yxCouponsDto;
    }

    /**
     * 增加销量
     *
     * @param couponId
     * @param sales
     */
    @Override
    public void updateAddSales(Integer couponId, Integer sales) {
        yxCouponsMapper.updateAddSales(couponId,sales);
    }

    /**
     * 扣减销量
     *
     * @param couponId
     * @param sales
     */
    @Override
    public void updateMulSales(Integer couponId, Integer sales) {
        yxCouponsMapper.updateMulSales(couponId,sales);
    }

    /**
     * 扫码获取船票订单信息
     * @param decodeVerifyCode
     * @return
     */
    @Override
    public YxCouponsDto getShipCouponInfo(String decodeVerifyCode, SystemUser user) {
        // 检查优惠券信息
        YxCouponsDto yxCouponsDto = null;
        YxCouponOrderDetail yxCouponOrderDetail = null;
        YxCouponOrder yxCouponOrder = null;
        YxCoupons yxCoupons = null ;
        yxCouponsDto = checkCouponInfo(decodeVerifyCode,user,yxCouponOrderDetail,yxCouponOrder,yxCoupons);
        if(yxCouponsDto.getStatus().intValue()==99){
            return yxCouponsDto;
        }

        // 组装返回参数
        yxCouponsDto = getYxCouponsDto(yxCouponOrderDetail, yxCouponOrder, yxCoupons);

        // 查询船只系列
        YxShipSeries shipSeries = yxShipSeriesService.getById(yxCoupons.getSeriesId());
        if(shipSeries == null){
            yxCouponsDto.setStatus(99);
            yxCouponsDto.setStatusDesc("查询船只系列错误");
            return yxCouponsDto;
        }
        // 最多乘坐
        yxCouponsDto.setShipMaxUserCount(shipSeries.getRideLimit());
        // TODO: 2020/11/10
        // 订单人数
        yxCouponsDto.setShipUserCount(0);
        // 未成年人数人数
        yxCouponsDto.setShipUnderageCount(0);
        // 健康状况
        yxCouponsDto.setShipHealthStatus("");
        // 乘客
        yxCouponsDto.setShipPassenger(null);

        return yxCouponsDto;
    }

    private YxCouponsDto getYxCouponsDto(YxCouponOrderDetail yxCouponOrderDetail, YxCouponOrder yxCouponOrder, YxCoupons yxCoupons) {
        YxCouponsDto yxCouponsDto;
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        yxCouponsDto.setOrderId(yxCouponOrder.getOrderId());
        yxCouponsDto.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponsDto.setUseCount(yxCouponOrderDetail.getUseCount());
        yxCouponsDto.setBuyTime(DateUtils.timestampToStr10(yxCouponOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        yxCouponsDto.setAvailableTimeStr(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
        yxCouponsDto.setExpireDateStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd()));
        return yxCouponsDto;
    }

    /**
     * 获取卡券的价格配置（当前日期符合）
     * @param couponId
     * @return
     */
    @Override
    public YxCouponsPriceConfig getPirceConfig(int couponId) {
        QueryWrapper<YxCouponsPriceConfig> queryWrapper = new QueryWrapper<>();
        int nowDate = DateUtils.getNowTime();
        queryWrapper.lambda().eq(YxCouponsPriceConfig::getCouponId, couponId).eq(YxCouponsPriceConfig::getDelFlag, 0).ge(YxCouponsPriceConfig::getStartDate, nowDate).le(YxCouponsPriceConfig::getEndDate, nowDate);
        List<YxCouponsPriceConfig> couponsPriceConfigList = yxCouponsPriceConfigService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(couponsPriceConfigList)) {
            return couponsPriceConfigList.get(0);
        }
        return null;
    }
}
