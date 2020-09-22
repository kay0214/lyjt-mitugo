package co.yixiang.modules.shop.service;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.param.LocalLiveQueryParam;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveListVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoDetailQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

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
    IPage<YxStoreInfoQueryVo> getStoreInfoList(YxStoreInfoQueryParam yxStoreInfoQueryParam);
    /**
     * 显示店铺详情
     * @param id
     * @return
     */
    public YxStoreInfoDetailQueryVo getStoreDetailInfoById(Integer id);


    /**
     * 查询本地生活店铺列表
     * @param localLiveQueryParam
     * @return
     * @throws Exception
     */
    Paging<LocalLiveListVo> getLocalLiveList(LocalLiveQueryParam localLiveQueryParam,String location) throws Exception;

    ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageListByStoreId(YxCouponsQueryParam yxCouponsQueryParam);

    /**
     * 根据nid查询店铺信息
     * @param storeNid
     * @return
     */
    YxStoreInfoQueryVo getYxStoreInfoByNid(String storeNid);

    ApiResult productPoster(Integer id) throws IOException, FontFormatException;
}
