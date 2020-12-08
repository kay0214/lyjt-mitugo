package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipAppointDetail;
import co.yixiang.modules.ship.web.param.YxShipAppointDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只预约表详情 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipAppointDetailMapper extends BaseMapper<YxShipAppointDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipAppointDetailQueryVo getYxShipAppointDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipAppointDetailQueryParam
     * @return
     */
    IPage<YxShipAppointDetailQueryVo> getYxShipAppointDetailPageList(@Param("page") Page page, @Param("param") YxShipAppointDetailQueryParam yxShipAppointDetailQueryParam);

}
