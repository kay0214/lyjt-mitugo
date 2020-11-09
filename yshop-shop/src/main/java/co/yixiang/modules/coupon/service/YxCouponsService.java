/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupon.domain.CouponAddRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRateRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRequest;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liusy
* @date 2020-08-31
*/
public interface YxCouponsService  extends BaseService<YxCoupons>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxCouponsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxCouponsDto>
    */
    List<YxCoupons> queryAll(YxCouponsQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxCouponsDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据核销码查询卡券信息
     *
     * @param verifyCode
     * @param uid
     * @return
     */
    YxCouponsDto getCouponByVerifyCode(String verifyCode, int uid);

    /**
     * 更新卡券销量
     *
     * @param couponId
     * @param totalNum
     * @return
     */
    boolean updateCancelNoPayOrder(Integer couponId, Integer totalNum);

    /**
     * 新增卡券处理
     *
     * @param request
     * @return
     */
    boolean createCoupons(CouponAddRequest request);

    /**
     * 缩略图操作
     *
     * @param typeId
     * @param video
     * @param imgPath
     * @param sliderPath
     * @param loginUserId
     */
    void couponImg(Integer typeId,String video, String imgPath, String sliderPath, Integer loginUserId);

    /**
     * 更新卡券
     *
     * @param request
     * @return
     */
    boolean updateCoupons(CouponModifyRequest request);

    /**
     * 修改分佣比例
     *
     * @param request
     */
    void updateRate(CouponModifyRateRequest request);
}
