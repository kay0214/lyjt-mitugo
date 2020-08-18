/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreProductRelation;
import co.yixiang.modules.shop.web.param.YxStoreProductRelationQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductRelationQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品点赞和收藏表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
public interface YxStoreProductRelationService extends BaseService<YxStoreProductRelation> {

    Boolean isProductRelation(int productId, String category,
                              int uid, String relationType);

    void addRroductRelation(YxStoreProductRelationQueryParam param,int uid,
                            String relationType);

    void delRroductRelation(YxStoreProductRelationQueryParam param,int uid,
                            String relationType);

    List<YxStoreProductRelationQueryVo> userCollectProduct(int page,int limit,int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductRelationQueryVo getYxStoreProductRelationById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreProductRelationQueryParam
     * @return
     */
    Paging<YxStoreProductRelationQueryVo> getYxStoreProductRelationPageList(YxStoreProductRelationQueryParam yxStoreProductRelationQueryParam) throws Exception;

}
