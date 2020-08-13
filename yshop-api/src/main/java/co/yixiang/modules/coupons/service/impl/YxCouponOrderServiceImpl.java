package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 卡券订单表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderServiceImpl extends BaseServiceImpl<YxCouponOrderMapper, YxCouponOrder> implements YxCouponOrderService {

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

}
