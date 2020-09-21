package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.enums.CommonEnum;
import co.yixiang.enums.ProductEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.entity.YxStoreProductAttrResult;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
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
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.mp.config.ShopKeyUtils;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
        wrapper.eq("is_del", 0).eq("is_show", 1).eq("id", id);
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
        double vipPrice = userService.setLevelPrice(
                storeProductQueryVo.getPrice().doubleValue(), uid);
        storeProductQueryVo.setVipPrice(BigDecimal.valueOf(vipPrice));
        storeProductQueryVo.setUserCollect(relationService
                .isProductRelation(id, "product", uid, "collect"));
        //销量= 销量+虚拟销量
        storeProductQueryVo.setSales(storeProductQueryVo.getSales() + storeProductQueryVo.getFicti());
        productDTO.setStoreInfo(storeProductQueryVo);
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
        YxCommissionRate commissionRate = commissionRateService.getOne(new QueryWrapper<YxCommissionRate>().eq("del_flag", 0));
        BigDecimal bigCommission = storeProduct.getCommission();

        if (ObjectUtil.isNotNull(commissionRate)) {
            //佣金= 佣金*分享
            bigCommission = bigCommission.multiply(commissionRate.getShareRate());
        }
        storeProduct.setCommission(bigCommission);

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
    @Cached(name = "cachedGoodList-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.BOTH)
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

        IPage<YxStoreProduct> pageList = yxStoreProductMapper.selectPage(pageModel, wrapper);

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
}
