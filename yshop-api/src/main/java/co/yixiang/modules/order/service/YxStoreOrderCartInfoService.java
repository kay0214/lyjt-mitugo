/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.order.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.order.entity.YxStoreOrderCartInfo;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;

import java.util.List;

/**
 * <p>
 * 订单购物详情表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreOrderCartInfoService extends BaseService<YxStoreOrderCartInfo> {

    void saveCartInfo(Integer oid, List<YxStoreCartQueryVo> cartInfo);

    YxStoreOrderCartInfo findByUni(String unique);


}
