package co.yixiang.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.system.entity.YxHotConfig;
import co.yixiang.modules.system.web.param.YxHotConfigQueryParam;
import co.yixiang.modules.system.web.vo.YxHotConfigQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * HOT配置表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxHotConfigMapper extends BaseMapper<YxHotConfig> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxHotConfigQueryVo getYxHotConfigById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxHotConfigQueryParam
     * @return
     */
    IPage<YxHotConfigQueryVo> getYxHotConfigPageList(@Param("page") Page page, @Param("param") YxHotConfigQueryParam yxHotConfigQueryParam);

}
