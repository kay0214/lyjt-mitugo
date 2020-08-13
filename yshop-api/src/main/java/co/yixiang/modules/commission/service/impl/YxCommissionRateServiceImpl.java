package co.yixiang.modules.commission.service.impl;

import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.mapper.YxCommissionRateMapper;
import co.yixiang.modules.commission.service.YxCommissionRateService;
import co.yixiang.modules.commission.web.param.YxCommissionRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCommissionRateQueryVo;
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
 * 分佣配置表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCommissionRateServiceImpl extends BaseServiceImpl<YxCommissionRateMapper, YxCommissionRate> implements YxCommissionRateService {

    @Autowired
    private YxCommissionRateMapper yxCommissionRateMapper;

    @Override
    public YxCommissionRateQueryVo getYxCommissionRateById(Serializable id) throws Exception{
        return yxCommissionRateMapper.getYxCommissionRateById(id);
    }

    @Override
    public Paging<YxCommissionRateQueryVo> getYxCommissionRatePageList(YxCommissionRateQueryParam yxCommissionRateQueryParam) throws Exception{
        Page page = setPageParam(yxCommissionRateQueryParam,OrderItem.desc("create_time"));
        IPage<YxCommissionRateQueryVo> iPage = yxCommissionRateMapper.getYxCommissionRatePageList(page,yxCommissionRateQueryParam);
        return new Paging(iPage);
    }

}
