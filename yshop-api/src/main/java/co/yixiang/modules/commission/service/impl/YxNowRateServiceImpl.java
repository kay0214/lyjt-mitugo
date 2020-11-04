package co.yixiang.modules.commission.service.impl;

import co.yixiang.modules.commission.entity.YxNowRate;
import co.yixiang.modules.commission.mapper.YxNowRateMapper;
import co.yixiang.modules.commission.service.YxNowRateService;
import co.yixiang.modules.commission.web.param.YxNowRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxNowRateQueryVo;
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
 * 购买时费率记录表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxNowRateServiceImpl extends BaseServiceImpl<YxNowRateMapper, YxNowRate> implements YxNowRateService {

    @Autowired
    private YxNowRateMapper yxNowRateMapper;

    @Override
    public YxNowRateQueryVo getYxNowRateById(Serializable id) throws Exception{
        return yxNowRateMapper.getYxNowRateById(id);
    }

    @Override
    public Paging<YxNowRateQueryVo> getYxNowRatePageList(YxNowRateQueryParam yxNowRateQueryParam) throws Exception{
        Page page = setPageParam(yxNowRateQueryParam,OrderItem.desc("create_time"));
        IPage<YxNowRateQueryVo> iPage = yxNowRateMapper.getYxNowRatePageList(page,yxNowRateQueryParam);
        return new Paging(iPage);
    }

}
