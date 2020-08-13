package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrValueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrValueQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 商品属性值表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxStoreProductAttrValueService extends BaseService<YxStoreProductAttrValue> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrValueQueryVo getYxStoreProductAttrValueById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreProductAttrValueQueryParam
     * @return
     */
    Paging<YxStoreProductAttrValueQueryVo> getYxStoreProductAttrValuePageList(YxStoreProductAttrValueQueryParam yxStoreProductAttrValueQueryParam) throws Exception;

}
