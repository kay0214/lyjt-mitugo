package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只运营记录 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipOperationMapper extends BaseMapper<YxShipOperation> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipOperationQueryVo getYxShipOperationById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipOperationQueryParam
     * @return
     */
    IPage<YxShipOperationQueryVo> getYxShipOperationPageList(@Param("page") Page page, @Param("param") YxShipOperationQueryParam yxShipOperationQueryParam);

}
