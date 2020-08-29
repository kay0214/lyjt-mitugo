package co.yixiang.modules.quartz.task;

import co.yixiang.modules.shop.service.YxStoreOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 订单取消定时任务
 * @date 2018-12-25
 */
@Component
@Slf4j
public class CancelOrderTask {

    @Autowired
    private YxStoreOrderService orderService;
        public void run() {
            log.info("---------------------订单取消定时任务开始---------------------");
            orderService.cancelOrder();
        }
}
