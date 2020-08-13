package co.yixiang.modules.funds.service.impl;

import co.yixiang.modules.funds.entity.YxFundsAccount;
import co.yixiang.modules.funds.mapper.YxFundsAccountMapper;
import co.yixiang.modules.funds.service.YxFundsAccountService;
import co.yixiang.modules.funds.web.param.YxFundsAccountQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsAccountQueryVo;
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
 * 平台账户表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxFundsAccountServiceImpl extends BaseServiceImpl<YxFundsAccountMapper, YxFundsAccount> implements YxFundsAccountService {

    @Autowired
    private YxFundsAccountMapper yxFundsAccountMapper;

    @Override
    public YxFundsAccountQueryVo getYxFundsAccountById(Serializable id) throws Exception{
        return yxFundsAccountMapper.getYxFundsAccountById(id);
    }

    @Override
    public Paging<YxFundsAccountQueryVo> getYxFundsAccountPageList(YxFundsAccountQueryParam yxFundsAccountQueryParam) throws Exception{
        Page page = setPageParam(yxFundsAccountQueryParam,OrderItem.desc("create_time"));
        IPage<YxFundsAccountQueryVo> iPage = yxFundsAccountMapper.getYxFundsAccountPageList(page,yxFundsAccountQueryParam);
        return new Paging(iPage);
    }

}
