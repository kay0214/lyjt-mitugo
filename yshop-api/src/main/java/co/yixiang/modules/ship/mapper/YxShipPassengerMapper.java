package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 本地生活帆船订单乘客表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipPassengerMapper extends BaseMapper<YxShipPassenger> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipPassengerQueryVo getYxShipPassengerById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipPassengerQueryParam
     * @return
     */
    IPage<YxShipPassengerQueryVo> getYxShipPassengerPageList(@Param("page") Page page, @Param("param") YxShipPassengerQueryParam yxShipPassengerQueryParam);

}
