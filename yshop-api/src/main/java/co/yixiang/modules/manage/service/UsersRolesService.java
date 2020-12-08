package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.UsersRolesQueryParam;
import co.yixiang.modules.manage.web.vo.UsersRolesQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 用户角色关联 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-12
 */
public interface UsersRolesService extends BaseService<UsersRoles> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UsersRolesQueryVo getUsersRolesById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param usersRolesQueryParam
     * @return
     */
    Paging<UsersRolesQueryVo> getUsersRolesPageList(UsersRolesQueryParam usersRolesQueryParam) throws Exception;

}
