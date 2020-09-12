/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service.impl;

import co.yixiang.common.rocketmq.service.WechatNotifyService;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.service.YxUserRechargeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version WechatNotifyServiceImpl, v0.1 2020/9/11 17:12
 */
@Service
public class WechatNotifyServiceImpl implements WechatNotifyService {

    @Autowired
    private YxStoreOrderService orderService;
    @Autowired
    private YxUserRechargeService userRechargeService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    /**
     * 处理微信异步回调
     *
     * @param orderId
     * @param attach
     */
    @Override
    public void updateNotifyStatus(String orderId, String attach) {
        if (BillDetailEnum.TYPE_3.getValue().equals(attach)) {
            // 商品购买
            List<YxStoreOrderQueryVo> orderList = orderService.getOrderInfoList(orderId, 0);
            if (CollectionUtils.isEmpty(orderList)) {
                return;
            }
            if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(orderList.get(0).getPaid())) {
                return;
            }
            orderService.paySuccessNew(orderList.get(0).getPaymentNo(), "weixin");
        } else if (BillDetailEnum.TYPE_1.getValue().equals(attach)) {
            //处理充值
            YxUserRecharge userRecharge = userRechargeService.getInfoByOrderId(orderId);
            if (userRecharge == null) {
                return;
            }
            if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(userRecharge.getPaid())) {
                return;
            }
            userRechargeService.updateRecharge(userRecharge);
        } else if (BillDetailEnum.TYPE_8.getValue().equals(attach)) {
            // 本地生活购买
            YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().eq("order_id", orderId));
            if (yxCouponOrder == null) {
                return;
            }
            if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(yxCouponOrder.getPayStaus())) {
                return;
            }
            yxCouponOrder.setPayType("weixin");
            yxCouponOrder.setIsChannel(1);
            yxCouponOrderService.updatePaySuccess(yxCouponOrder);
        }
    }
}
