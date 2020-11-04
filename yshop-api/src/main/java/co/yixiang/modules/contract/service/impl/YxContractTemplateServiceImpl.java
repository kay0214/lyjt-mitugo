package co.yixiang.modules.contract.service.impl;

import co.yixiang.modules.contract.entity.YxContractTemplate;
import co.yixiang.modules.contract.mapper.YxContractTemplateMapper;
import co.yixiang.modules.contract.service.YxContractTemplateService;
import co.yixiang.modules.contract.web.param.YxContractTemplateQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
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
 * 合同模板 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxContractTemplateServiceImpl extends BaseServiceImpl<YxContractTemplateMapper, YxContractTemplate> implements YxContractTemplateService {

    @Autowired
    private YxContractTemplateMapper yxContractTemplateMapper;

    @Override
    public YxContractTemplateQueryVo getYxContractTemplateById(Serializable id) throws Exception{
        return yxContractTemplateMapper.getYxContractTemplateById(id);
    }

    @Override
    public Paging<YxContractTemplateQueryVo> getYxContractTemplatePageList(YxContractTemplateQueryParam yxContractTemplateQueryParam) throws Exception{
        Page page = setPageParam(yxContractTemplateQueryParam,OrderItem.desc("create_time"));
        IPage<YxContractTemplateQueryVo> iPage = yxContractTemplateMapper.getYxContractTemplatePageList(page,yxContractTemplateQueryParam);
        return new Paging(iPage);
    }

}
