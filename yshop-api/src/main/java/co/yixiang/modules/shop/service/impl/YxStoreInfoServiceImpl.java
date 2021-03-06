package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.DistanceMeterUtil;
import co.yixiang.common.util.WxUtils;
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
import co.yixiang.modules.shop.service.YxStoreAttributeService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoDetailQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.tools.domain.QiniuContent;
import co.yixiang.tools.service.QiNiuService;
import co.yixiang.tools.service.QiniuContentService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static co.yixiang.utils.FileUtil.transformStyle;

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
    private YxImageInfoMapper yxImageInfoMapper;
    @Autowired
    private YxStoreCouponIssueMapper couponIssueMapper;


    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxSystemAttachmentService systemAttachmentService;

    @Autowired
    private YxUserService yxUserService;

    @Autowired
    QiNiuService qiNiuService;
    @Autowired
    QiniuContentService qiniuContentService;
    @Value("${file.path}")
    private String path;


    @Value("${file.localUrl}")
    private String localUrl;

    @Value("${yshop.isProd}")
    private boolean isProd;


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
    public ApiResult productPoster(Integer id) throws IOException {
        int uid = SecurityUtils.getUserId().intValue();

        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }
        // 海报
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }

        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String fileDir = path + "qrcode" + File.separator;
        String spreadPicName = uid + "_" + id + "_store_" + userType + "_product_user_spread.png";
        String spreadPicPath = fileDir + spreadPicName;

        YxSystemAttachment attachmentT = systemAttachmentService.getInfo(spreadPicName);
        String spreadUrl;
        if (ObjectUtil.isNotNull(attachmentT)) {
            spreadUrl = attachmentT.getImageType().equals(2) ? attachmentT.getSattDir() : apiUrl + "/file/" + attachmentT.getSattDir();
            return ApiResult.ok(spreadUrl);
        }

        String qrCodeUrl = "";//getCode(fileDir, userType, siteUrl, apiUrl, uid, id);
        // 测试环境二维码生成走旧逻辑
        if (isProd) {
            qrCodeUrl = getCode(fileDir, userType, siteUrl, apiUrl, uid, id);
        } else {
            qrCodeUrl = getOldCode(fileDir, userType, siteUrl, apiUrl, uid, id);
        }

        YxStoreInfo yxStoreInfo = yxStoreInfoMapper.selectById(id);
        YxImageInfo yxImageInfo = yxImageInfoService.selectOneImg(id, CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC);

        //创建图片
        BufferedImage img = new BufferedImage(688, 1133, BufferedImage.TYPE_INT_ARGB);
        //开启画图
        Graphics2D g = img.createGraphics();
        //背景 -- 读取互联网图片
        InputStream stream = getClass().getClassLoader().getResourceAsStream("af-background.png");
        ImageInputStream background = ImageIO.createImageInputStream(stream);
        BufferedImage back = ImageIO.read(background);

        g.drawImage(back.getScaledInstance(688, 1133, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图

        BufferedImage head = ImageIO.read(getClass().getClassLoader().getResourceAsStream("af-head.png"));
        g.drawImage(head.getScaledInstance(393, 125, Image.SCALE_DEFAULT), 44, 76, null);

        //商品  banner图
        BufferedImage productUrl = null;
        try {
            productUrl = ImageIO.read(new URL(transformStyle(yxImageInfo.getImgUrl())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(productUrl.getScaledInstance(612, 469, Image.SCALE_DEFAULT), 37, 218, null);
        InputStream streamT = getClass().getClassLoader()
                .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
        File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
        FileUtils.copyInputStreamToFile(streamT, newFileT);
        //文案标题
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        String storeName = yxStoreInfo.getStoreName();
        storeName = storeName.length() > 14 ? storeName.substring(0, 13) + "..." : storeName;
        g.setFont(new Font(null, Font.BOLD, 40));
        g.setColor(new Color(29, 29, 29));
        Font font = new Font("SimHei", Font.BOLD, 40);
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(storeName);
        int widthX = (688 - textWidth) / 2;


        g.drawString(storeName, widthX, 782);

        //读取互联网图片
        BufferedImage qrCode = null;
        try {
            qrCode = ImageIO.read(new URL(qrCodeUrl));
        } catch (IOException e) {
            log.error("二维码图片读取失败", e);
            e.printStackTrace();
        }
        // 绘制缩小后的图
        g.drawImage(qrCode.getScaledInstance(228, 228, Image.SCALE_DEFAULT), 76, 847, null);

        //二维码字体
        g.setFont(new Font("SimHei", Font.PLAIN, 30));
        g.setColor(new Color(29, 29, 29));
        g.drawString("扫描或长按小程序码", 331, 926);
        g.setFont(new Font("SimHei", Font.PLAIN, 30));
        g.setColor(new Color(29, 29, 29));
        g.drawString("查看商品详情", 331, 968);

        g.dispose();
        //先将画好的海报写到本地
        File file = new File(spreadPicPath);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StrUtil.isEmpty(localUrl)) {
            QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
            systemAttachmentService.attachmentAdd(spreadPicName, String.valueOf(qiniuContent.getSize()),
                    qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
            spreadUrl = qiniuContent.getUrl();
        } else {
            systemAttachmentService.attachmentAdd(spreadPicName, String.valueOf(FileUtil.size(new File(spreadPicPath))),
                    spreadPicPath, "qrcode/" + spreadPicName);
            spreadUrl = apiUrl + "/file/qrcode/" + spreadPicName;
        }
        return ApiResult.ok(spreadUrl);
    }

    private String getOldCode(String fileDir, String userType, String siteUrl, String apiUrl, int uid, Integer id) {
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        String qrCodeUrl;
        YxSystemAttachment attachmentWap = systemAttachmentService.getInfo(name);
        if (attachmentWap == null) {
            QrConfig config = new QrConfig(150, 150);
            config.setMargin(0);
            //如果类型是小程序
            File file = new File(fileDir + name);
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                siteUrl = siteUrl + "/shop/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.ROUNTINE.getValue(), config, file);
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //h5地址
                siteUrl = siteUrl + "/shop/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), config, file);
            } else {//如果类型是h5
                //生成二维码
                QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, config, file);
            }

            if (StrUtil.isEmpty(localUrl)) {
                QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
                systemAttachmentService.attachmentAdd(name, String.valueOf(qiniuContent.getSize()),
                        qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
                qrCodeUrl = qiniuContent.getUrl();
            } else {
                systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                        fileDir + name, "qrcode/" + name);
                qrCodeUrl = apiUrl + "/file/qrcode/" + name;
            }
        } else {
            qrCodeUrl = attachmentWap.getImageType().equals(2) ? attachmentWap.getSattDir() : apiUrl + "/file/" + attachmentWap.getSattDir();
        }
        return qrCodeUrl;
    }

    public String getCode(String fileDir, String userType, String siteUrl, String apiUrl, int uid, int id) {
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachmentWap = systemAttachmentService.getInfo(name);
        String qrCodeUrl;
        if (attachmentWap == null) {
            QrConfig config = new QrConfig(228, 228);
            config.setMargin(0);
            //如果类型是小程序
            File file = new File(fileDir + name);
            String appId = WxUtils.getAppId();
            String secret = WxUtils.getSecret();
            String accessToken = WxUtils.getAccessToken(appId, secret);
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                //生成二维码
                WxUtils.getQrCode(accessToken, fileDir + name, "id=" + id + "&uid=" + uid + "&type=shop");
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //app地址
                siteUrl = siteUrl + "/shop/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), config, file);
            } else {//如果类型是h5
                //生成二维码
                QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, config, file);
            }

            if (StrUtil.isEmpty(localUrl)) {
                QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
                systemAttachmentService.attachmentAdd(name, String.valueOf(qiniuContent.getSize()),
                        qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
                qrCodeUrl = qiniuContent.getUrl();
            } else {
                systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                        fileDir + name, "qrcode/" + name);
                qrCodeUrl = apiUrl + "/file/qrcode/" + name;
            }
        } else {
            qrCodeUrl = attachmentWap.getImageType().equals(2) ? attachmentWap.getSattDir() : apiUrl + "/file/" + attachmentWap.getSattDir();
        }
        return qrCodeUrl;
    }

}
