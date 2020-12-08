package co.yixiang.modules.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.contract.entity.YxContractTemplate;
import co.yixiang.modules.contract.web.param.YxContractTemplateQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 合同模板 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxContractTemplateMapper extends BaseMapper<YxContractTemplate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxContractTemplateQueryVo getYxContractTemplateById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxContractTemplateQueryParam
     * @return
     */
    IPage<YxContractTemplateQueryVo> getYxContractTemplatePageList(@Param("page") Page page, @Param("param") YxContractTemplateQueryParam yxContractTemplateQueryParam);

}
