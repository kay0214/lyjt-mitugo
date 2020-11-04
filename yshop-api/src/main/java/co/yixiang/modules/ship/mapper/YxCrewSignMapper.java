package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxCrewSign;
import co.yixiang.modules.ship.web.param.YxCrewSignQueryParam;
import co.yixiang.modules.ship.web.vo.YxCrewSignQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船员签到表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxCrewSignMapper extends BaseMapper<YxCrewSign> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCrewSignQueryVo getYxCrewSignById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCrewSignQueryParam
     * @return
     */
    IPage<YxCrewSignQueryVo> getYxCrewSignPageList(@Param("page") Page page, @Param("param") YxCrewSignQueryParam yxCrewSignQueryParam);

}
