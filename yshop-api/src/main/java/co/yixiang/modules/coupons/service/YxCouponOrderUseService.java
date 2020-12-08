package co.yixiang.modules.coupons.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.criteria.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxCouponOrderUseService extends BaseService<YxCouponOrderUse> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxCouponOrderUseQueryVo getYxCouponOrderUseById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param yxCouponOrderUseQueryParam
     * @return
     */
    Paging<YxCouponOrderUseQueryVo> getYxCouponOrderUsePageList(YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam) throws Exception;


    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @return Map<String   ,   Object>
     */
    Map<String, Object> queryAll(YxCouponOrderUseQueryCriteria criteria);

    /**
     * 根据订单号获取最新一条核销记录
     *
     * @param couponOrderId
     * @return
     */
    YxCouponOrderUse getOneByOrderId(String couponOrderId);
}
