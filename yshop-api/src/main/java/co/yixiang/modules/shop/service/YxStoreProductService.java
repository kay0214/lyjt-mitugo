package co.yixiang.modules.shop.service;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.ShipUserLeaveVO;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.web.dto.ProductDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductNoAttrQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxStoreProductService extends BaseService<YxStoreProduct> {

    YxStoreProduct getProductInfo(int id);


    void incProductStock(int num, int productId, String unique);

    void decProductStock(int num, int productId, String unique);

    int getProductStock(int productId, String unique);


    ProductDTO goodsDetail(int id, int type, int uid, String latitude, String longitude);

    List<YxStoreProductQueryVo> getGoodsList(YxStoreProductQueryParam productQueryParam);

    /**
     * 商品列表
     *
     * @param page
     * @param limit
     * @param order
     * @return
     */
    Paging<YxStoreProductQueryVo> getList(int page, int limit, int order);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxStoreProductQueryVo getYxStoreProductById(Serializable id);

    YxStoreProductQueryVo getNewStoreProductById(int id);

    /**
     * 获取分页对象
     *
     * @param yxStoreProductQueryParam
     * @return
     */
    Paging<YxStoreProductQueryVo> getYxStoreProductPageList(YxStoreProductQueryParam yxStoreProductQueryParam) throws Exception;

    /**
     * 根据商户id获取商品信息
     *
     * @param storeId
     * @return
     */
    public List<YxStoreProductNoAttrQueryVo> getProductListByStoreId(int storeId);

    /**
     * 查询所有商品数量
     *
     * @return
     */
    Integer getAllProduct();

    /**
     * 本地生活商品数量
     *
     * @return
     */
    Integer getLocalProduct();

    /**
     * 根据cartId获取规格属性
     *
     * @param cartId
     * @return
     */
    public String getProductArrtValueByCartId(String cartId);

    /**
     * 商品海报
     *
     * @param pageType
     * @param id
     * @return
     */
    ApiResult productPoster(String pageType, Integer id) throws IOException, FontFormatException;

    /**
     * 本地生活商品数量
     *
     * @param storeId
     * @return
     */
    Map<String, Long> getLocalProductCount(Integer storeId);

    /**
     * 本地生活订单相关数量
     *
     * @param storeId
     * @return
     */
    Map<String, Long> getLocalProductOrderCount(Integer storeId);

    /**
     * 今日营业额
     *
     * @param storeId
     * @return
     */
    BigDecimal getLocalSumPrice(Integer storeId);

    /**
     * 商城商品相关
     *
     * @param storeId
     * @return
     */
    Map<String, Long> getShopProductCount(Integer storeId);

    /**
     * 商城订单数量相关
     *
     * @param storeId
     * @return
     */
    Map<String, Long> getShopOrderCount(Integer storeId);

    Long getShopOrderSend(Integer storeId);

    Long getShopOrderRefund(Integer storeId);

    /**
     * 商城今日营业额
     *
     * @param storeId
     * @return
     */
    BigDecimal getShopSumPrice(Integer storeId);

    /**
     * 船只出港次数最多的船只
     *
     * @param storeId
     * @return
     */
    List<ShipUserLeaveVO> getTopShipLeaves(Integer storeId);

    /**
     * 出港最多的船长
     *
     * @param storeId
     * @return
     */
    List<ShipUserLeaveVO> getShipUserLeaveS(Integer storeId);

    /**
     * 今日出港次数
     *
     * @param storeId
     * @return
     */
    Integer getShipLeaveCount(Integer storeId);

    /**
     * 今日运营船只
     *
     * @param storeId
     * @return
     */
    Integer getShipCount(Integer storeId);

    /**
     * 未核销订单（取总值）
     *
     * @param storeId
     * @return
     */
    Long getLocalOrderWait(Integer storeId);

    /**
     * 待处理退款（取总值）
     *
     * @param storeId
     * @return
     */
    Long getLocalOrderRefund(Integer storeId);
}
