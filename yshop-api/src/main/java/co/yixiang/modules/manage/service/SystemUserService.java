package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.SystemUserQueryParam;
import co.yixiang.modules.manage.web.vo.SystemUserQueryVo;
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
public interface SystemUserService extends BaseService<SystemUser> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    SystemUserQueryVo getUserById(Serializable id);

    /**
     * 获取分页对象
     * @param userQueryParam
     * @return
     */
    Paging<SystemUserQueryVo> getUserPageList(SystemUserQueryParam userQueryParam) throws Exception;

    /**
     * 商户金额操作
     * @param updateSystemUser
     */
    void updateUserTotal(SystemUser updateSystemUser);
}
