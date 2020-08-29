package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.mapper.SystemUserMapper;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.manage.web.param.SystemUserQueryParam;
import co.yixiang.modules.manage.web.vo.SystemUserQueryVo;
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
 * 系统用户 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {

    @Autowired
    private SystemUserMapper userMapper;

    @Override
    public SystemUserQueryVo getUserById(Serializable id){
        return userMapper.getUserById(id);
    }

    @Override
    public Paging<SystemUserQueryVo> getUserPageList(SystemUserQueryParam userQueryParam) throws Exception{
        Page page = setPageParam(userQueryParam,OrderItem.desc("create_time"));
        IPage<SystemUserQueryVo> iPage = userMapper.getUserPageList(page,userQueryParam);
        return new Paging(iPage);
    }

}
