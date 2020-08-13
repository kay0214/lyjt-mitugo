package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.User;
import co.yixiang.modules.manage.web.param.UserQueryParam;
import co.yixiang.modules.manage.web.vo.UserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UserQueryVo getUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param userQueryParam
     * @return
     */
    IPage<UserQueryVo> getUserPageList(@Param("page") Page page, @Param("param") UserQueryParam userQueryParam);

}
