package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponsPriceConfigQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsPriceConfigQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 价格配置 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxCouponsPriceConfigService extends BaseService<YxCouponsPriceConfig> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsPriceConfigQueryVo getYxCouponsPriceConfigById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponsPriceConfigQueryParam
     * @return
     */
    Paging<YxCouponsPriceConfigQueryVo> getYxCouponsPriceConfigPageList(YxCouponsPriceConfigQueryParam yxCouponsPriceConfigQueryParam) throws Exception;

}
