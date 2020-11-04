package co.yixiang.modules.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.contract.entity.YxContractSign;
import co.yixiang.modules.contract.web.param.YxContractSignQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractSignQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 合同签署表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxContractSignMapper extends BaseMapper<YxContractSign> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxContractSignQueryVo getYxContractSignById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxContractSignQueryParam
     * @return
     */
    IPage<YxContractSignQueryVo> getYxContractSignPageList(@Param("page") Page page, @Param("param") YxContractSignQueryParam yxContractSignQueryParam);

}
