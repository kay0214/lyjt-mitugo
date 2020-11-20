package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.CommonEnum;
import co.yixiang.enums.ProductEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.commission.service.YxCustomizeRateService;
import co.yixiang.modules.couponUse.dto.ShipUserLeaveVO;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.shop.entity.*;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductMapper;
import co.yixiang.modules.shop.mapping.YxStoreProductMap;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.dto.FromatDetailDto;
import co.yixiang.modules.shop.web.dto.ProductDTO;
import co.yixiang.modules.shop.web.dto.ProductFormatDto;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductNoAttrQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.mp.config.ShopKeyUtils;
import co.yixiang.tools.domain.QiniuContent;
import co.yixiang.tools.service.QiNiuService;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static co.yixiang.utils.FileUtil.transformStyle;


/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings("unchecked")
public class YxStoreProductServiceImpl extends BaseServiceImpl<YxStoreProductMapper, YxStoreProduct> implements YxStoreProductService {

    @Autowired
    private YxStoreProductMapper yxStoreProductMapper;
    @Autowired
    private YxStoreProductAttrValueMapper storeProductAttrValueMapper;

    @Autowired
    private YxStoreProductAttrService storeProductAttrService;
    @Autowired
    private YxStoreProductRelationService relationService;
    @Autowired
    private YxStoreProductReplyService replyService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxStoreProductMap storeProductMap;
    @Autowired
    private YxStoreInfoMapper storeInfoMapper;
    @Autowired
    private YxCommissionRateService commissionRateService;
    @Autowired
    private YxStoreProductAttrResultService yxStoreProductAttrResultService;

    @Autowired
    private YxStoreCartService yxStoreCartService;

    @Autowired
    private YxStoreProductService storeProductService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxSystemAttachmentService systemAttachmentService;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private CreatShareProductService creatShareProductService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;

    @Autowired
    QiNiuService qiNiuService;

    @Value("${file.path}")
    private String path;

    @Value("${file.localUrl}")
    private String localUrl;


    /**
     * 增加库存 减少销量
     *
     * @param num
     * @param productId
     * @param unique
     */
    @Override
    public void incProductStock(int num, int productId, String unique) {
        if (StrUtil.isNotEmpty(unique)) {
            storeProductAttrService.incProductAttrStock(num, productId, unique);
            yxStoreProductMapper.decSales(num, productId);
            //更新attResult
            setAttrbuteResultByProductId(num, productId, unique, 1);
        } else {
            yxStoreProductMapper.incStockDecSales(num, productId);
        }
    }

    /**
     * 库存与销量
     *
     * @param num
     * @param productId
     * @param unique
     */
    @Override
    public void decProductStock(int num, int productId, String unique) {
        if (StrUtil.isNotEmpty(unique)) {
            storeProductAttrService.decProductAttrStock(num, productId, unique);
            yxStoreProductMapper.incSales(num, productId);
            //更新attResult
            setAttrbuteResultByProductId(num, productId, unique, -1);
        } else {
            yxStoreProductMapper.decStockIncSales(num, productId);
        }
    }


