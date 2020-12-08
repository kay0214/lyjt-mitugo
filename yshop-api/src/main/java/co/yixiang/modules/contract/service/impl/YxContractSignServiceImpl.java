package co.yixiang.modules.contract.service.impl;

import co.yixiang.modules.contract.entity.YxContractSign;
import co.yixiang.modules.contract.mapper.YxContractSignMapper;
import co.yixiang.modules.contract.service.YxContractSignService;
import co.yixiang.modules.contract.web.param.YxContractSignQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractSignQueryVo;
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
 * 合同签署表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxContractSignServiceImpl extends BaseServiceImpl<YxContractSignMapper, YxContractSign> implements YxContractSignService {

    @Autowired
    private YxContractSignMapper yxContractSignMapper;

    @Override
    public YxContractSignQueryVo getYxContractSignById(Serializable id) throws Exception{
        return yxContractSignMapper.getYxContractSignById(id);
    }

    @Override
    public Paging<YxContractSignQueryVo> getYxContractSignPageList(YxContractSignQueryParam yxContractSignQueryParam) throws Exception{
        Page page = setPageParam(yxContractSignQueryParam,OrderItem.desc("create_time"));
        IPage<YxContractSignQueryVo> iPage = yxContractSignMapper.getYxContractSignPageList(page,yxContractSignQueryParam);
        return new Paging(iPage);
    }

}
