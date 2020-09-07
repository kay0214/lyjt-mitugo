/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.web.param.YxStoreCouponUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponUserQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券发放记录表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreCouponUserService extends BaseService<YxStoreCouponUser> {

    int getUserValidCouponCount(int uid);

    void useCoupon(int id);

    YxStoreCouponUser getCoupon(int id,int uid);

    List<YxStoreCouponUser> beUsableCouponList(int uid,double price);

    YxStoreCouponUser beUsableCoupon(int uid,double price);

    void checkInvalidCoupon(int uid);

    List<YxStoreCouponUserQueryVo > getUserCoupon(int uid,int type);

    void addUserCoupon(int uid,int cid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponUserQueryVo getYxStoreCouponUserById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreCouponUserQueryParam
     * @return
     */
    Paging<YxStoreCouponUserQueryVo> getYxStoreCouponUserPageList(YxStoreCouponUserQueryParam yxStoreCouponUserQueryParam) throws Exception;

    void addUserCouponNew(int uid, int cid,int storeId);

    /**
     * 创建订单时，获取用户可用优惠券
     * @param uid
     * @param price
     * @param cartIds
     * @return
     */
    List<YxStoreCouponUserQueryVo> getUsableCouponList(int uid, double price,String cartIds);

    /**
     * 获取可用优惠券
     * @param uid
     * @param price
     * @return
     */
    List<YxStoreCouponUser> beUsableCouponListStore(int uid, double price,int storeId);
    List<YxStoreCouponUser> getCouponList(List<Integer> ids, int uid, Integer storeId);

    ApiResult<Paging<YxStoreCouponUserQueryVo>> getUserCouponNew(YxStoreCouponUserQueryParam param);
}
