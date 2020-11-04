package co.yixiang.modules.commission.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.web.param.YxCustomizeRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCustomizeRateQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 自定义分佣配置表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxCustomizeRateMapper extends BaseMapper<YxCustomizeRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCustomizeRateQueryVo getYxCustomizeRateById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCustomizeRateQueryParam
     * @return
     */
    IPage<YxCustomizeRateQueryVo> getYxCustomizeRatePageList(@Param("page") Page page, @Param("param") YxCustomizeRateQueryParam yxCustomizeRateQueryParam);

}
