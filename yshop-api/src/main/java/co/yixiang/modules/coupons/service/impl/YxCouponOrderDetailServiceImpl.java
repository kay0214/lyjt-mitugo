package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponOrderDetail;
import co.yixiang.modules.coupons.mapper.YxCouponOrderDetailMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderDetailQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderDetailQueryVo;
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
 * 卡券订单详情表 服务实现类
 * </p>
 *
 * @author liusy
 * @since 2020-08-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderDetailServiceImpl extends BaseServiceImpl<YxCouponOrderDetailMapper, YxCouponOrderDetail> implements YxCouponOrderDetailService {

    @Autowired
    private YxCouponOrderDetailMapper yxCouponOrderDetailMapper;

    @Override
    public YxCouponOrderDetailQueryVo getYxCouponOrderDetailById(Serializable id) throws Exception{
        return yxCouponOrderDetailMapper.getYxCouponOrderDetailById(id);
    }

    @Override
    public Paging<YxCouponOrderDetailQueryVo> getYxCouponOrderDetailPageList(YxCouponOrderDetailQueryParam yxCouponOrderDetailQueryParam) throws Exception{
        Page page = setPageParam(yxCouponOrderDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponOrderDetailQueryVo> iPage = yxCouponOrderDetailMapper.getYxCouponOrderDetailPageList(page,yxCouponOrderDetailQueryParam);
        return new Paging(iPage);
    }

}
