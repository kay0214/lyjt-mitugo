package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.Dict;
import co.yixiang.modules.manage.web.param.DictQueryParam;
import co.yixiang.modules.manage.web.vo.DictQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Repository
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    DictQueryVo getDictById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param dictQueryParam
     * @return
     */
    IPage<DictQueryVo> getDictPageList(@Param("page") Page page, @Param("param") DictQueryParam dictQueryParam);

}
