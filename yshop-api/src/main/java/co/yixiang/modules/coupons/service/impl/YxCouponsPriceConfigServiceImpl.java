package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponsPriceConfig;
import co.yixiang.modules.coupons.mapper.YxCouponsPriceConfigMapper;
import co.yixiang.modules.coupons.service.YxCouponsPriceConfigService;
import co.yixiang.modules.coupons.web.param.YxCouponsPriceConfigQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsPriceConfigQueryVo;
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
 * 价格配置 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsPriceConfigServiceImpl extends BaseServiceImpl<YxCouponsPriceConfigMapper, YxCouponsPriceConfig> implements YxCouponsPriceConfigService {

    @Autowired
    private YxCouponsPriceConfigMapper yxCouponsPriceConfigMapper;

    @Override
    public YxCouponsPriceConfigQueryVo getYxCouponsPriceConfigById(Serializable id) throws Exception{
        return yxCouponsPriceConfigMapper.getYxCouponsPriceConfigById(id);
    }

    @Override
    public Paging<YxCouponsPriceConfigQueryVo> getYxCouponsPriceConfigPageList(YxCouponsPriceConfigQueryParam yxCouponsPriceConfigQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsPriceConfigQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsPriceConfigQueryVo> iPage = yxCouponsPriceConfigMapper.getYxCouponsPriceConfigPageList(page,yxCouponsPriceConfigQueryParam);
        return new Paging(iPage);
    }

}
