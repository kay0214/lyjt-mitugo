/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.quartz.task;

import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单创建超30分钟自动取消订单
 *
 * @author PC-LIUSHOUYI
 * @version CouponNoPayCancleTask, v0.1 2020/9/7 20:41
 */
@Component
@Slf4j
public class CouponNoPayCancleTask {

    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    public void run() {
        log.info("---------------------支付超时订单取消开始---------------------");
        // 待支付的订单创建时间小于当前时间减30分钟
        List<YxCouponOrder> list = this.yxCouponOrderService.list(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getStatus, 0).le(YxCouponOrder::getCreateTime, DateUtils.minusMinutes(LocalDateTime.now(), 30L)));
        for (YxCouponOrder item : list) {
            boolean result = this.yxCouponOrderService.updateCancelNoPayOrder(item);
            if (!result) {
                log.info("订单：" + item.getOrderId() + "支付超时取消失败");
            }
        }
        log.info("---------------------支付超时订单取消结束---------------------");
    }
}
