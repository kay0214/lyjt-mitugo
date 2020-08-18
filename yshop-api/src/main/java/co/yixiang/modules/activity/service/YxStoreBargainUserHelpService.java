/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.activity.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserHelpQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserHelpQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 砍价用户帮助表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
public interface YxStoreBargainUserHelpService extends BaseService<YxStoreBargainUserHelp> {

    List<YxStoreBargainUserHelpQueryVo> getList(int bargainId,int bargainUserUid,int page,int limit);

    int getBargainUserHelpPeopleCount(int bargainId,int bargainUserUid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainUserHelpQueryVo getYxStoreBargainUserHelpById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreBargainUserHelpQueryParam
     * @return
     */
    Paging<YxStoreBargainUserHelpQueryVo> getYxStoreBargainUserHelpPageList(YxStoreBargainUserHelpQueryParam yxStoreBargainUserHelpQueryParam) throws Exception;

}
