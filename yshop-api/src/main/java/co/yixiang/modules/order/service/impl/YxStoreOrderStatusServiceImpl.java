/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.order.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.order.entity.YxStoreOrderStatus;
import co.yixiang.modules.order.mapper.YxStoreOrderStatusMapper;
import co.yixiang.modules.order.service.YxStoreOrderStatusService;
import co.yixiang.utils.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单操作记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreOrderStatusServiceImpl extends BaseServiceImpl<YxStoreOrderStatusMapper, YxStoreOrderStatus> implements YxStoreOrderStatusService {

    private final YxStoreOrderStatusMapper yxStoreOrderStatusMapper;

    @Override
    public void create(int oid, String changetype, String changeMessage) {
        YxStoreOrderStatus storeOrderStatus = new YxStoreOrderStatus();
        storeOrderStatus.setOid(oid);
        storeOrderStatus.setChangeType(changetype);
        storeOrderStatus.setChangeMessage(changeMessage);
        storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

        yxStoreOrderStatusMapper.insert(storeOrderStatus);
    }
}
