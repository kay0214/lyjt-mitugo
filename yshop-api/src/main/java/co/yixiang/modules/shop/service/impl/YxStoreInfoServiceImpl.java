package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.DistanceMeterUtil;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.CommonEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.LocalLiveQueryParam;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveCouponsVo;
import co.yixiang.modules.coupons.web.vo.LocalLiveListVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.service.DictDetailService;
import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueMapper;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.mapping.YxStoreInfoMap;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoDetailQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.tools.service.QiniuContentService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreInfoServiceImpl extends BaseServiceImpl<YxStoreInfoMapper, YxStoreInfo> implements YxStoreInfoService {

    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxStoreInfoMap yxStoreInfoMap;
    @Autowired
    private DictDetailService dictDetailService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxStoreAttributeService yxStoreAttributeService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxStoreProductService yxStoreProductService;

    @Autowired
    private YxImageInfoMapper yxImageInfoMapper;
    @Autowired
    private YxStoreCouponIssueMapper couponIssueMapper;


    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxSystemAttachmentService systemAttachmentService;
    @Autowired
    private CreatShareStoreService creatShareStoreService;
    @Autowired
    private YxUserService yxUserService;

    @Autowired
    QiniuContentService qiniuContentService;
    @Value("${file.path}")
    private String path;

    @Value("${file.localUrl}")
    private String localUrl;



    @Override
    public YxStoreInfoQueryVo getYxStoreInfoById(Serializable id) {
        return yxStoreInfoMapper.getYxStoreInfoById(id);
    }

    @Override
    public Paging<YxStoreInfoQueryVo> getYxStoreInfoPageList(YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception {
        Page page = setPageParam(yxStoreInfoQueryParam, OrderItem.desc("create_time"));
        IPage<YxStoreInfoQueryVo> iPage = yxStoreInfoMapper.getYxStoreInfoPageList(page, yxStoreInfoQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据参数获取店铺信息
     *
     * @param yxStoreInfoQueryParam
     * @return
     */
    @Override
    public IPage<YxStoreInfoQueryVo> getStoreInfoList(YxStoreInfoQueryParam yxStoreInfoQueryParam) {
        /*if(StringUtils.isBlank(yxStoreInfoQueryParam.getSalesOrder())&&StringUtils.isBlank(yxStoreInfoQueryParam.getScoreOrder())){
            //默认
            QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
            wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status",0);
            if(StringUtils.isNotBlank(yxStoreInfoQueryParam.getStoreName())){
                wrapper.like("store_name", yxStoreInfoQueryParam.getStoreName());
            }
            wrapper.orderByDesc("create_time");
            Page<YxStoreInfo> pageModel = new Page<YxStoreInfo>(yxStoreInfoQueryParam.getPage(),
                    yxStoreInfoQueryParam.getLimit());
            IPage<YxStoreInfo> pageList = yxStoreInfoMapper.selectPage(pageModel,wrapper);
            list= yxStoreInfoMap.toDto(pageList.getRecords());
        }
        if(ObjectUtil.isNotNull(yxStoreInfoQueryParam.getSalesOrder())&&StringUtils.isNotBlank(yxStoreInfoQueryParam.getSalesOrder())){
            //根据销量排序
            list= yxStoreInfoMapper.selectInfoListBySales(yxStoreInfoQueryParam.getSalesOrder(),yxStoreInfoQueryParam.getStoreName());
        }
        if(ObjectUtil.isNotNull(yxStoreInfoQueryParam.getScoreOrder())&&StringUtils.isNotBlank(yxStoreInfoQueryParam.getScoreOrder())){
            //根据评分排序
            list = yxStoreInfoMapper.selectInfoListBySocre(yxStoreInfoQueryParam.getScoreOrder(),yxStoreInfoQueryParam.getStoreName());
        }*/
        IPage<YxStoreInfoQueryVo> list = yxStoreInfoMapper.selectStoreInfoVoList(new Page<>(yxStoreInfoQueryParam.getPage(), yxStoreInfoQueryParam.getLimit()), yxStoreInfoQueryParam);
        if (!CollectionUtils.isEmpty(list.getRecords())) {
            for (YxStoreInfoQueryVo yxStoreInfoQueryVo : list.getRecords()) {
                //行业类别
                DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_INDUSTRY_CATEGORY, yxStoreInfoQueryVo.getIndustryCategory());
                if (null != dictDetail) {
                    yxStoreInfoQueryVo.setIndustryCategoryInfo(dictDetail.getLabel());
                }
                //店铺缩略图
                yxStoreInfoQueryVo.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC));
//                yxStoreInfoQueryVo.setSalesCount();
            }
        }
        return list;
    }

    /**
     * 显示店铺详情
     *
     * @param id
     * @return
     */
    @Override
    public YxStoreInfoDetailQueryVo getStoreDetailInfoById(Integer id) {
        YxStoreInfoDetailQueryVo yxStoreInfoDetailQueryVo = new YxStoreInfoDetailQueryVo();
        QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status", 0).eq("id", id);
        YxStoreInfo yxStoreInfo = this.getOne(wrapper);
        YxStoreInfoQueryVo yxStoreInfoQueryVo = yxStoreInfoMap.toDto(yxStoreInfo);
        //行业类别
        if (ObjectUtil.isNull(yxStoreInfo)) {
            throw new ErrorRequestException("店铺信息为空！");
        }
        DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_INDUSTRY_CATEGORY, yxStoreInfoQueryVo.getIndustryCategory());
        if (null != dictDetail) {
            yxStoreInfoQueryVo.setIndustryCategoryInfo(dictDetail.getLabel());
        }
        //店铺缩略图
        yxStoreInfoQueryVo.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC));
        //轮播图
        yxStoreInfoQueryVo.setStoreRotationImages(yxImageInfoService.selectImgByParamList(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_ROTATION1));
        //店铺服务
        List<String> listService = getStroeAttribute(yxStoreInfo.getId(), 1);
        yxStoreInfoQueryVo.setStoreServiceList(listService);
        //店铺营业时间
        List<String> listOpenTiems = getStroeAttribute(yxStoreInfo.getId(), 0);
        yxStoreInfoQueryVo.setOpenTimes(listOpenTiems);
        yxStoreInfoDetailQueryVo.setSotreInfo(yxStoreInfoQueryVo);
        //卡券信息
        /*List<YxCouponsQueryVo> lsitCoupons = yxCouponsService.getCouponsInfoByStoreId(yxStoreInfo.getId());
        yxStoreInfoDetailQueryVo.setCouponsListInfo(getCouponsImg(lsitCoupons));*/
      /*  //商品信息
        yxStoreInfoDetailQueryVo.setProductListInfo(yxStoreProductService.getProductListByStoreId(yxStoreInfo.getId()));*/
        //是否有可用优惠券
        yxStoreInfoDetailQueryVo.setCouponUse(1);
        Page<YxStoreCouponIssue> pageModel = new Page<>(1, 10);
        List<YxStoreCouponIssueQueryVo> couponIssueQueryList = couponIssueMapper
                .selectCouponListByStoreId(pageModel, id);
        if (CollectionUtils.isEmpty(couponIssueQueryList)) {
            yxStoreInfoDetailQueryVo.setCouponUse(0);
        }
        return yxStoreInfoDetailQueryVo;
    }

    public List<String> getStroeAttribute(int sotreId, int attributeType) {
        List<String> listStr = new ArrayList<String>();
        QueryWrapper<YxStoreAttribute> wrapper = new QueryWrapper<YxStoreAttribute>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("store_id", sotreId).eq("attribute_type", attributeType);
        List<YxStoreAttribute> yxStoreAttributeList = yxStoreAttributeService.list(wrapper);
        if (CollectionUtils.isEmpty(yxStoreAttributeList)) {
            return null;
        }
        if (1 == attributeType) {
            //店铺服务
            for (YxStoreAttribute yxStoreAttribute : yxStoreAttributeList) {
                //行业类别
                DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_STORE_SERVICE, Integer.parseInt(yxStoreAttribute.getAttributeValue1()));
                if (null != dictDetail) {
                    listStr.add(dictDetail.getLabel());
                }
            }
        } else {
            for (YxStoreAttribute yxStoreAttribute : yxStoreAttributeList) {
                listStr.add(yxStoreAttribute.getAttributeValue1() + " " + yxStoreAttribute.getAttributeValue2());
            }
        }
        return listStr;
    }

    /**
     * @param localLiveQueryParam
     * @return
     * @throws Exception
     */
    @Override
    public Paging<LocalLiveListVo> getLocalLiveList(LocalLiveQueryParam localLiveQueryParam, String location) throws Exception {
        Page page = setPageParam(localLiveQueryParam, OrderItem.desc("create_time"));
        if (StringUtils.isNotBlank(location) && location.split(",").length == 2) {
            String[] locationArr = location.split(",");
            localLiveQueryParam.setLat(locationArr[1]);
            localLiveQueryParam.setLnt(locationArr[0]);
        }

        IPage<LocalLiveListVo> iPage = yxStoreInfoMapper.getLocalLiveList(page, localLiveQueryParam);
        List<LocalLiveListVo> localLiveListVoList = iPage.getRecords();
        for (LocalLiveListVo localLiveListVo : localLiveListVoList) {
            if (StringUtils.isNotBlank(location) && location.split(",").length == 2 && StringUtils.isNotBlank(localLiveListVo.getCoordinateY()) && StringUtils.isNotBlank(localLiveListVo.getCoordinateX())) {
                // 设置距离
                // 维度  京都
                String[] locationArr = location.split(",");
                GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(localLiveListVo.getCoordinateY()), Double.parseDouble(localLiveListVo.getCoordinateX()));
                GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
                double distance = DistanceMeterUtil.getDistanceMeter(source, target);
                localLiveListVo.setDistance(new BigDecimal(distance).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).toString() + "km");
            } else {
                localLiveListVo.setDistance("");
            }
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda().eq(YxImageInfo::getTypeId, localLiveListVo.getId())
                    .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_STORE).eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC);
            YxImageInfo yxImageInfo = yxImageInfoMapper.selectOne(imageInfoQueryWrapper);
            if (yxImageInfo != null) {
                localLiveListVo.setImg(yxImageInfo.getImgUrl());
            }
