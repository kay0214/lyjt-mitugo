package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.mapper.YxLeaveMessageMapper;
import co.yixiang.modules.user.service.YxLeaveMessageService;
import co.yixiang.modules.user.web.param.YxLeaveMessageQueryParam;
import co.yixiang.modules.user.web.vo.YxLeaveMessageQueryVo;
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
 * 常用联系人表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxLeaveMessageServiceImpl extends BaseServiceImpl<YxLeaveMessageMapper, YxLeaveMessage> implements YxLeaveMessageService {

    @Autowired
    private YxLeaveMessageMapper yxLeaveMessageMapper;

    @Override
    public YxLeaveMessageQueryVo getYxLeaveMessageById(Serializable id) throws Exception{
        return yxLeaveMessageMapper.getYxLeaveMessageById(id);
    }

    @Override
    public Paging<YxLeaveMessageQueryVo> getYxLeaveMessagePageList(YxLeaveMessageQueryParam yxLeaveMessageQueryParam) throws Exception{
        Page page = setPageParam(yxLeaveMessageQueryParam,OrderItem.desc("create_time"));
        IPage<YxLeaveMessageQueryVo> iPage = yxLeaveMessageMapper.getYxLeaveMessagePageList(page,yxLeaveMessageQueryParam);
        return new Paging(iPage);
    }

}
