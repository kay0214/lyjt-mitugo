package co.yixiang.modules.system.service.impl;

import co.yixiang.modules.system.entity.YxHotConfig;
import co.yixiang.modules.system.mapper.YxHotConfigMapper;
import co.yixiang.modules.system.service.YxHotConfigService;
import co.yixiang.modules.system.web.param.YxHotConfigQueryParam;
import co.yixiang.modules.system.web.vo.YxHotConfigQueryVo;
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
 * HOT配置表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxHotConfigServiceImpl extends BaseServiceImpl<YxHotConfigMapper, YxHotConfig> implements YxHotConfigService {

    @Autowired
    private YxHotConfigMapper yxHotConfigMapper;

    @Override
    public YxHotConfigQueryVo getYxHotConfigById(Serializable id) throws Exception{
        return yxHotConfigMapper.getYxHotConfigById(id);
    }

    @Override
    public Paging<YxHotConfigQueryVo> getYxHotConfigPageList(YxHotConfigQueryParam yxHotConfigQueryParam) throws Exception{
        Page page = setPageParam(yxHotConfigQueryParam,OrderItem.desc("create_time"));
        IPage<YxHotConfigQueryVo> iPage = yxHotConfigMapper.getYxHotConfigPageList(page,yxHotConfigQueryParam);
        return new Paging(iPage);
    }

}
