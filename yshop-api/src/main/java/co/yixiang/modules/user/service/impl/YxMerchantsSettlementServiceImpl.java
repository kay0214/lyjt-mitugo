package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxMerchantsSettlement;
import co.yixiang.modules.user.mapper.YxMerchantsSettlementMapper;
import co.yixiang.modules.user.service.YxMerchantsSettlementService;
import co.yixiang.modules.user.web.param.YxMerchantsSettlementQueryParam;
import co.yixiang.modules.user.web.vo.YxMerchantsSettlementQueryVo;
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
 * 商家入驻表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxMerchantsSettlementServiceImpl extends BaseServiceImpl<YxMerchantsSettlementMapper, YxMerchantsSettlement> implements YxMerchantsSettlementService {

    @Autowired
    private YxMerchantsSettlementMapper yxMerchantsSettlementMapper;

    @Override
    public YxMerchantsSettlementQueryVo getYxMerchantsSettlementById(Serializable id) throws Exception{
        return yxMerchantsSettlementMapper.getYxMerchantsSettlementById(id);
    }

    @Override
    public Paging<YxMerchantsSettlementQueryVo> getYxMerchantsSettlementPageList(YxMerchantsSettlementQueryParam yxMerchantsSettlementQueryParam) throws Exception{
        Page page = setPageParam(yxMerchantsSettlementQueryParam,OrderItem.desc("create_time"));
        IPage<YxMerchantsSettlementQueryVo> iPage = yxMerchantsSettlementMapper.getYxMerchantsSettlementPageList(page,yxMerchantsSettlementQueryParam);
        return new Paging(iPage);
    }

}
