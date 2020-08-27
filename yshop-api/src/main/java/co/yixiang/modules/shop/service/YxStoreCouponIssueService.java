/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券前台领取表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreCouponIssueService extends BaseService<YxStoreCouponIssue> {

    int couponCount(int id,int uid);

    void issueUserCoupon(int id,int uid);

    List<YxStoreCouponIssueQueryVo> getCouponList(int page, int limit, int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponIssueQueryVo getYxStoreCouponIssueById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreCouponIssueQueryParam
     * @return
     */
    Paging<YxStoreCouponIssueQueryVo> getYxStoreCouponIssuePageList(YxStoreCouponIssueQueryParam yxStoreCouponIssueQueryParam) throws Exception;

    /**
     * 领取优惠券
     * @param id id
     * @param uid uid
     */
    void issueUserCouponNew(int id, int uid);
}
