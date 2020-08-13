package co.yixiang.modules.point.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.point.entity.YxPointDetail;
import co.yixiang.modules.point.web.param.YxPointDetailQueryParam;
import co.yixiang.modules.point.web.vo.YxPointDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 积分获取明细 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Repository
public interface YxPointDetailMapper extends BaseMapper<YxPointDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxPointDetailQueryVo getYxPointDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxPointDetailQueryParam
     * @return
     */
    IPage<YxPointDetailQueryVo> getYxPointDetailPageList(@Param("page") Page page, @Param("param") YxPointDetailQueryParam yxPointDetailQueryParam);

}
