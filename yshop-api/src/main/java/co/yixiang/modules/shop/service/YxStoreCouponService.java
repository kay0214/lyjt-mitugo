/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreCoupon;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreCouponService extends BaseService<YxStoreCoupon> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponQueryVo getYxStoreCouponById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreCouponQueryParam
     * @return
     */
    Paging<YxStoreCouponQueryVo> getYxStoreCouponPageList(YxStoreCouponQueryParam yxStoreCouponQueryParam) throws Exception;

}
