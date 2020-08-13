package co.yixiang.modules.funds.service.impl;

import co.yixiang.modules.funds.entity.YxFundsDetail;
import co.yixiang.modules.funds.mapper.YxFundsDetailMapper;
import co.yixiang.modules.funds.service.YxFundsDetailService;
import co.yixiang.modules.funds.web.param.YxFundsDetailQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsDetailQueryVo;
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
 * 平台资金明细 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxFundsDetailServiceImpl extends BaseServiceImpl<YxFundsDetailMapper, YxFundsDetail> implements YxFundsDetailService {

    @Autowired
    private YxFundsDetailMapper yxFundsDetailMapper;

    @Override
    public YxFundsDetailQueryVo getYxFundsDetailById(Serializable id) throws Exception{
        return yxFundsDetailMapper.getYxFundsDetailById(id);
    }

    @Override
    public Paging<YxFundsDetailQueryVo> getYxFundsDetailPageList(YxFundsDetailQueryParam yxFundsDetailQueryParam) throws Exception{
        Page page = setPageParam(yxFundsDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxFundsDetailQueryVo> iPage = yxFundsDetailMapper.getYxFundsDetailPageList(page,yxFundsDetailQueryParam);
        return new Paging(iPage);
    }

}
