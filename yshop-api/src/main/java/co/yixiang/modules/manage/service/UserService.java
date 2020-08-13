package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.User;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.UserQueryParam;
import co.yixiang.modules.manage.web.vo.UserQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UserQueryVo getUserById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param userQueryParam
     * @return
     */
    Paging<UserQueryVo> getUserPageList(UserQueryParam userQueryParam) throws Exception;

}
