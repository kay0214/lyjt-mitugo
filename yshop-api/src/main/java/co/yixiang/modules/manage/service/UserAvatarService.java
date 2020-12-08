package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.UserAvatar;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.UserAvatarQueryParam;
import co.yixiang.modules.manage.web.vo.UserAvatarQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 系统用户头像 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-18
 */
public interface UserAvatarService extends BaseService<UserAvatar> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UserAvatarQueryVo getUserAvatarById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param userAvatarQueryParam
     * @return
     */
    Paging<UserAvatarQueryVo> getUserAvatarPageList(UserAvatarQueryParam userAvatarQueryParam) throws Exception;

}