    private void setAttrbuteResultByProductId(int num, int productId, String unique, Integer type) {
        YxStoreProductAttrResult yxStoreProductAttrResult = yxStoreProductAttrResultService
                .getOne(new QueryWrapper<YxStoreProductAttrResult>().eq("product_id", productId));
        if (ObjectUtil.isNull(yxStoreProductAttrResult)) return;

        JSONObject jsonObject = JSON.parseObject(yxStoreProductAttrResult.getResult());

        List<FromatDetailDto> attrList = JSON.parseArray(
                jsonObject.get("attr").toString(),
                FromatDetailDto.class);
        List<ProductFormatDto> valueList = JSON.parseArray(
                jsonObject.get("value").toString(),
                ProductFormatDto.class);

        if (CollectionUtils.isEmpty(valueList)) {
            return;
        }
        for (ProductFormatDto productFormatDto : valueList) {
            if (productFormatDto.getUnique().equals(unique)) {
                //
                int sunSales = 0;
                if (productFormatDto.getSales() != 0) {
                    sunSales = productFormatDto.getSales() - num;
                }
                if (1 == type) {
                    sunSales = productFormatDto.getSales() + num;
                }
                productFormatDto.setSales(sunSales);
            }
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("attr", JSONObject.toJSON(attrList));
        map.put("value", JSONObject.toJSON(valueList));

        yxStoreProductAttrResult.setResult(JSON.toJSONString(map));
        yxStoreProductAttrResult.setChangeTime(OrderUtil.getSecondTimestampTwo());
//        yxStoreProductAttrResultService.remove(new QueryWrapper<YxStoreProductAttrResult>().eq("product_id", productId));
        yxStoreProductAttrResultService.saveOrUpdate(yxStoreProductAttrResult);
    }

    /**
     * 返回商品库存
     *
     * @param productId
     * @param unique
     * @return
     */
    @Override
    public int getProductStock(int productId, String unique) {
        if (StrUtil.isEmpty(unique)) {
            return getYxStoreProductById(productId).getStock();
        } else {
            return storeProductAttrService.uniqueByStock(unique);
        }

    }

    @Override
    public YxStoreProduct getProductInfo(int id) {
        QueryWrapper<YxStoreProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0).eq("is_show", 1).eq("id", id);
        YxStoreProduct storeProduct = yxStoreProductMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new ErrorRequestException("商品不存在或已下架");
        }

        return storeProduct;
    }

    @Override
    public ProductDTO goodsDetail(int id, int type, int uid, String latitude, String longitude) {
        QueryWrapper<YxStoreProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0).eq("id", id);
        YxStoreProduct storeProduct = yxStoreProductMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new ErrorRequestException("商品不存在或已下架");
        }
        Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(id, 0, 0);
        ProductDTO productDTO = new ProductDTO();
        YxStoreProductQueryVo storeProductQueryVo = storeProductMap.toDto(storeProduct);

        //处理库存
        Integer newStock = storeProductAttrValueMapper.sumStock(id);
        if (newStock != null) storeProductQueryVo.setStock(newStock);

        //设置VIP价格
//        double vipPrice = userService.setLevelPrice(
//                storeProductQueryVo.getPrice().doubleValue(), uid);
//        storeProductQueryVo.setVipPrice(BigDecimal.valueOf(vipPrice));
//        storeProductQueryVo.setUserCollect(relationService
//                .isProductRelation(id, "product", uid, "collect"));
        //销量= 销量+虚拟销量
        storeProductQueryVo.setSales(storeProductQueryVo.getSales() + storeProductQueryVo.getFicti());
        productDTO.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
        productDTO.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));

        productDTO.setReply(replyService.getReply(id));
        int replyCount = replyService.productReplyCount(id);
        productDTO.setReplyCount(replyCount);
        // 好评率
        productDTO.setReplyChance(replyService.doReply(id, replyCount));//百分比
        productDTO.setReplyStar(replyService.doReplyStar(id, replyCount));// 小星星

        //门店
