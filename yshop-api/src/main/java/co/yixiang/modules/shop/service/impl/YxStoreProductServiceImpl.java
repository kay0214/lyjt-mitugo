package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.WxUtils;
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
    YxUserService yxUserService;
    @Autowired
    CreatShareProductService creatShareProductService;
    @Autowired
    YxCouponsService yxCouponsService;
    @Autowired
    YxImageInfoService yxImageInfoService;
    @Autowired
    private YxCustomizeRateService yxCustomizeRateService;

    @Autowired
    QiNiuService qiNiuService;

    @Value("${file.path}")
    private String path;

    @Value("${file.localUrl}")
    private String localUrl;

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${yshop.isProd}")
    private boolean isProd;

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
                YxCustomizeRate yxCustomizeRate = yxCustomizeRateService.getCustomizeRateByParam(0, storeProductQueryVo.getId());
                if (ObjectUtil.isNotNull(yxCustomizeRate)) {
                    //佣金= 佣金*分享
                    bigCommission = bigCommission.multiply(yxCustomizeRate.getShareRate());
                }
                break;
        }
        storeProductQueryVo.setCommission(bigCommission.setScale(2, BigDecimal.ROUND_DOWN));
        productDTO.setStoreInfo(storeProductQueryVo);
        // 获取商品关闭状态
        String storeClose = systemConfigService.getData(SystemConfigConstants.STORE_CLOSE_SWITCH);
        if (StringUtils.isNotBlank(storeClose)) {
            productDTO.setStoreClose(Integer.valueOf(storeClose));
        }

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
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }

        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String spreadPicName = uid + "_" + id + "_" + pageType + "_" + userType + "_product_user_spread.png";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(spreadPicName);

        String spreadUrl;
        if (ObjectUtil.isNotNull(attachment)) {
            spreadUrl = attachment.getImageType().equals(2) ? attachment.getSattDir() : apiUrl + "/file/" + attachment.getSattDir();
            return ApiResult.ok(spreadUrl);
        }
        // 海报
        String fileDir = path + "qrcode" + File.separator;
        String spreadPicPath = fileDir + spreadPicName;
        // 二维码地址获取

        // 测试环境二维码生成走旧逻辑
        String qrCodeUrl = "";
        if (isProd) {
            qrCodeUrl = getQrCode(fileDir, userType, siteUrl, apiUrl, uid, id, pageType);
        } else {
            qrCodeUrl = getCode(fileDir, userType, siteUrl, apiUrl, uid, id);
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
        BufferedImage priductUrl = null;
        try {
            priductUrl = ImageIO.read(new URL(transformStyle(productInfo.getImage())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(priductUrl.getScaledInstance(612, 416, Image.SCALE_DEFAULT), 37, 218, null);
        InputStream streamT = getClass().getClassLoader()
                .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
        File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
        FileUtils.copyInputStreamToFile(streamT, newFileT);
        //文案标题
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g.setFont(new Font("SimHei", Font.BOLD, 40));
        g.setColor(new Color(29, 29, 29));
        String storeName = productInfo.getStoreName();
        storeName = storeName.length() > 14 ? storeName.substring(0, 13) + "..." : storeName;
        g.drawString(storeName, 41, 703);

        //价格
        g.setFont(new Font("ArialMT", Font.PLAIN, 45));
        g.setColor(new Color(249, 64, 64));
        g.drawString("¥", 44, 780);

        //价格
        String priceValue = productInfo.getPrice().toString();
        //String priceInt = priceValue.substring(0, priceValue.indexOf("."));
        //g.setFont(font.deriveFont(Font.PLAIN, 55));
        g.setFont(new Font("Adobe Heiti Std R", Font.BOLD, 55));
        g.setColor(new Color(249, 64, 64));
        g.drawString(priceValue, 80, 780);

        //原价
        int x = 102 + priceValue.length() * 29;
        g.setFont(new Font("ArialMT", Font.PLAIN, 40));
        g.setColor(new Color(171, 171, 171));
        String price = "¥" + productInfo.getOtPrice();
        g.drawString(price, x + 22, 779);
        g.drawLine(x + 22, 765, x + 22 + price.length() * 20, 765);

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
    public Map<String, Long> getLocalProductCount(Integer storeId) {
        return yxStoreProductMapper.getLocalProductCount(storeId);
    }

    /**
     * 本地生活订单相关数量
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Long> getLocalProductOrderCount(Integer storeId) {
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
    public Map<String, Long> getShopProductCount(Integer storeId) {
        return yxStoreProductMapper.getShopProductCount(storeId);
    }

    /**
     * 商城订单数量相关
     *
     * @param storeId
     * @return
     */
    @Override
    public Map<String, Long> getShopOrderCount(Integer storeId) {
        return yxStoreProductMapper.getShopOrderCount(storeId);
    }

    /**
     * 商城订单数量相关(全部)
     *
     * @param storeId
     * @return
     */
    @Override
    public Long getShopOrderSend(Integer storeId) {
        return yxStoreProductMapper.getShopOrderSend(storeId);
    }

    /**
     * 商城订单数量相关(全部)
     *
     * @param storeId
     * @return
     */
    @Override
    public Long getShopOrderRefund(Integer storeId) {
        return yxStoreProductMapper.getShopOrderRefund(storeId);
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
     * 未核销订单（取总值）
     *
     * @param storeId
     * @return
     */
    @Override
    public Long getLocalOrderWait(Integer storeId) {
        return yxStoreProductMapper.getLocalOrderWait(storeId);
    }

    /**
     * 待处理退款（取总值）
     *
     * @param storeId
     * @return
     */
    @Override
    public Long getLocalOrderRefund(Integer storeId) {
        return yxStoreProductMapper.getLocalOrderRefund(storeId);
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


    /**
     * 缩放图片+透明
     *
     * @param image  需要缩放的图片
     * @param width  宽
     * @param height 高
     * @return BufferedImage
     */
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        java.awt.Image img = image.getScaledInstance(width, height, java.awt.Image.SCALE_FAST);
        BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newBufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return newBufferedImage;
    }

    /**
     * 获取二维码连接
     *
     * @param fileDir
     * @param userType
     * @param siteUrl
     * @param apiUrl
     * @param uid
     * @param id
     * @return
     */
    public String getCode(String fileDir, String userType, String siteUrl, String apiUrl, int uid, int id) {
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachmentWap = systemAttachmentService.getInfo(name);
        String qrCodeUrl;
        if (attachmentWap != null) {
            qrCodeUrl = attachmentWap.getImageType().equals(2) ? attachmentWap.getSattDir() : apiUrl + "/file/" + attachmentWap.getSattDir();
        } else {
            QrConfig config = new QrConfig(228, 228);
            config.setMargin(0);
            //如果类型是小程序
            File file = new File(fileDir + name);
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                siteUrl = siteUrl + "/shop/";
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.ROUNTINE.getValue(), config, file);
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //app地址
                siteUrl = siteUrl + "/shop/";
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), config, file);
            } else {//如果类型是h5
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
        }
        return qrCodeUrl;
    }

    /**
     * 获取小程序码连接
     *
     * @param fileDir
     * @param userType
     * @param siteUrl
     * @param apiUrl
     * @param uid
     * @param id
     * @return
     */
    public String getQrCode(String fileDir, String userType, String siteUrl, String apiUrl, int uid, int id, String pageType) {
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachmentWap = systemAttachmentService.getInfo(name);
        String qrCodeUrl;
        if (attachmentWap != null) {
            qrCodeUrl = attachmentWap.getImageType().equals(2) ? attachmentWap.getSattDir() : apiUrl + "/file/" + attachmentWap.getSattDir();
        } else {
            QrConfig config = new QrConfig(228, 228);
            config.setMargin(0);
            //如果类型是小程序
            File file = new File(fileDir + name);
            String appId = WxUtils.getAppId();
            String secret = WxUtils.getSecret();
            String accessToken = WxUtils.getAccessToken(appId, secret);
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                WxUtils.getQrCode(accessToken, fileDir + name, "id=" + id + "&uid=" + uid + "&type=" + pageType);
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //app地址
                siteUrl = siteUrl + "/shop/";
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), config, file);
            } else {//如果类型是h5
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.H5.getValue(), config, file);
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
        }
        return qrCodeUrl;
    }
}
