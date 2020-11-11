package co.yixiang.modules.contract.service.impl;

import co.yixiang.modules.contract.entity.YxSignInfo;
import co.yixiang.modules.contract.mapper.YxSignInfoMapper;
import co.yixiang.modules.contract.service.YxSignInfoService;
import co.yixiang.modules.contract.web.param.YxSignInfoQueryParam;
import co.yixiang.modules.contract.web.vo.YxSignInfoQueryVo;
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
 * 签章信息表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxSignInfoServiceImpl extends BaseServiceImpl<YxSignInfoMapper, YxSignInfo> implements YxSignInfoService {

    @Autowired
    private YxSignInfoMapper yxSignInfoMapper;

    @Override
    public YxSignInfoQueryVo getYxSignInfoById(Serializable id) throws Exception{
        return yxSignInfoMapper.getYxSignInfoById(id);
    }

    @Override
    public Paging<YxSignInfoQueryVo> getYxSignInfoPageList(YxSignInfoQueryParam yxSignInfoQueryParam) throws Exception{
        Page page = setPageParam(yxSignInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxSignInfoQueryVo> iPage = yxSignInfoMapper.getYxSignInfoPageList(page,yxSignInfoQueryParam);
        return new Paging(iPage);
    }

}
