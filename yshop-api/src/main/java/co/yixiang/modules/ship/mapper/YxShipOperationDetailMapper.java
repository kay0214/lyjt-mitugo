package co.yixiang.modules.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 船只运营记录详情 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxShipOperationDetailMapper extends BaseMapper<YxShipOperationDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxShipOperationDetailQueryVo getYxShipOperationDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxShipOperationDetailQueryParam
     * @return
     */
    IPage<YxShipOperationDetailQueryVo> getYxShipOperationDetailPageList(@Param("page") Page page, @Param("param") YxShipOperationDetailQueryParam yxShipOperationDetailQueryParam);

}
