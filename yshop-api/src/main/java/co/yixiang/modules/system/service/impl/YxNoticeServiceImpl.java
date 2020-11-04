package co.yixiang.modules.system.service.impl;

import co.yixiang.modules.system.entity.YxNotice;
import co.yixiang.modules.system.mapper.YxNoticeMapper;
import co.yixiang.modules.system.service.YxNoticeService;
import co.yixiang.modules.system.web.param.YxNoticeQueryParam;
import co.yixiang.modules.system.web.vo.YxNoticeQueryVo;
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
 * 公告表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxNoticeServiceImpl extends BaseServiceImpl<YxNoticeMapper, YxNotice> implements YxNoticeService {

    @Autowired
    private YxNoticeMapper yxNoticeMapper;

    @Override
    public YxNoticeQueryVo getYxNoticeById(Serializable id) throws Exception{
        return yxNoticeMapper.getYxNoticeById(id);
    }

    @Override
    public Paging<YxNoticeQueryVo> getYxNoticePageList(YxNoticeQueryParam yxNoticeQueryParam) throws Exception{
        Page page = setPageParam(yxNoticeQueryParam,OrderItem.desc("create_time"));
        IPage<YxNoticeQueryVo> iPage = yxNoticeMapper.getYxNoticePageList(page,yxNoticeQueryParam);
        return new Paging(iPage);
    }

}
