package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.manage.mapper.UsersRolesMapper;
import co.yixiang.modules.manage.service.UsersRolesService;
import co.yixiang.modules.manage.web.param.UsersRolesQueryParam;
import co.yixiang.modules.manage.web.vo.UsersRolesQueryVo;
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
 * 用户角色关联 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UsersRolesServiceImpl extends BaseServiceImpl<UsersRolesMapper, UsersRoles> implements UsersRolesService {

    @Autowired
    private UsersRolesMapper usersRolesMapper;

    @Override
    public UsersRolesQueryVo getUsersRolesById(Serializable id) throws Exception{
        return usersRolesMapper.getUsersRolesById(id);
    }

    @Override
    public Paging<UsersRolesQueryVo> getUsersRolesPageList(UsersRolesQueryParam usersRolesQueryParam) throws Exception{
        Page page = setPageParam(usersRolesQueryParam,OrderItem.desc("create_time"));
        IPage<UsersRolesQueryVo> iPage = usersRolesMapper.getUsersRolesPageList(page,usersRolesQueryParam);
        return new Paging(iPage);
    }

}
