/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.activity.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户参与砍价表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
public interface YxStoreBargainUserService extends BaseService<YxStoreBargainUser> {

    void setBargainUserStatus(int bargainId,int uid);

    void bargainCancel(int bargainId,int uid);

    List<YxStoreBargainUserQueryVo> bargainUserList(int bargainUserUid,int page,int limit);

    boolean isBargainUserHelp(int bargainId,int bargainUserUid,int uid);

    void setBargain(Integer bargainId,Integer uid);

    double getBargainUserDiffPrice(int id);


    YxStoreBargainUser getBargainUserInfo(int bargainId, int uid);

    List<YxStoreBargainUserQueryVo> getBargainUserList(int bargainId,int status);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainUserQueryVo getYxStoreBargainUserById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreBargainUserQueryParam
     * @return
     */
    Paging<YxStoreBargainUserQueryVo> getYxStoreBargainUserPageList(YxStoreBargainUserQueryParam yxStoreBargainUserQueryParam) throws Exception;

}
