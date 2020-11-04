package co.yixiang.modules.contract.service;

import co.yixiang.modules.contract.entity.YxContractTemplate;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.contract.web.param.YxContractTemplateQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractTemplateQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 合同模板 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxContractTemplateService extends BaseService<YxContractTemplate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxContractTemplateQueryVo getYxContractTemplateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxContractTemplateQueryParam
     * @return
     */
    Paging<YxContractTemplateQueryVo> getYxContractTemplatePageList(YxContractTemplateQueryParam yxContractTemplateQueryParam) throws Exception;

}
