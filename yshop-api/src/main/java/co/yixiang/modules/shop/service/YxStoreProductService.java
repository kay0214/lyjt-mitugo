package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.dto.ProductDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

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


    void incProductStock(int num,int productId,String unique);

    void decProductStock(int num,int productId,String unique);

    int getProductStock(int productId,String unique);


    ProductDTO goodsDetail(int id, int type, int uid, String latitude, String longitude);

    List<YxStoreProductQueryVo> getGoodsList(YxStoreProductQueryParam productQueryParam);

    /**
     * 商品列表
     * @param page
     * @param limit
     * @param order
     * @return
     */
    List<YxStoreProductQueryVo> getList(int page, int limit, int order);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductQueryVo getYxStoreProductById(Serializable id);

    YxStoreProductQueryVo getNewStoreProductById(int id);

    /**
     * 获取分页对象
     * @param yxStoreProductQueryParam
     * @return
     */
    Paging<YxStoreProductQueryVo> getYxStoreProductPageList(YxStoreProductQueryParam yxStoreProductQueryParam) throws Exception;

}
