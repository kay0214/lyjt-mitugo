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
        log.info("---------------------优惠券过期取消订单开始---------------------");
        List<YxCoupons> yxCoupons = this.yxCouponsService.list(new QueryWrapper<YxCoupons>().lambda().le(YxCoupons::getExpireDateEnd, LocalDateTime.now()));
        if (null == yxCoupons || yxCoupons.size() <= 0) {
            return;
        }
        for (YxCoupons item : yxCoupons) {
            log.info("卡券id：" + item.getId() + "卡券名称：" + item.getCouponName() + "开始处理过期订单...");
            LambdaQueryWrapper<YxCouponOrder> orderWapper = new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getCouponId, item.getId());
            // 0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回
            orderWapper.in(YxCouponOrder::getStatus, 0, 2, 3, 4, 5, 7);
            YxCouponOrder yxCouponOrder = new YxCouponOrder();
            yxCouponOrder.setStatus(1);
            yxCouponOrderService.update(yxCouponOrder, orderWapper);
            LambdaQueryWrapper<YxCouponOrderDetail> orderDetailWapper = new QueryWrapper<YxCouponOrderDetail>().lambda().eq(YxCouponOrderDetail::getCouponId, item.getId());
            orderDetailWapper.in(YxCouponOrderDetail::getStatus, 0, 2, 3, 4, 5, 7);
            YxCouponOrderDetail yxCouponOrderDetail = new YxCouponOrderDetail();
            yxCouponOrderDetail.setStatus(2);
            yxCouponOrderDetailService.update(yxCouponOrderDetail, orderDetailWapper);
            log.info("卡券id：" + item.getId() + "卡券名称：" + item.getCouponName() + "过期订单处理结束...");
        }
        log.info("---------------------优惠券过期取消订单结束---------------------");
    }
}
