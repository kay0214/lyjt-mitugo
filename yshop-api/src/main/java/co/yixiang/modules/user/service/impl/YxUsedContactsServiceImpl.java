package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.mapper.YxUsedContactsMapper;
import co.yixiang.modules.user.service.YxUsedContactsService;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
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
public class YxUsedContactsServiceImpl extends BaseServiceImpl<YxUsedContactsMapper, YxUsedContacts> implements YxUsedContactsService {

    @Autowired
    private YxUsedContactsMapper yxUsedContactsMapper;

    @Override
    public YxUsedContactsQueryVo getYxUsedContactsById(Serializable id) throws Exception{
        return yxUsedContactsMapper.getYxUsedContactsById(id);
    }

    @Override
    public Paging<YxUsedContactsQueryVo> getYxUsedContactsPageList(YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception{
        Page page = setPageParam(yxUsedContactsQueryParam,OrderItem.desc("create_time"));
        IPage<YxUsedContactsQueryVo> iPage = yxUsedContactsMapper.getYxUsedContactsPageList(page,yxUsedContactsQueryParam);
        return new Paging(iPage);
    }

}
