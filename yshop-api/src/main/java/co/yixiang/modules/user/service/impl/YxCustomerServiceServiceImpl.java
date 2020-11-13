package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxCustomerService;
import co.yixiang.modules.user.mapper.YxCustomerServiceMapper;
import co.yixiang.modules.user.service.YxCustomerServiceService;
import co.yixiang.modules.user.web.param.YxCustomerServiceQueryParam;
import co.yixiang.modules.user.web.vo.YxCustomerServiceQueryVo;
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
 * 机器人客服表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCustomerServiceServiceImpl extends BaseServiceImpl<YxCustomerServiceMapper, YxCustomerService> implements YxCustomerServiceService {

    @Autowired
    private YxCustomerServiceMapper yxCustomerServiceMapper;

    @Override
    public YxCustomerServiceQueryVo getYxCustomerServiceById(Serializable id) throws Exception{
        return yxCustomerServiceMapper.getYxCustomerServiceById(id);
    }

    @Override
    public Paging<YxCustomerServiceQueryVo> getYxCustomerServicePageList(YxCustomerServiceQueryParam yxCustomerServiceQueryParam) throws Exception{
        Page page = setPageParam(yxCustomerServiceQueryParam,OrderItem.asc("sort"));
        IPage<YxCustomerServiceQueryVo> iPage = yxCustomerServiceMapper.getYxCustomerServicePageList(page,yxCustomerServiceQueryParam);
        return new Paging(iPage);
    }

}
