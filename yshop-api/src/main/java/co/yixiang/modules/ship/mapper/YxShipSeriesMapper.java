package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.web.param.YxShipSeriesQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只系列表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipSeriesMapper extends BaseMapper<YxShipSeries> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipSeriesQueryVo getYxShipSeriesById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipSeriesQueryParam
     * @return
     */
    IPage<YxShipSeriesQueryVo> getYxShipSeriesPageList(@Param("page") Page page, @Param("param") YxShipSeriesQueryParam yxShipSeriesQueryParam);

}
