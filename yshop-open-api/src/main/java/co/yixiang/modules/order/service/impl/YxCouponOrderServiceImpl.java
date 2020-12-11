package co.yixiang.modules.order.service.impl;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.service.impl.BaseOpenServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.order.entity.YxCouponOrder;
import co.yixiang.modules.order.mapper.YxCouponOrderMapper;
import co.yixiang.modules.order.service.YxCouponOrderService;
import co.yixiang.modules.order.web.param.OrderStatusQueryParam;
import co.yixiang.modules.order.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.order.web.vo.YxCouponOrderQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 卡券订单表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderServiceImpl extends BaseOpenServiceImpl<YxCouponOrderMapper, YxCouponOrder> implements YxCouponOrderService {

    @Autowired
    private YxCouponOrderMapper yxCouponOrderMapper;

    @Override
    public YxCouponOrderQueryVo getYxCouponOrderById(Serializable id) throws Exception{
        return yxCouponOrderMapper.getYxCouponOrderById(id);
    }

    @Override
    public Paging<YxCouponOrderQueryVo> getYxCouponOrderPageList(YxCouponOrderQueryParam yxCouponOrderQueryParam) throws Exception{
        Page page = setPageParam(yxCouponOrderQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponOrderQueryVo> iPage = yxCouponOrderMapper.getYxCouponOrderPageList(page,yxCouponOrderQueryParam);
        return new Paging(iPage);
    }

    @Override
    public ApiResult selectByOrderId(OrderStatusQueryParam idParam) {
        //验签
        checkAuth(idParam);
        YxCouponOrder yxCouponOrder = yxCouponOrderMapper.selectOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId,idParam.getOrderId()));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("orderId",yxCouponOrder.getOrderId());
        resultMap.put("status",yxCouponOrder.getStatus());
        return ApiResult.ok(resultMap);
    }

}
