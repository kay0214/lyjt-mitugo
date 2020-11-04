package co.yixiang.modules.commission.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.commission.entity.YxNowRate;
import co.yixiang.modules.commission.web.param.YxNowRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxNowRateQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 购买时费率记录表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxNowRateMapper extends BaseMapper<YxNowRate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxNowRateQueryVo getYxNowRateById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxNowRateQueryParam
     * @return
     */
    IPage<YxNowRateQueryVo> getYxNowRatePageList(@Param("page") Page page, @Param("param") YxNowRateQueryParam yxNowRateQueryParam);

}
