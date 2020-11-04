package co.yixiang.modules.contract.service;

import co.yixiang.modules.contract.entity.YxContractSign;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.contract.web.param.YxContractSignQueryParam;
import co.yixiang.modules.contract.web.vo.YxContractSignQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 合同签署表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxContractSignService extends BaseService<YxContractSign> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxContractSignQueryVo getYxContractSignById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxContractSignQueryParam
     * @return
     */
    Paging<YxContractSignQueryVo> getYxContractSignPageList(YxContractSignQueryParam yxContractSignQueryParam) throws Exception;

}
