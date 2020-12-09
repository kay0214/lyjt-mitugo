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
        yxCouponOrderService.updateOrderAutoCancel();
    }
}
