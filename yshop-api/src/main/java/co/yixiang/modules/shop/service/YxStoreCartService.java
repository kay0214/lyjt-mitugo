/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
public interface YxStoreCartService extends BaseService<YxStoreCart> {

    void removeUserCart(int uid, List<String> ids);

    void changeUserCartNum(int cartId,int cartNum,int uid);

    Map<String,Object> getUserProductCartList(int uid,String cartIds,int status);

    int getUserCartNum(int uid,String type,int numType);

    int addCart(int uid,int productId,int cartNum, String productAttrUnique,
                String type,int isNew,int combinationId,int seckillId,int bargainId);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCartQueryVo getYxStoreCartById(Serializable id);

    /**
     * 验证产品库存是否足够
     * @param uid  用户ID
     * @param productId  产品ID
     * @param cartNum 购买数量
     * @param productAttrUnique  商品属性Unique
     * @param combinationId  拼团产品ID
     * @param seckillId      秒杀产品ID
     * @param bargainId   砍价产品ID
     */
    void checkProductStock(int uid, int productId, int cartNum, String productAttrUnique,
                           int combinationId, int seckillId, int bargainId);
}
