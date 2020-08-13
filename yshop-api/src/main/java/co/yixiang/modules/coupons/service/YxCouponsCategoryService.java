package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponsCategory;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponsCategoryQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsCategoryQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 本地生活, 卡券分类表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxCouponsCategoryService extends BaseService<YxCouponsCategory> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsCategoryQueryVo getYxCouponsCategoryById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponsCategoryQueryParam
     * @return
     */
    Paging<YxCouponsCategoryQueryVo> getYxCouponsCategoryPageList(YxCouponsCategoryQueryParam yxCouponsCategoryQueryParam) throws Exception;

}