//        productDTO.setSystemStore(systemStoreService.getStoreInfo(latitude,longitude));
        productDTO.setSystemStore(storeInfoMapper.getYxStoreInfoById(storeProductQueryVo.getStoreId()));
        productDTO.setMapKey(RedisUtil.get(ShopKeyUtils.getTengXunMapKey()));
        //佣金
        BigDecimal bigCommission = storeProductQueryVo.getCommission();

        switch (storeProductQueryVo.getCustomizeType()) {
            case 0:
                //0：按平台
                YxCommissionRate commissionRate = commissionRateService.getOne(new QueryWrapper<YxCommissionRate>().eq("del_flag", 0));
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
                YxCustomizeRate yxCustomizeRate = yxCustomizeRateService.getCustomizeRateByParam(1, storeProductQueryVo.getId());
                if (ObjectUtil.isNotNull(yxCustomizeRate)) {
                    //佣金= 佣金*分享
                    bigCommission = bigCommission.multiply(yxCustomizeRate.getShareRate());
                }
                break;
        }
        storeProductQueryVo.setCommission(bigCommission.setScale(2, BigDecimal.ROUND_DOWN));
        productDTO.setStoreInfo(storeProductQueryVo);

        return productDTO;
    }

    /**
     * 商品列表
     *
     * @return
     */
    @Override
    public List<YxStoreProductQueryVo> getGoodsList(YxStoreProductQueryParam productQueryParam) {
        List<YxStoreProductQueryVo> list = new ArrayList<YxStoreProductQueryVo>();
        QueryWrapper<YxStoreProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", CommonEnum.DEL_STATUS_0.getValue()).eq("is_show", CommonEnum.SHOW_STATUS_1.getValue());

        //分类搜索
        if (StrUtil.isNotBlank(productQueryParam.getSid()) && !productQueryParam.getSid().equals("0")) {
            wrapper.eq("cate_id", productQueryParam.getSid());
        }
        //关键字搜索
        if (StrUtil.isNotEmpty(productQueryParam.getKeyword())) {
            wrapper.like("keyword", productQueryParam.getKeyword());
        }

        //新品搜索
        if (StrUtil.isNotBlank(productQueryParam.getNews()) && productQueryParam.getNews().equals("1")) {
            wrapper.eq("is_new", 1);
        }
        //销量排序
        if (ObjectUtil.isNotNull(productQueryParam.getSalesOrder()) && StringUtils.isNotBlank(productQueryParam.getSalesOrder())) {
            if (productQueryParam.getSalesOrder().equals("desc")) {
                wrapper.orderByDesc("sales + ficti");
            } else if (productQueryParam.getSalesOrder().equals("asc")) {
                wrapper.orderByAsc("sales + ficti");
            }
        }

        //价格排序
        if (ObjectUtil.isNotNull(productQueryParam.getPriceOrder()) && StringUtils.isNotBlank(productQueryParam.getPriceOrder())) {
            if (productQueryParam.getPriceOrder().equals("desc")) {
                wrapper.orderByDesc("price");
            } else if (productQueryParam.getPriceOrder().equals("asc")) {
                wrapper.orderByAsc("price");
            }
        }
        //商品名
        if (StrUtil.isNotEmpty(productQueryParam.getName())) {
            wrapper.like("store_name", productQueryParam.getName());
        }

        //根据店铺id查询
        if (null != productQueryParam.getStoreId()) {
            wrapper.eq("store_id", productQueryParam.getStoreId());
        }

        wrapper.orderByAsc("sort");
        Page<YxStoreProduct> pageModel = new Page<>(productQueryParam.getPage(),
                productQueryParam.getLimit());

        IPage<YxStoreProduct> pageList = yxStoreProductMapper.selectPageAllProduct(pageModel, wrapper);

        list = storeProductMap.toDto(pageList.getRecords());
        if (CollectionUtils.isNotEmpty(list)) {
            for (YxStoreProductQueryVo productQueryVo : list) {
                productQueryVo.setSales(productQueryVo.getSales() + productQueryVo.getFicti());
            }
        }

        return list;
    }

    /**
     * 商品列表
     *
     * @param page
     * @param limit
     * @param order
     * @return
     */
    @Override
    @Cached(name = "cachedGoodList-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    public Paging<YxStoreProductQueryVo> getList(int page, int limit, int order) {

        QueryWrapper<YxStoreProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0).eq("is_show", 1).orderByAsc("sort");

        // order
        switch (ProductEnum.toType(order)) {
            case TYPE_1:
                wrapper.eq("is_best", 1); //精品推荐
                break;
            case TYPE_3:
                wrapper.eq("is_new", 1); //// 首发新品
                break;
            case TYPE_4:
                wrapper.eq("is_benefit", 1); //// 促销单品
                break;
            case TYPE_2:
                wrapper.eq("is_hot", 1);//// 热门榜单
                break;
        }
        Page<YxStoreProduct> pageModel = new Page<>(page, limit);

        IPage<YxStoreProduct> pageList = yxStoreProductMapper.selectPageAllProduct(pageModel, wrapper);

