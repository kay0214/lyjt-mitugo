package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.CommonEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.couponUse.dto.YxCouponsDto;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveCouponsVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.shop.mapping.YxCouponsMap;
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
    private final IGenerator generator;

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
    public List<YxCouponsQueryVo> getCouponsHotList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        List<YxCouponsQueryVo> couponsHotList = yxCouponsMapper.getCouponsHotList(yxCouponsQueryParam);
        return couponsHotList;
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
        QueryWrapper queryWrapper = new QueryWrapper<YxCoupons>().last("limit 3").eq("store_id", id).eq("del_flag", 0).eq("is_show", 1);
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
     * @param uid
     * @return
     */
    @Override
    public YxCouponsDto getCouponByVerifyCode(String decodeVerifyCode, int uid) {
        String[] decode = decodeVerifyCode.split(",");
        if (decode.length != 2) {
            throw new BadRequestException("无效核销码");
        }
        // 获取核销码
        String verifyCode = decode[0];
        // 获取核销用户的id
        String useUid = decode[1];
        YxCouponsDto yxCouponsDto = new YxCouponsDto();
        YxCouponOrderDetail yxCouponOrderDetail = this.yxCouponOrderDetailService.getOne(new QueryWrapper<YxCouponOrderDetail>().eq("verify_code", verifyCode));
        if (null == yxCouponOrderDetail) {
            yxCouponsDto.setStatus(-1);
            yxCouponsDto.setStatusDesc("无效卡券");
            return yxCouponsDto;
        }
        YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", yxCouponOrderDetail.getOrderId()));
        if (null == yxCouponOrder) {
            throw new BadRequestException("查询卡券订单失败");
        }
        if (!useUid.equals(yxCouponOrder.getUid() + "")) {
            throw new BadRequestException("核销码与用户信息不匹配");
        }
        // 查询优惠券信息
        YxCoupons yxCoupons = this.getById(yxCouponOrderDetail.getCouponId());
        if (null == yxCoupons) {
            yxCouponsDto.setStatus(-2);
            yxCouponsDto.setStatusDesc("卡券已失效");
            return yxCouponsDto;
        }
        // 组装返回参数
        yxCouponsDto = generator.convert(yxCoupons, YxCouponsDto.class);
        yxCouponsDto.setOrderId(yxCouponOrder.getOrderId());
        yxCouponsDto.setUsedCount(yxCouponOrderDetail.getUsedCount());
        yxCouponsDto.setUseCount(yxCouponOrderDetail.getUseCount());
        yxCouponsDto.setBuyTime(DateUtils.timestampToStr10(yxCouponOrder.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        yxCouponsDto.setAvailableTimeStr(yxCoupons.getAvailableTimeStart() + " ~ " + yxCoupons.getAvailableTimeEnd());
        yxCouponsDto.setExpireDateStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCoupons.getExpireDateEnd()));

        // 判断是否本商铺发放的卡券
        if (!yxCoupons.getCreateUserId().equals(uid)) {
            yxCouponsDto.setStatus(-3);
            yxCouponsDto.setStatusDesc("非本商户卡券");
            return yxCouponsDto;
        }
        // 可核销次数已核销次数
        if (yxCouponOrderDetail.getUsedCount() >= yxCouponOrderDetail.getUseCount()) {
            yxCouponsDto.setStatus(-4);
            yxCouponsDto.setStatusDesc("当前卡券已达核销上限");
        }
        // 判断卡券状态
        switch (yxCouponOrderDetail.getStatus()) {
            case 0:
                yxCouponsDto.setStatus(-5);
                yxCouponsDto.setStatusDesc("待支付");
                break;
            case 1:
                yxCouponsDto.setStatus(-6);
                yxCouponsDto.setStatusDesc("已过期");
                break;
            case 2:
                yxCouponsDto.setStatus(-7);
                yxCouponsDto.setStatusDesc("待发放");
                break;
            case 3:
                yxCouponsDto.setStatus(-8);
                yxCouponsDto.setStatusDesc("支付失败");
                break;
            case 4:
                yxCouponsDto.setStatus(4);
                yxCouponsDto.setStatusDesc("待使用");
                break;
            case 5:
                yxCouponsDto.setStatus(5);
                yxCouponsDto.setStatusDesc("已使用");
                break;
            case 6:
                yxCouponsDto.setStatus(-9);
                yxCouponsDto.setStatusDesc("已核销");
                break;
            case 7:
                yxCouponsDto.setStatus(-10);
                yxCouponsDto.setStatusDesc("退款中");
                break;
            case 8:
                yxCouponsDto.setStatus(-11);
                yxCouponsDto.setStatusDesc("已退款");
                break;
            case 9:
                yxCouponsDto.setStatus(-12);
                yxCouponsDto.setStatusDesc("退款驳回");
                break;
            default:
                yxCouponsDto.setStatus(-13);
                yxCouponsDto.setStatusDesc("未知状态");
                break;
        }
        // 判断有效期
        LocalDateTime expireDateStart = DateUtils.dateToLocalDate(yxCoupons.getExpireDateStart());
        LocalDateTime expireDateEnd = DateUtils.dateToLocalDate(yxCoupons.getExpireDateEnd());
        if (expireDateStart.isBefore(LocalDateTime.now()) || expireDateEnd.isAfter(LocalDateTime.now())) {
            yxCouponsDto.setStatus(-14);
            yxCouponsDto.setStatusDesc("已失效");
            return yxCouponsDto;
        }
        return yxCouponsDto;
    }
}
