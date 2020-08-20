package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.web.param.DictDetailQueryParam;
import co.yixiang.modules.manage.web.vo.DictDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 数据字典详情 Mapper 接口
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Repository
public interface DictDetailMapper extends BaseMapper<DictDetail> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    DictDetailQueryVo getDictDetailById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param dictDetailQueryParam
     * @return
     */
    IPage<DictDetailQueryVo> getDictDetailPageList(@Param("page") Page page, @Param("param") DictDetailQueryParam dictDetailQueryParam);

}
