package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxFundsDetail;
import co.yixiang.modules.user.web.param.YxFundsDetailQueryParam;
import co.yixiang.modules.user.web.vo.YxFundsDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 平台资金明细 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxFundsDetailMapper extends BaseMapper<YxFundsDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxFundsDetailQueryVo getYxFundsDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxFundsDetailQueryParam
     * @return
     */
    IPage<YxFundsDetailQueryVo> getYxFundsDetailPageList(@Param("page") Page page, @Param("param") YxFundsDetailQueryParam yxFundsDetailQueryParam);

}
