/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.domain.YxStoreProductChange;
import co.yixiang.modules.shop.service.dto.ProductFormatDto;
import co.yixiang.modules.shop.service.dto.YxStoreProductDto;
import co.yixiang.modules.shop.service.dto.YxStoreProductQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author hupeng
* @date 2020-05-12
*/
public interface YxStoreProductService  extends BaseService<YxStoreProduct>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxStoreProductQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxStoreProductDto>
    */
    List<YxStoreProduct> queryAll(YxStoreProductQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxStoreProductDto> all, HttpServletResponse response) throws IOException;

    YxStoreProductDto saveProduct(YxStoreProductDto storeProduct);

    void recovery(Integer id);

    void onSale(Integer id, int status);

    List<ProductFormatDto> isFormatAttr(Integer id, String jsonStr);

    void createProductAttr(Integer id, String jsonStr);

    void clearProductAttr(Integer id,boolean isActice);

    void setResult(Map<String, Object> map,Integer id);

    String getStoreProductAttrResult(Integer id);

    void updateProduct(YxStoreProductDto resources);

    void delete(Integer id);

    /**
     *
     * @param request
     */
    void changeStatus(YxStoreProductChange request);

    /**
     * 修改商品分佣比例
     *
     * @param resources
     */
    void updateRate(YxStoreProductDto resources);

    /**
     * 卡券相关 数据统计
     * @param storeId
     * @return
     */
    Map<String, Long> getLocalProductCount(int storeId);

    /**
     * 卡券订单相关
     * @param storeId
     * @return
     */
    Map<String, Long> getLocalProductOrderCount(int storeId);

    /**
     * 今日营业额
     * @param storeId
     * @return
     */
    BigDecimal getLocalSumPrice(int storeId);

    /**
     * 商城相关
     * @param storeId
     * @return
     */
    Map<String, Long> getShopProductCount(int storeId);

    /**
     * 商城订单数量相关
     * @param storeId
     * @return
     */
    Map<String, Long> getShopOrderCount(int storeId);

    /**
     * 本月本地生活成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    Double getMonthLocalPrice(int nowMonth, int storeId);

    /**
     * 本月本地生活成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    Integer getMonthLocalCount(int nowMonth, int storeId);

    /**
     * 本月商城成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    Double getMonthShopPrice(int nowMonth, int storeId);

    /**
     * 本月商城成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    Integer getMonthShopCount(int nowMonth, int storeId);

    /**
     * 近七天本地生活成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    Integer getLastWeekLocalCount(int nowMonth, int storeId);

    /**
     * 近七天本地生活成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    Double getLastWeekLocalPrice(int nowMonth, int storeId);

    /**
     * 近七天商城成交量
     * @param nowMonth
     * @param storeId
     * @return
     */
    Integer getLastWeekShopCount(int nowMonth, int storeId);

    /**
     * 近七天商城成交额
     * @param nowMonth
     * @param storeId
     * @return
     */
    Double getLastWeekShopPrice(int nowMonth, int storeId);
}
