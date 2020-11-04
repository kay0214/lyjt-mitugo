package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只预约表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipAppointMapper extends BaseMapper<YxShipAppoint> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipAppointQueryVo getYxShipAppointById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipAppointQueryParam
     * @return
     */
    IPage<YxShipAppointQueryVo> getYxShipAppointPageList(@Param("page") Page page, @Param("param") YxShipAppointQueryParam yxShipAppointQueryParam);

}
