package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.UserAvatar;
import co.yixiang.modules.manage.mapper.UserAvatarMapper;
import co.yixiang.modules.manage.service.UserAvatarService;
import co.yixiang.modules.manage.web.param.UserAvatarQueryParam;
import co.yixiang.modules.manage.web.vo.UserAvatarQueryVo;
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
 * 系统用户头像 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAvatarServiceImpl extends BaseServiceImpl<UserAvatarMapper, UserAvatar> implements UserAvatarService {

    @Autowired
    private UserAvatarMapper userAvatarMapper;

    @Override
    public UserAvatarQueryVo getUserAvatarById(Serializable id) throws Exception{
        return userAvatarMapper.getUserAvatarById(id);
    }

    @Override
    public Paging<UserAvatarQueryVo> getUserAvatarPageList(UserAvatarQueryParam userAvatarQueryParam) throws Exception{
        Page page = setPageParam(userAvatarQueryParam,OrderItem.desc("create_time"));
        IPage<UserAvatarQueryVo> iPage = userAvatarMapper.getUserAvatarPageList(page,userAvatarQueryParam);
        return new Paging(iPage);
    }

}
