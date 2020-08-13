package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponsCategory;
import co.yixiang.modules.coupons.mapper.YxCouponsCategoryMapper;
import co.yixiang.modules.coupons.service.YxCouponsCategoryService;
import co.yixiang.modules.coupons.web.param.YxCouponsCategoryQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsCategoryQueryVo;
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
 * 本地生活, 卡券分类表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsCategoryServiceImpl extends BaseServiceImpl<YxCouponsCategoryMapper, YxCouponsCategory> implements YxCouponsCategoryService {

    @Autowired
    private YxCouponsCategoryMapper yxCouponsCategoryMapper;

    @Override
    public YxCouponsCategoryQueryVo getYxCouponsCategoryById(Serializable id) throws Exception{
        return yxCouponsCategoryMapper.getYxCouponsCategoryById(id);
    }

    @Override
    public Paging<YxCouponsCategoryQueryVo> getYxCouponsCategoryPageList(YxCouponsCategoryQueryParam yxCouponsCategoryQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsCategoryQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsCategoryQueryVo> iPage = yxCouponsCategoryMapper.getYxCouponsCategoryPageList(page,yxCouponsCategoryQueryParam);
        return new Paging(iPage);
    }

}
