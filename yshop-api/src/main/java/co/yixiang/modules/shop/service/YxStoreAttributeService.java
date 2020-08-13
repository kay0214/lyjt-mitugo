package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreAttributeQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreAttributeQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 店铺属性表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxStoreAttributeService extends BaseService<YxStoreAttribute> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreAttributeQueryVo getYxStoreAttributeById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreAttributeQueryParam
     * @return
     */
    Paging<YxStoreAttributeQueryVo> getYxStoreAttributePageList(YxStoreAttributeQueryParam yxStoreAttributeQueryParam) throws Exception;

}
