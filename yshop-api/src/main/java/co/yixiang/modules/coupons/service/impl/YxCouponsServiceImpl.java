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
import co.yixiang.modules.couponUse.dto.YxShipPassengerVO;
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
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.shop.mapping.YxCouponsMap;
import co.yixiang.utils.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class YxCouponsServiceImpl extends BaseServiceImpl<YxCouponsMapper, YxCoupons> implements YxCouponsService {

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
    @Autowired
    private YxShipPassengerService yxShipPassengerService;


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
        YxCouponsDto yxCouponsDto = new YxCouponsDto();

        Map<String, Object> checkResult = checkCouponInfo(decodeVerifyCode, user);
        yxCouponsDto = (YxCouponsDto) checkResult.get("yxCouponsDto");

        if (yxCouponsDto.getStatus().intValue() == 99) {
            return yxCouponsDto;
        }

        YxCouponOrderDetail yxCouponOrderDetail = (YxCouponOrderDetail) checkResult.get("yxCouponOrderDetail");
        YxCouponOrder yxCouponOrder = (YxCouponOrder) checkResult.get("yxCouponOrder");
        YxCoupons yxCoupons = (YxCoupons) checkResult.get("yxCoupons");

        // 组装返回参数
        yxCouponsDto = getYxCouponsDto(yxCouponOrderDetail, yxCouponOrder, yxCoupons);


        return yxCouponsDto;
    }

    // 检查票务信息
    private Map<String, Object> checkCouponInfo(String decodeVerifyCode, SystemUser user) {
        Map<String, Object> result = new HashMap<>();

        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        result.put("yxCouponsDto", yxCouponsDto);
        yxCouponsDto.setStatus(99);
        String[] decode = decodeVerifyCode.split(",");
        if (decode.length != 2) {
            yxCouponsDto.setStatusDesc("无效核销码");
            return result;
        }
        // 获取核销码
        String verifyCode = decode[0];
        // 获取核销用户的id
        String useUid = decode[1];
        YxCouponOrderDetail yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        result.put("yxCouponOrderDetail", yxCouponOrderDetail);

        if (null == yxCouponOrderDetail) {
            yxCouponsDto.setStatusDesc("无效卡券");
            return result;
        }
        YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", yxCouponOrderDetail.getOrderId()));
        result.put("yxCouponOrder", yxCouponOrder);
        if (null == yxCouponOrder) {
            yxCouponsDto.setStatusDesc("无卡券订单信息");
            return result;
        }
        if (!useUid.equals(yxCouponOrder.getUid() + "")) {
            yxCouponsDto.setStatusDesc("卡券所属验证失败");
            return result;
        }
        // 查询优惠券信息
        YxCoupons yxCoupons = this.getById(yxCouponOrderDetail.getCouponId());
        result.put("yxCoupons", yxCoupons);
        if (null == yxCoupons) {
            yxCouponsDto.setStatusDesc("卡券已失效");
            return result;
        }
        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        if (expireDateStart.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("未到使用日期");
            return result;
        }
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
        if (expireDateEnd.isBefore(LocalDateTime.now())) {
            yxCouponsDto.setStatusDesc("卡券已过期");
            return result;
        }


        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getStoreId().equals(user.getStoreId())) {
            yxCouponsDto.setStatusDesc("非本商户卡券");
            return result;
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
        return result;
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
        yxCouponsMapper.updateAddSales(couponId, sales);
    }

    /**
     * 扣减销量
     *
     * @param couponId
     * @param sales
     */
    @Override
    public void updateMulSales(Integer couponId, Integer sales) {
        yxCouponsMapper.updateMulSales(couponId, sales);
    }

    /**
     * 扫码获取船票订单信息
     *
     * @param decodeVerifyCode
     * @return
     */
    @Override
    public YxCouponsDto getShipCouponInfo(String decodeVerifyCode, SystemUser user) {
        // 检查优惠券信息
        YxCouponsDto yxCouponsDto = new YxCouponsDto();

        Map<String, Object> checkResult = checkCouponInfo(decodeVerifyCode, user);
        yxCouponsDto = (YxCouponsDto) checkResult.get("yxCouponsDto");

        if (yxCouponsDto.getStatus().intValue() == 99) {
            return yxCouponsDto;
        }

        YxCouponOrderDetail yxCouponOrderDetail = (YxCouponOrderDetail) checkResult.get("yxCouponOrderDetail");
        YxCouponOrder yxCouponOrder = (YxCouponOrder) checkResult.get("yxCouponOrder");
        YxCoupons yxCoupons = (YxCoupons) checkResult.get("yxCoupons");

        // 组装返回参数
        yxCouponsDto = getYxCouponsDto(yxCouponOrderDetail, yxCouponOrder, yxCoupons);

        // 查询船只系列
        YxShipSeries shipSeries = yxShipSeriesService.getById(yxCoupons.getSeriesId());
        if (shipSeries == null) {
            yxCouponsDto.setStatus(99);
            yxCouponsDto.setStatusDesc("查询船只系列错误");
            return yxCouponsDto;
        }
        // 最多乘坐
        yxCouponsDto.setShipMaxUserCount(shipSeries.getRideLimit());
        List<YxShipPassenger> couponUsers = yxShipPassengerService.list(new QueryWrapper<YxShipPassenger>().eq("coupon_order_id", yxCouponOrder.getId()));
        List<YxShipPassengerVO> users = new ArrayList<>();
        if (couponUsers == null || couponUsers.size() == 0) {
            yxCouponsDto.setStatus(99);
            yxCouponsDto.setStatusDesc("未选择乘坐人");
            return yxCouponsDto;
        }
        int underageCount = 0;
        int oldCount = 0;
        for (YxShipPassenger item : couponUsers) {
            if (item.getIsAdult().intValue() == 0) {
                underageCount++;
            } else if (item.getIsAdult().intValue() == 2) {
                oldCount++;
            }
            YxShipPassengerVO resultVo = CommonsUtils.convertBean(item, YxShipPassengerVO.class);
            resultVo.setIdCard(CardNumUtil.idEncrypt(item.getIdCard()));
            resultVo.setPhone(CardNumUtil.mobileEncrypt(item.getPhone()));
            resultVo.setPassengerName(CardNumUtil.nameEncrypt(item.getPassengerName()));
            // 0:未成年 1:成年人 2：老年人
            resultVo.setAgeArea("");
            if (resultVo.getIsAdult().intValue() == 0) {
                resultVo.setAgeArea("未成年");
            } else if (resultVo.getIsAdult().intValue() == 2) {
                resultVo.setAgeArea("老年人");
            }
            users.add(resultVo);
        }
        // 订单人数
        yxCouponsDto.setShipUserCount(couponUsers.size());
        // 未成年人数
        yxCouponsDto.setShipUnderageCount(underageCount);
        // 老年人人数
        yxCouponsDto.setOldCount(oldCount);
        // 健康状况
        yxCouponsDto.setShipHealthStatus("");
        // 乘客
        yxCouponsDto.setShipPassenger(users);
        // 系列ID
        yxCouponsDto.setSeriesId(yxCoupons.getSeriesId());
        // 船只ID
        yxCouponsDto.setShipId(yxCoupons.getShipId());
        yxCouponsDto.setStatus(1);
        return yxCouponsDto;
    }

    private YxCouponsDto getYxCouponsDto(YxCouponOrderDetail yxCouponOrderDetail, YxCouponOrder yxCouponOrder, YxCoupons yxCoupons) {
        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        yxCouponsDto.setOrderId(yxCouponOrder.getOrderId());
        yxCouponsDto.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponsDto.setUseCount(yxCouponOrderDetail.getUseCount());
        yxCouponsDto.setBuyTime(DateUtils.timestampToStr10(yxCouponOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        yxCouponsDto.setAvailableTimeStr(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
        yxCouponsDto.setExpireDateStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd()));
        yxCouponsDto.setStatus(1);
        return yxCouponsDto;
    }

    /**
     * 获取卡券的价格配置（当前日期符合）
     *
     * @param couponId
     * @return
     */
    @Override
    public YxCouponsPriceConfig getPirceConfig(int couponId) {
        QueryWrapper<YxCouponsPriceConfig> queryWrapper = new QueryWrapper<>();
        int nowDate = Integer.parseInt(DateUtils.getDateMMDD());
        queryWrapper.lambda()
                .eq(YxCouponsPriceConfig::getCouponId, couponId)
                .eq(YxCouponsPriceConfig::getDelFlag, 0)
                .le(YxCouponsPriceConfig::getStartDate, nowDate)
                .ge(YxCouponsPriceConfig::getEndDate, nowDate).orderByDesc(YxCouponsPriceConfig::getId);
        List<YxCouponsPriceConfig> couponsPriceConfigList = yxCouponsPriceConfigService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(couponsPriceConfigList)) {
            return couponsPriceConfigList.get(0);
        }
        return null;
    }
}
