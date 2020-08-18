/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreProductAttrResult;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrResultQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrResultQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 商品属性详情表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
public interface YxStoreProductAttrResultService extends BaseService<YxStoreProductAttrResult> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrResultQueryVo getYxStoreProductAttrResultById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreProductAttrResultQueryParam
     * @return
     */
    Paging<YxStoreProductAttrResultQueryVo> getYxStoreProductAttrResultPageList(YxStoreProductAttrResultQueryParam yxStoreProductAttrResultQueryParam) throws Exception;

}
