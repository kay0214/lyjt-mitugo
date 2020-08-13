package co.yixiang.modules.commission.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.commission.entity.YxCommissionRate;
import co.yixiang.modules.commission.web.param.YxCommissionRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCommissionRateQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 分佣配置表 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Repository
public interface YxCommissionRateMapper extends BaseMapper<YxCommissionRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCommissionRateQueryVo getYxCommissionRateById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCommissionRateQueryParam
     * @return
     */
    IPage<YxCommissionRateQueryVo> getYxCommissionRatePageList(@Param("page") Page page, @Param("param") YxCommissionRateQueryParam yxCommissionRateQueryParam);

}