//        List<YxStoreProductQueryVo> list = storeProductMap.toDto(pageList.getRecords());
//        if(null == list){
//            return new ArrayList<>();
//        }
        for (YxStoreProduct product : pageList.getRecords()) {
            product.setSales(product.getSales() + product.getFicti());
        }

        return new Paging(pageList);
    }

    @Override
    public YxStoreProductQueryVo getYxStoreProductById(Serializable id) {
        return yxStoreProductMapper.getYxStoreProductById(id);
    }

    @Override
    public YxStoreProductQueryVo getNewStoreProductById(int id) {
        return storeProductMap.toDto(yxStoreProductMapper.selectById(id));
    }

    @Override
    public Paging<YxStoreProductQueryVo> getYxStoreProductPageList(YxStoreProductQueryParam yxStoreProductQueryParam) throws Exception {
        Page page = setPageParam(yxStoreProductQueryParam, OrderItem.desc("create_time"));
        IPage<YxStoreProductQueryVo> iPage = yxStoreProductMapper.getYxStoreProductPageList(page, yxStoreProductQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据商户id获取商品信息
     *
     * @param storeId
     * @return
     */
    @Override
    public List<YxStoreProductNoAttrQueryVo> getProductListByStoreId(int storeId) {
        QueryWrapper<YxStoreProduct> wrapper = new QueryWrapper<YxStoreProduct>();
        wrapper.eq("is_del", CommonEnum.DEL_STATUS_0.getValue()).eq("is_show", 1).eq("store_id", storeId);
        wrapper.orderByDesc("sort");
        List<YxStoreProduct> storeProductList = this.list(wrapper);
        List<YxStoreProductNoAttrQueryVo> queryVoList = CommonsUtils.convertBeanList(storeProductList, YxStoreProductNoAttrQueryVo.class);
        return queryVoList;
    }

    /**
     * 验证产品
     *
     * @param cartId
     * @return
     */
    @Override
    public String getProductArrtValueByCartId(String cartId) {
        //
        String[] cartIds = cartId.split(",");
        List<String> listCarts = java.util.Arrays.asList(cartIds);
        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<YxStoreCart>();
        wrapper.in("id", listCarts);
        List<YxStoreCart> yxStoreCartList = yxStoreCartService.list(wrapper);
//        YxStoreCart yxStoreCart = yxStoreCartService.getById(cartId);
        if (CollectionUtils.isNotEmpty(yxStoreCartList)) {
            for (YxStoreCart yxStoreCart : yxStoreCartList) {
                if (StringUtils.isNotBlank(yxStoreCart.getProductAttrUnique())) {
                    //规格属性不为空
                    YxStoreProductAttrValue attrValue = storeProductAttrValueMapper.getProductArrtValueByCartId(yxStoreCart.getId().intValue());
                    if (ObjectUtil.isEmpty(attrValue)) {
                        return "产品规格属性已经被修改，请重新选择后下单！";
                    }
                }
                YxStoreProduct product = this.getProductInfo(yxStoreCart.getProductId());
                if (product.getIsShow().equals(0) || product.getIsDel().equals(1)) {
                    return "产品已下架或已删除，请重新选择后下单！";
                }
            }
        }
        /*if (StringUtils.isNotBlank(yxStoreCart.getProductAttrUnique())) {
            //规格属性不为空
            YxStoreProductAttrValue attrValue = storeProductAttrValueMapper.getProductArrtValueByCartId(cartId);
            if (ObjectUtil.isEmpty(attrValue)) {
                return "产品规格属性已经被修改，请重新选择后下单！";
            }
        }
        YxStoreProduct product = this.getProductInfo(yxStoreCart.getProductId());
        if (product.getIsShow().equals(0)||product.getIsDel().equals(1)) {
            return "产品已下架或已删除，请重新选择后下单！";
        }*/
        return null;
    }

    @Override
    public ApiResult productPoster(String pageType, Integer id) throws IOException, FontFormatException {
        int uid = SecurityUtils.getUserId().intValue();

        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }

        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String spreadPicName = uid + "_" + id + "_" + pageType + "_" + userType + "_product_user_spread.jpg";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(spreadPicName);

        String spreadUrl;
        if (ObjectUtil.isNotNull(attachment)) {
            spreadUrl = attachment.getImageType().equals(2) ? attachment.getSattDir() : apiUrl + "/file/" + attachment.getSattDir();
            return ApiResult.ok(spreadUrl);
        }
        // 海报
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }

        String fileDir = path + "qrcode" + File.separator;
        String spreadPicPath = fileDir + spreadPicName;

        String name = uid + "_" + id + "_" + pageType + "_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachmentWap = systemAttachmentService.getInfo(name);
        String qrCodeUrl;
        if (attachmentWap == null) {
            QrConfig config = new QrConfig(150, 150);
            config.setMargin(0);
            //如果类型是小程序
            File file = new File(fileDir + name);
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                siteUrl = siteUrl + "/" + pageType + "/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.ROUNTINE.getValue(), config, file);
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //h5地址
                siteUrl = siteUrl + "/" + pageType + "/";
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
                        fileDir + name, "qrcode/" + name, 1);
                qrCodeUrl = apiUrl + "/file/qrcode/" + name;
            }
        } else {
            qrCodeUrl = attachmentWap.getImageType().equals(2) ? attachmentWap.getSattDir() : apiUrl + "/file/" + attachmentWap.getSattDir();
        }

        ProductInfo productInfo = new ProductInfo();
        if (pageType.equals("good")) {
            YxStoreProduct storeProduct = storeProductService.getProductInfo(id);
            BeanUtils.copyProperties(storeProduct, productInfo);
        } else {
            YxCoupons yxCoupons = yxCouponsService.getCouponsById(id);
            YxImageInfo yxImageInfo = yxImageInfoService.selectOneImg(id, CommonConstant.IMG_TYPE_CARD, CommonConstant.IMG_CATEGORY_PIC);
            if (null == yxImageInfo) {
                return ApiResult.fail("卡券图片为空");
            }
            productInfo.setImage(yxImageInfo.getImgUrl());
            productInfo.setOtPrice(yxCoupons.getOriginalPrice());
            productInfo.setStoreName(yxCoupons.getCouponName());
            productInfo.setPrice(yxCoupons.getSellingPrice());
            productInfo.setStoreInfo(yxCoupons.getCouponInfo());
        }
        //创建图片
        BufferedImage img = new BufferedImage(750, 1624, BufferedImage.TYPE_INT_RGB);
        //开启画图
        Graphics2D g = img.createGraphics();
        //背景 -- 读取互联网图片
        InputStream stream = getClass().getClassLoader().getResourceAsStream("background.png");
        ImageInputStream background = ImageIO.createImageInputStream(stream);
        BufferedImage back = ImageIO.read(background);

        g.drawImage(back.getScaledInstance(750, 1288, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图

        BufferedImage head = ImageIO.read(getClass().getClassLoader().getResourceAsStream("head.png"));
        g.drawImage(head.getScaledInstance(750, 280, Image.SCALE_DEFAULT), 0, 0, null);
        //商品  banner图
        //读取互联网图片
        BufferedImage priductUrl = null;
        try {
            priductUrl = ImageIO.read(new URL(transformStyle(productInfo.getImage())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(priductUrl.getScaledInstance(670, 604, Image.SCALE_DEFAULT), 40, 280, null);
        InputStream streamT = getClass().getClassLoader()
                .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
        File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
        FileUtils.copyInputStreamToFile(streamT, newFileT);
        Font font = Font.createFont(Font.TRUETYPE_FONT, newFileT);
        //文案标题
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g.setFont(font.deriveFont(Font.BOLD, 32));
        g.setColor(new Color(29, 29, 29));
        //文字叠加,自动换行叠加
        int tempXb = 40;
        int tempYb = 925;
        //单字符长度
        int tempCharLenb = 0;
        //单行字符总长度临时计算
        int tempLineLenb = 0;
        StringBuffer sbb = new StringBuffer();
        for (int i = 0; i < productInfo.getStoreName().length(); i++) {
            char tempChar = productInfo.getStoreName().charAt(i);
            tempCharLenb = getCharLen(tempChar, g);
            tempLineLenb += tempCharLenb;
            if (tempLineLenb >= (back.getWidth() + 220)) {
                g.drawString(sbb.toString(), tempXb, tempYb + 38);
                //清空内容,重新追加
                sbb.delete(0, sbb.length());
                //每行文字间距38
                tempYb += 38;
                tempLineLenb = 0;
            }
            //追加字符
            sbb.append(tempChar);
        }
        g.drawString(sbb.toString(), tempXb, tempYb + 38);

        //------------------------------------------------文案-----------------------

        //文案
        g.setFont(font.deriveFont(Font.PLAIN, 24));
        g.setColor(new Color(47, 47, 47));
        //文字叠加,自动换行叠加
        int tempX = 40;
        int tempY = 1030;
        //单字符长度
        int tempCharLen = 0;
        //单行字符总长度临时计算
        int tempLineLen = 0;
        StringBuffer sb = new StringBuffer();
        String info = productInfo.getStoreInfo();
        if (info.length() > 100) {
            info = info.substring(0, 100);
            info = info + "...";
        }
        for (int i = 0; i < info.length(); i++) {
            char tempChar = info.charAt(i);
            tempCharLen = getCharLen(tempChar, g);
            tempLineLen += tempCharLen;
            if (tempLineLen >= (back.getWidth() + 180)) {
                //长度已经满一行,进行文字叠加
                g.drawString(sb.toString(), tempX, tempY + 32);
                //清空内容,重新追加
                sb.delete(0, sb.length());
                //每行文字间距32
                tempY += 32;
                tempLineLen = 0;
            }
            //追加字符
            sb.append(tempChar);
        }
        //最后叠加余下的文字
        g.drawString(sb.toString(), tempX, tempY + 32);


        //价格
        g.setFont(font.deriveFont(Font.PLAIN, 39.2f));
        g.setColor(new Color(249, 64, 64));
        g.drawString("¥", 238, 1214);
        //价格
        String priceValue = productInfo.getPrice().toString();
        String priceInt = priceValue.substring(0, priceValue.indexOf("."));
        g.setFont(font.deriveFont(Font.PLAIN, 56));
        g.setColor(new Color(249, 64, 64));
        g.drawString(priceInt, 258, 1214);
        //价格
        g.setFont(font.deriveFont(Font.PLAIN, 39.2f));
        g.setColor(new Color(249, 64, 64));
        int x = 258 + priceInt.length() * 32;
        g.drawString(priceValue.substring(priceValue.indexOf(".")), x, 1214);

        //原价
        g.setFont(font.deriveFont(Font.PLAIN, 28));
        g.setColor(new Color(171, 171, 171));
        String price = "¥" + productInfo.getOtPrice();
        g.drawString(price, x + 62, 1214);
        g.drawLine(x + 62, 1205, x + 62 + price.length() * 14, 1205);

        //背景 -- 读取互联网图片
        InputStream stream2 = getClass().getClassLoader().getResourceAsStream("background2.png");
        ImageInputStream background2 = ImageIO.createImageInputStream(stream2);
        BufferedImage back2 = ImageIO.read(background2);

        g.drawImage(back2.getScaledInstance(750, 336, Image.SCALE_DEFAULT), 0, 1288, null); // 绘制缩小后的图

        //读取互联网图片
        BufferedImage qrCode = null;
        try {
            qrCode = ImageIO.read(new URL(qrCodeUrl));
        } catch (IOException e) {
            log.error("二维码图片读取失败", e);
            e.printStackTrace();
        }
        // 绘制缩小后的图
        g.drawImage(qrCode.getScaledInstance(150, 150, Image.SCALE_DEFAULT), 54, 1334, null);

        //二维码字体
        g.setFont(font.deriveFont(Font.PLAIN, 20));
        g.setColor(new Color(171, 171, 171));
        //绘制文字
        g.drawString("扫描或长按小程序码", 238, 1388);

        g.setFont(font.deriveFont(Font.PLAIN, 20));
        g.setColor(new Color(171, 171, 171));
        g.drawString("查看商品详情", 238, 1428);

        g.dispose();
        //先将画好的海报写到本地
        File file = new File(spreadPicPath);
        try {
            ImageIO.write(img, "jpg", file);
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
                    spreadPicPath, "qrcode/" + spreadPicName, 1);
            spreadUrl = apiUrl + "/file/qrcode/" + spreadPicName;
        }
        return ApiResult.ok(spreadUrl);
    }

    /**
     * 商户的本地生活商品数量
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Integer> getLocalProductCount(Integer storeId) {
        return yxStoreProductMapper.getLocalProductCount(storeId);
    }

    /**
     * 本地生活订单相关数量
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Integer> getLocalProductOrderCount(Integer storeId) {
        return yxStoreProductMapper.getLocalProductOrderCount(storeId);
    }

    /**
     * 今日营业额
     *
     * @param storeId
     * @return
     */
    @Override
    public BigDecimal getLocalSumPrice(Integer storeId) {
        return yxStoreProductMapper.getLocalSumPrice(storeId);
    }

    /**
     * 商城商品相关
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Integer> getShopProductCount(Integer storeId) {
        return yxStoreProductMapper.getShopProductCount(storeId);
    }

    /**
     * 商城订单数量相关
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Integer> getShopOrderCount(Integer storeId) {
        return yxStoreProductMapper.getShopOrderCount(storeId);
    }

    /**
     * 商城今日营业额
     *
     * @param storeId
     * @return
     */
    @Override
    public BigDecimal getShopSumPrice(Integer storeId) {
        return yxStoreProductMapper.getShopSumPrice(storeId);
    }

    /**
     * 船只出港次数最多的船只
     *
     * @param storeId
     * @return
     */
    @Override
    public List<ShipUserLeaveVO> getTopShipLeaves(Integer storeId) {
        return yxStoreProductMapper.getTopShipLeaves(storeId);
    }

    /**
     * 船只出港次数最多的船长
     *
     * @param storeId
     * @return
     */
    @Override
    public List<ShipUserLeaveVO> getShipUserLeaveS(Integer storeId) {
        return yxStoreProductMapper.getShipUserLeaveS(storeId);
    }

    /**
     * 今日出港次数
     *
     * @param storeId
     * @return
     */
    @Override
    public Integer getShipLeaveCount(Integer storeId) {
        return yxStoreProductMapper.getShipLeaveCount(storeId);
    }

    /**
     * 今日运营船只
     *
     * @param storeId
     * @return
     */
    @Override
    public Integer getShipCount(Integer storeId) {
        return yxStoreProductMapper.getShipCount(storeId);
    }

    /**
     * 所有商品数量
     *
     * @return
     */
    @Override
    public Integer getAllProduct() {
        return yxStoreProductMapper.getAllProduct();
    }

    /**
     * 本地生活商品数量
     *
     * @return
     */
    @Override
    public Integer getLocalProduct() {
        return yxStoreProductMapper.getLocalProduct();
    }

    public static int getCharLen(char c, Graphics g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }
}
