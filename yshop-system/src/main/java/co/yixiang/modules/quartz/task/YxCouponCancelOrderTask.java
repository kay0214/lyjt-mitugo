/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.quartz.task;

import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.domain.YxCouponOrderDetail;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
import co.yixiang.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 优惠券订单batch
 *
 * @author PC-LIUSHOUYI
 * @version YxCouponOrderTask, v0.1 2020/9/7 19:48
 */
@Component
@Slf4j
public class YxCouponCancelOrderTask {

    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxCouponOrderDetailService yxCouponOrderDetailService;

    public void run() {
        // 获取待使用和未使用的、当前时间已过期的订单
        List<YxCouponOrder> list = this.yxCouponOrderService.list(new QueryWrapper<YxCouponOrder>().lambda()
                .in(YxCouponOrder::getStatus, 0, 2, 4, 5, 7)
                .gt(YxCouponOrder::getOutTime, 0)
                .lt(YxCouponOrder::getOutTime, DateUtils.getNowTime()));
        for (YxCouponOrder item : list) {
            YxCoupons yxCoupons = this.yxCouponsService.getById(item.getCouponId());
            if (null == yxCoupons) {
                // 卡券查询不到的默认按不支持过期退处理
                yxCoupons = new YxCoupons();
                yxCoupons.setAwaysRefund(0);
                yxCoupons.setOuttimeRefund(0);
            }
            // 先判断下卡券是否支持过期退和随时退、支持的话把所有未使用的订单退款
            if (1 == yxCoupons.getAwaysRefund() || 1 == yxCoupons.getOuttimeRefund()) {
                if (4 == item.getStatus()) {
                    // 支持过期退和随时退的、未使用的订单全额退款
                    YxCouponOrderDto resources = new YxCouponOrderDto();
                    resources.setId(item.getId());
                    resources.setRefundStatus(2);
                    resources.setRefundPrice(item.getTotalPrice());
                    yxCouponOrderService.refund(resources);
                }
            }
            // 剩下未退款的更新成已过期
            item.setStatus(1);
            yxCouponOrderService.updateById(item);
            LambdaQueryWrapper<YxCouponOrderDetail> orderDetailWrapper = new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getOrderId, item.getOrderId());
            YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
            yxCouponOrderDetail.setStatus(1);
            yxCouponOrderDetailService.update(yxCouponOrderDetail, orderDetailWrapper);
        }
    }
}
