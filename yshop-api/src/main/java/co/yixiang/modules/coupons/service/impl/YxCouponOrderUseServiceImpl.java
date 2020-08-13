package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.mapper.YxCouponOrderUseMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;
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
 * 用户地址表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderUseServiceImpl extends BaseServiceImpl<YxCouponOrderUseMapper, YxCouponOrderUse> implements YxCouponOrderUseService {

    @Autowired
    private YxCouponOrderUseMapper yxCouponOrderUseMapper;

    @Override
    public YxCouponOrderUseQueryVo getYxCouponOrderUseById(Serializable id) throws Exception{
        return yxCouponOrderUseMapper.getYxCouponOrderUseById(id);
    }

    @Override
    public Paging<YxCouponOrderUseQueryVo> getYxCouponOrderUsePageList(YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam) throws Exception{
        Page page = setPageParam(yxCouponOrderUseQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponOrderUseQueryVo> iPage = yxCouponOrderUseMapper.getYxCouponOrderUsePageList(page,yxCouponOrderUseQueryParam);
        return new Paging(iPage);
    }

}
