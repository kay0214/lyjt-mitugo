package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxCustomerService;
import co.yixiang.modules.user.web.param.YxCustomerServiceQueryParam;
import co.yixiang.modules.user.web.vo.YxCustomerServiceQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 机器人客服表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxCustomerServiceMapper extends BaseMapper<YxCustomerService> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCustomerServiceQueryVo getYxCustomerServiceById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCustomerServiceQueryParam
     * @return
     */
    IPage<YxCustomerServiceQueryVo> getYxCustomerServicePageList(@Param("page") Page page, @Param("param") YxCustomerServiceQueryParam yxCustomerServiceQueryParam);

}
