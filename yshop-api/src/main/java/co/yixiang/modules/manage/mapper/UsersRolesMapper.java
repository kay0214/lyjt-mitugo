package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.manage.web.param.UsersRolesQueryParam;
import co.yixiang.modules.manage.web.vo.UsersRolesQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户角色关联 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-12
 */
@Repository
public interface UsersRolesMapper extends BaseMapper<UsersRoles> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UsersRolesQueryVo getUsersRolesById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param usersRolesQueryParam
     * @return
     */
    IPage<UsersRolesQueryVo> getUsersRolesPageList(@Param("page") Page page, @Param("param") UsersRolesQueryParam usersRolesQueryParam);

}
