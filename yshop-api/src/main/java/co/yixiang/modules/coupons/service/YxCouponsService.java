package co.yixiang.modules.coupons.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.YxCouponsDto;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveCouponsVo;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.manage.entity.SystemUser;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 本地生活, 卡券表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxCouponsService extends BaseService<YxCoupons> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsQueryVo getYxCouponsById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponsQueryParam
     * @return
     */
    Paging<YxCouponsQueryVo> getYxCouponsPageList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception;

    /**
     * 本地生活卡券热销榜单
     * @param yxCouponsQueryParam
     * @return
     * @throws Exception
     */
    Paging<YxCouponsQueryVo> getCouponsHotList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception;

    /**
     * 根据商户id获取卡券信息
     * @param storeId
     * @return
     */
    public List<YxCouponsQueryVo> getCouponsInfoByStoreId(int storeId);

    /**
     * 通过店铺ID获取店铺卡券
     * @param id
     * @return
     */
    List<LocalLiveCouponsVo> getCouponsLitByBelog(int id);

    /**
     * 根据ID查询卡券信息
     * @param id
     * @return
     */
    YxCoupons getCouponsById(Integer id);

    Paging<YxCouponsQueryVo> getYxCouponsPageListByStoreId(YxCouponsQueryParam yxCouponsQueryParam);

    /**
     * 查询详情
     *
     * @param id
     * @param keyword
     * @return
     */
    List<LocalLiveCouponsVo> getCouponsListByPram(Integer id, String keyword);

    /**
     * 根据核销码查询卡券信息
     *
     * @param decodeVerifyCode
     * @return
     */
    YxCouponsDto getCouponByVerifyCode(String decodeVerifyCode, SystemUser user);

    /**
     *
     *
     * @param orderId
     * @return
     */
    YxCouponsDto getCouponByOrderId(String orderId, SystemUser user);

    /**
     * 增加销量
     *
     * @param couponId
     * @param sales
     */
    void updateAddSales(Integer couponId, Integer sales);

    /**
     * 扣减销量
     *
     * @param couponId
     * @param sales
     */
    void updateMulSales(Integer couponId,Integer sales);

    /**
     * 扫码获取船票订单信息
     * @param decodeVerifyCode
     * @return
     */
    YxCouponsDto getShipCouponInfo(String decodeVerifyCode, SystemUser user);

    /**
     * 获取卡券的价格配置（当前日期符合）
     * @param couponId
     * @return
     */
    YxCouponsPriceConfig getPirceConfig(int couponId);
}
