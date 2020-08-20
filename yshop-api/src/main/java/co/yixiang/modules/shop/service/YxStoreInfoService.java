package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
public interface YxStoreInfoService extends BaseService<YxStoreInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreInfoQueryVo getYxStoreInfoById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreInfoQueryParam
     * @return
     */
    Paging<YxStoreInfoQueryVo> getYxStoreInfoPageList(YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception;

    /**
     * 根据参数获取店铺信息
     * @param yxStoreInfoQueryParam
     * @return
     */
    List<YxStoreInfoQueryVo> getStoreInfoList(YxStoreInfoQueryParam yxStoreInfoQueryParam);
}
