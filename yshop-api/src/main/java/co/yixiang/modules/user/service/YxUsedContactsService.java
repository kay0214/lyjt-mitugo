package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 常用联系人表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxUsedContactsService extends BaseService<YxUsedContacts> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUsedContactsQueryVo getYxUsedContactsById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUsedContactsQueryParam
     * @return
     */
    Paging<YxUsedContactsQueryVo> getYxUsedContactsPageList(YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception;

}
