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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
//        log.info("---------------------优惠券过期取消订单开始---------------------");
        List<YxCoupons> list = this.yxCouponsService.list(new QueryWrapper<YxCoupons>().lambda().le(YxCoupons::getExpireDateEnd, LocalDateTime.now()).eq(YxCoupons::getIsShow, 1));
        if (null == list || list.size() <= 0) {
            return;
        }
        for (YxCoupons item : list) {
//            log.info("卡券id：" + item.getId() + "卡券名称：" + item.getCouponName() + "开始处理过期订单...");
            // 先判断下卡券是否支持过期退和随时退、支持的话把所有未使用的订单退款
            if (0 == item.getAwaysRefund() || 0 == item.getOuttimeRefund()) {
                // 支持过期退和随时退的、未使用的订单全额退款
                LambdaQueryWrapper<YxCouponOrder> refundWrapper = new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getCouponId, item.getId());
                refundWrapper.eq(YxCouponOrder::getStatus, 4);
                List<YxCouponOrder> yxCouponOrderList = this.yxCouponOrderService.list(refundWrapper);
                if (null != yxCouponOrderList && yxCouponOrderList.size() > 0) {
                    for (YxCouponOrder refundItem : yxCouponOrderList) {
                        YxCouponOrderDto resources = new YxCouponOrderDto();
                        resources.setId(refundItem.getId());
                        resources.setRefundStatus(2);
                        resources.setRefundPrice(refundItem.getTotalPrice());
                        yxCouponOrderService.refund(resources);
                    }
                }
            }
            // 剩下未退款的更新成已过期
            LambdaQueryWrapper<YxCouponOrder> orderWapper = new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getCouponId, item.getId());
            // 0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回
            orderWapper.in(YxCouponOrder::getStatus, 0, 2, 3, 4, 5, 7);
            YxCouponOrder yxCouponOrder = new YxCouponOrder();
            yxCouponOrder.setStatus(1);
            yxCouponOrderService.update(yxCouponOrder, orderWapper);
            LambdaQueryWrapper<YxCouponOrderDetail> orderDetailWapper = new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getCouponId, item.getId());
            orderDetailWapper.in(YxCouponOrderDetail::getStatus, 0, 2, 3, 4, 5, 7);
            YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
            yxCouponOrderDetail.setStatus(1);
            yxCouponOrderDetailService.update(yxCouponOrderDetail, orderDetailWapper);
            YxCoupons yxCoupons = new YxCoupons();
            yxCoupons.setIsShow(0);
            yxCoupons.setId(item.getId());
            this.yxCouponsService.updateById(yxCoupons);
//            log.info("卡券id：" + item.getId() + "卡券名称：" + item.getCouponName() + "过期订单处理结束...");
        }
//        log.info("---------------------优惠券过期取消订单结束---------------------");
    }
}
