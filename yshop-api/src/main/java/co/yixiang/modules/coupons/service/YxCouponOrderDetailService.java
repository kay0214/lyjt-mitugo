package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderDetailQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 卡券订单详情表 服务类
 * </p>
 *
 * @author liusy
 * @since 2020-08-28
 */
public interface YxCouponOrderDetailService extends BaseService<YxCouponOrderDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponOrderDetailQueryVo getYxCouponOrderDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponOrderDetailQueryParam
     * @return
     */
    Paging<YxCouponOrderDetailQueryVo> getYxCouponOrderDetailPageList(YxCouponOrderDetailQueryParam yxCouponOrderDetailQueryParam) throws Exception;

}
