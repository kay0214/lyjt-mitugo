package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxPointDetail;
import co.yixiang.modules.user.mapper.YxPointDetailMapper;
import co.yixiang.modules.user.service.YxPointDetailService;
import co.yixiang.modules.user.web.param.YxPointDetailQueryParam;
import co.yixiang.modules.user.web.vo.YxPointDetailQueryVo;
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
 * 积分获取明细 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxPointDetailServiceImpl extends BaseServiceImpl<YxPointDetailMapper, YxPointDetail> implements YxPointDetailService {

    @Autowired
    private YxPointDetailMapper yxPointDetailMapper;

    @Override
    public YxPointDetailQueryVo getYxPointDetailById(Serializable id) throws Exception{
        return yxPointDetailMapper.getYxPointDetailById(id);
    }

    @Override
    public Paging<YxPointDetailQueryVo> getYxPointDetailPageList(YxPointDetailQueryParam yxPointDetailQueryParam) throws Exception{
        Page page = setPageParam(yxPointDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxPointDetailQueryVo> iPage = yxPointDetailMapper.getYxPointDetailPageList(page,yxPointDetailQueryParam);
        return new Paging(iPage);
    }

}
