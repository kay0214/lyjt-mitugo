/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.web.dto.UserLevelDTO;
import co.yixiang.modules.user.web.param.YxSystemUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 设置用户等级表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
public interface YxSystemUserLevelService extends BaseService<YxSystemUserLevel> {

    int getNextLevelId(int levelId);

    boolean getClear(int levelId);

    List<YxSystemUserLevelQueryVo> getLevelListAndGrade(Integer levelId,boolean isTask);

    UserLevelDTO getLevelInfo(int uid,boolean isTask);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemUserLevelQueryVo getYxSystemUserLevelById(Serializable id);

    /**
     * 获取分页对象
     * @param yxSystemUserLevelQueryParam
     * @return
     */
    Paging<YxSystemUserLevelQueryVo> getYxSystemUserLevelPageList(YxSystemUserLevelQueryParam yxSystemUserLevelQueryParam) throws Exception;

}
