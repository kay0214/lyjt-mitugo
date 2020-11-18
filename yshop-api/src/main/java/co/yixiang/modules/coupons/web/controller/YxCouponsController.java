package co.yixiang.modules.coupons.web.controller;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.commission.service.YxCustomizeRateService;
import co.yixiang.modules.contract.service.YxContractTemplateService;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.service.YxCouponsReplyService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.StringUtils;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 本地生活, 卡券表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCoupons")
@Api(value = "本地生活, 卡券表 API", tags = "本地生活:卡券")
public class YxCouponsController extends BaseController {

    @Autowired
    private YxCouponsService yxCouponsService;

    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxCommissionRateService commissionRateService;
    @Autowired
    private YxShipInfoService yxShipInfoService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;
    @Autowired
    private YxContractTemplateService yxContractTemplateService;
    @Autowired
    private YxCouponsReplyService yxCouponsReplyService;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private IGenerator generator;

    /**
     * 获取本地生活, 卡券详情
     */
    @AnonymousAccess
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCoupons对象详情", notes = "查看本地生活, 卡券表", response = YxCouponsQueryVo.class)
    public ApiResult<YxCouponsQueryVo> getYxCoupons(@Valid @RequestBody IdParam idParam) throws Exception {
        YxCouponsQueryVo yxCouponsQueryVo = yxCouponsService.getYxCouponsById(idParam.getId());

        if (null == yxCouponsQueryVo) {
            throw new BadRequestException("未查询到卡券详情");
        }
        // 总销量
        yxCouponsQueryVo.setTotalSales(yxCouponsQueryVo.getSales() + yxCouponsQueryVo.getFicti());
        // 卡券缩略图
        YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", idParam.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
        if (thumbnail != null) {
            yxCouponsQueryVo.setImage(thumbnail.getImgUrl());
        }
        // 轮播图
        List<YxImageInfo> sliderImg = yxImageInfoService.list(new QueryWrapper<YxImageInfo>().eq("type_id", idParam.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_ROTATION1).eq("del_flag", 0));
        List<String> sliderImages = new ArrayList<>();
        if (sliderImg.size() > 0) {
            for (YxImageInfo slider : sliderImg) {
                String imgPath = slider.getImgUrl();
                sliderImages.add(imgPath);
            }
            yxCouponsQueryVo.setSliderImage(sliderImages);
        }
        // 卡券小视频
        YxImageInfo video = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().lambda()
                .eq(YxImageInfo::getTypeId, idParam.getId())
                .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_CARD)
                .eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_VIDEO)
                .eq(YxImageInfo::getDelFlag, 0));
        if (null != video) {
            yxCouponsQueryVo.setVideo(video.getImgUrl());
        }
        // 处理评价
        List<YxCouponsReply> replyList = this.yxCouponsReplyService.list(new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, idParam.getId())
                .eq(YxCouponsReply::getDelFlag, 0)
                .orderByDesc(YxCouponsReply::getAddTime));
        if (null != replyList && replyList.size() > 0) {
            // 处理订单评价并赋值
            yxCouponsQueryVo.setReply(this.yxCouponsReplyService.convertCouponsReply(replyList.get(0)));
            // 评价总数
            yxCouponsQueryVo.setReplyCount(replyList.size());
            // 处理好评率
            Integer goodCount = this.yxCouponsReplyService.count(new QueryWrapper<YxCouponsReply>().lambda()
                    .eq(YxCouponsReply::getCouponId, idParam.getId())
                    .eq(YxCouponsReply::getDelFlag, 0)
                    .gt(YxCouponsReply::getGeneralScore, 3));
            yxCouponsQueryVo.setGoodRate(new BigDecimal(goodCount).multiply(new BigDecimal(100)).divide(new BigDecimal(replyList.size()), 2, BigDecimal.ROUND_HALF_UP) + "%");
        }

        // 拼接有效期
        String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCouponsQueryVo.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, yxCouponsQueryVo.getExpireDateEnd());
        yxCouponsQueryVo.setExpireDate(expireDate);
        yxCouponsQueryVo.setAvailableTime(yxCouponsQueryVo.getAvailableTimeStart() + " ~ " + yxCouponsQueryVo.getAvailableTimeEnd());
        // 获取价格配置
        YxCouponsPriceConfig yxCouponsPriceConfig = yxCouponsService.getPirceConfig(yxCouponsQueryVo.getId());
        if (null != yxCouponsPriceConfig) {
            //佣金
            yxCouponsQueryVo.setCommission(yxCouponsPriceConfig.getCommission());
            //销售价格
            yxCouponsQueryVo.setSellingPrice(yxCouponsPriceConfig.getSellingPrice());
        }
        // 佣金按比例计算
        // 分佣类型 0：按平台，1：不分佣，2：自定义分佣
        int customType = yxCouponsQueryVo.getCustomizeType();
        BigDecimal bigCommission = new BigDecimal(0);
        switch (customType) {
            case 0:
                //0：按平台
                YxCommissionRate commissionRate = commissionRateService.getOne(new QueryWrapper<YxCommissionRate>().eq("del_flag", 0));
                bigCommission = yxCouponsQueryVo.getCommission();

                if (ObjectUtil.isNotNull(commissionRate)) {
                    //佣金= 佣金*分享
                    bigCommission = bigCommission.multiply(commissionRate.getShareRate());
                }
                break;
            case 1:
                //1：不分佣
                bigCommission = new BigDecimal(0);
                break;
            case 2:
                //2：自定义分佣
                YxCustomizeRate yxCustomizeRate = yxCustomizeRateService.getCustomizeRateByParam(0, yxCouponsQueryVo.getId());
                bigCommission = yxCouponsQueryVo.getCommission();

                if (ObjectUtil.isNotNull(yxCustomizeRate)) {
                    //佣金= 佣金*分享
                    bigCommission = bigCommission.multiply(yxCustomizeRate.getShareRate());
                }
                break;
        }
        yxCouponsQueryVo.setCommission(bigCommission);

        if (StringUtils.isNotBlank(yxCouponsQueryVo.getConfirmation())) {
            List<String> stringList = Arrays.asList(yxCouponsQueryVo.getConfirmation().split(","));
            yxCouponsQueryVo.setConfirmationList(stringList);
        }

        //船只信息
        if (4 == yxCouponsQueryVo.getCouponType()) {
            //船票券
            YxShipInfoQueryVo shipInfoQueryVo = yxShipInfoService.getYxShipInfoById(yxCouponsQueryVo.getShipId());
            yxCouponsQueryVo.setShipName(shipInfoQueryVo.getShipName());
            //船只系列信息
            yxCouponsQueryVo.setShipSeries(yxShipSeriesService.getYxShipSeriesById(yxCouponsQueryVo.getSeriesId()));
            //合同信息
            YxContractTemplateQueryVo templateQueryVo = null;
            if (0 != yxCouponsQueryVo.getTempId()) {
                templateQueryVo = yxContractTemplateService.getYxContractTemplateById(yxCouponsQueryVo.getTempId());
            }
            yxCouponsQueryVo.setContractTemplateQueryVo(templateQueryVo);
        }
        return ApiResult.ok(yxCouponsQueryVo);
    }

    /**
     * 本地生活, 卡券表分页列表
     */
    @AnonymousAccess
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCoupons分页列表", notes = "本地生活, 卡券表分页列表", response = YxCouponsQueryVo.class)
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        Paging<YxCouponsQueryVo> paging = yxCouponsService.getYxCouponsPageList(yxCouponsQueryParam);
        if (paging.getRecords() != null && paging.getRecords().size() > 0) {
            for (YxCouponsQueryVo item : paging.getRecords()) {
                // 卡券缩略图
                YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                        .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
                if (thumbnail != null) {
                    item.setImage(thumbnail.getImgUrl());
                }
                // 拼接有效期
                String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateEnd());
                item.setExpireDate(expireDate);
                item.setAvailableTime(item.getAvailableTimeStart() + " ~ " + item.getAvailableTimeEnd());
            }
        }
        return ApiResult.ok(paging);
    }

    /**
     * 本地生活卡券热销榜单
     *
     * @param yxCouponsQueryParam
     * @return
     * @throws Exception
     */
    @AnonymousAccess
    @PostMapping("/getCouponsHotList")
    @Cached(name = "cachedCouponsHotList-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    @ApiOperation(value = "本地生活卡券,热销榜单", notes = "本地生活卡券,热销榜单")
    public ApiResult<Paging<YxCouponsQueryVo>> getCouponsHotList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        Paging<YxCouponsQueryVo> paging = yxCouponsService.getCouponsHotList(yxCouponsQueryParam);
        for (YxCouponsQueryVo item : paging.getRecords()) {
            // 卡券缩略图
            YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                    .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
            if (thumbnail != null) {
                item.setImage(thumbnail.getImgUrl());
            }
            // 已售件数增加虚拟销量
            item.setTotalSales(item.getSales() + item.getFicti());
            // 拼接有效期
            String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateEnd());
            item.setExpireDate(expireDate);
            item.setAvailableTime(item.getAvailableTimeStart() + " ~ " + item.getAvailableTimeEnd());
        }
        return ApiResult.ok(paging);
    }

}