//            List<LocalLiveCouponsVo> localLiveCouponsVoList = yxCouponsService.getCouponsLitByBelog(localLiveListVo.getId());
            List<LocalLiveCouponsVo> localLiveCouponsVoList = yxCouponsService.getCouponsListByPram(localLiveListVo.getId(), localLiveQueryParam.getKeyword());

            localLiveListVo.setLocalLiveCouponsVoList(localLiveCouponsVoList);

        }
        return new Paging(iPage);
    }


    /**
     * 本地生活, 卡券表分页列表
     */
    @Override
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageListByStoreId(YxCouponsQueryParam yxCouponsQueryParam) {
        try {
            Paging<YxCouponsQueryVo> paging = yxCouponsService.getYxCouponsPageListByStoreId(yxCouponsQueryParam);
            if (paging.getRecords() != null && paging.getRecords().size() > 0) {
                for (YxCouponsQueryVo item : paging.getRecords()) {
                    // 卡券缩略图
                    YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                            .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
                    if (thumbnail != null) {
                        item.setImage(thumbnail.getImgUrl());
                    }
                    // 已售销量增加虚拟销量
                    item.setTotalSales(item.getSales() + item.getFicti());
                    // 拼接有效期
                    String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateEnd());
                    item.setExpireDate(expireDate);
                    item.setAvailableTime(item.getAvailableTimeStart() + " ~ " + item.getAvailableTimeEnd());
                }
            }
            return ApiResult.ok(paging);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Nid查询店铺信息
     *
     * @param storeNid
     * @return
     */
    @Override
    public YxStoreInfoQueryVo getYxStoreInfoByNid(String storeNid) {
        YxStoreInfoDetailQueryVo yxStoreInfoDetailQueryVo = new YxStoreInfoDetailQueryVo();
        QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status", 0).eq("store_nid", storeNid);
        YxStoreInfo yxStoreInfo = this.getOne(wrapper);
        YxStoreInfoQueryVo yxStoreInfoQueryVo = yxStoreInfoMap.toDto(yxStoreInfo);

        if (ObjectUtil.isNull(yxStoreInfo)) {
            throw new ErrorRequestException("店铺信息为空！");
        }

        //店铺缩略图
        yxStoreInfoQueryVo.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC));

        return yxStoreInfoQueryVo;
    }

    @Override
    public ApiResult productPoster(Integer id) throws IOException, FontFormatException {
        int uid = SecurityUtils.getUserId().intValue();
        // 海报
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }
        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(name);
        String fileDir = path + "qrcode" + File.separator;
        String qrcodeUrl = "";
        BufferedImage qrcode;
        QrConfig config = new QrConfig(122, 122);
        config.setMargin(0);
        //如果类型是小程序
        if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            //小程序地址
            siteUrl = siteUrl + "/shop/";
            //生成二维码
            qrcode = QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.ROUNTINE.getValue(), config);
        } else if (userType.equals(AppFromEnum.APP.getValue())) {
            //h5地址
            siteUrl = siteUrl + "/shop/";
            //生成二维码
            qrcode = QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), config);
        } else {//如果类型是h5
            //生成二维码
            qrcode = QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, config);
        }
        String spreadPicName = uid + "_" + id + "_store_" + userType + "_product_user_spread.jpg";
        if (StrUtil.isNotEmpty(localUrl)) {
            if (ObjectUtil.isNull(attachment)) {
                String spreadPicPath = fileDir + spreadPicName;
                creatShareStoreService.creatProductPic(id, qrcodeUrl, spreadPicName, spreadPicPath, apiUrl);

        }else{
            /*if(qiniuContentService.getOne(new QueryWrapper<QiniuContent>().eq("name", co.yixiang.utils.FileUtil.getFileNameNoEx(key))) != null) {
                key = QiNiuUtil.getKey(key);
            }*/
        }



      /*  String rr = creatShareStoreService.creatProductPic(id, qrcodeUrl,
                spreadPicName, spreadPicPath, apiUrl);
        return ApiResult.ok(rr);*/

    }
        return ApiResult.ok();
    }
}
