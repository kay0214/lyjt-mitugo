package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.DictDetailQueryParam;
import co.yixiang.modules.manage.web.vo.DictDetailQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 数据字典详情 服务类
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
public interface DictDetailService extends BaseService<DictDetail> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    DictDetailQueryVo getDictDetailById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param dictDetailQueryParam
     * @return
     */
    Paging<DictDetailQueryVo> getDictDetailPageList(DictDetailQueryParam dictDetailQueryParam) throws Exception;

    /**
     * 根据字段类型以及数值查找dictDetail
     *
     * @param type
     * @param valueId
     * @return
     */
    public DictDetail getDictDetailValueByType(String type, Integer valueId);

}
