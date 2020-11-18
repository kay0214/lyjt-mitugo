package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.UserAvatar;
import co.yixiang.modules.manage.web.param.UserAvatarQueryParam;
import co.yixiang.modules.manage.web.vo.UserAvatarQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 系统用户头像 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-18
 */
@Repository
public interface UserAvatarMapper extends BaseMapper<UserAvatar> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    UserAvatarQueryVo getUserAvatarById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param userAvatarQueryParam
     * @return
     */
    IPage<UserAvatarQueryVo> getUserAvatarPageList(@Param("page") Page page, @Param("param") UserAvatarQueryParam userAvatarQueryParam);

}
