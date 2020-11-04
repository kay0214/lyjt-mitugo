package co.yixiang.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.system.entity.YxNotice;
import co.yixiang.modules.system.web.param.YxNoticeQueryParam;
import co.yixiang.modules.system.web.vo.YxNoticeQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxNoticeMapper extends BaseMapper<YxNotice> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxNoticeQueryVo getYxNoticeById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxNoticeQueryParam
     * @return
     */
    IPage<YxNoticeQueryVo> getYxNoticePageList(@Param("page") Page page, @Param("param") YxNoticeQueryParam yxNoticeQueryParam);

}
