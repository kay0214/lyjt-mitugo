package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.Dict;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.DictQueryParam;
import co.yixiang.modules.manage.web.vo.DictQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    DictQueryVo getDictById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param dictQueryParam
     * @return
     */
    Paging<DictQueryVo> getDictPageList(DictQueryParam dictQueryParam) throws Exception;

}
