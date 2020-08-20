package co.yixiang.modules.coupons.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.CommonEnum;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.shop.mapping.YxCouponsMap;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 本地生活, 卡券表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsServiceImpl extends BaseServiceImpl<YxCouponsMapper, YxCoupons> implements YxCouponsService {

    @Autowired
    private YxCouponsMapper yxCouponsMapper;
    @Autowired
    private YxCouponsMap yxCouponsMap;
    @Override
    public YxCouponsQueryVo getYxCouponsById(Serializable id) throws Exception{
        return yxCouponsMapper.getYxCouponsById(id);
    }

    @Override
    public Paging<YxCouponsQueryVo> getYxCouponsPageList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsQueryVo> iPage = yxCouponsMapper.getYxCouponsPageList(page,yxCouponsQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据商户id获取卡券信息
     * @param merId
     * @return
     */
    @Override
    public List<YxCouponsQueryVo> getCouponsInfoByMerId(int merId){
        QueryWrapper<YxCoupons> wrapper = new QueryWrapper<YxCoupons>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("is_show",1).eq("create_user_id",merId);
        List<YxCoupons> storeProductList =  this.list(wrapper);
        List<YxCouponsQueryVo> queryVoList = yxCouponsMap.toDto(storeProductList);
        return queryVoList;
    }
}
