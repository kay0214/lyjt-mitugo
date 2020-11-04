package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipInfoMapper extends BaseMapper<YxShipInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipInfoQueryVo getYxShipInfoById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipInfoQueryParam
     * @return
     */
    IPage<YxShipInfoQueryVo> getYxShipInfoPageList(@Param("page") Page page, @Param("param") YxShipInfoQueryParam yxShipInfoQueryParam);

}
