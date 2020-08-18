/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxSystemUserTask;
import co.yixiang.modules.user.web.dto.TaskDTO;
import co.yixiang.modules.user.web.dto.UserLevelInfoDTO;
import co.yixiang.modules.user.web.param.YxSystemUserTaskQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 等级任务设置 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
public interface YxSystemUserTaskService extends BaseService<YxSystemUserTask> {

    List<YxSystemUserTaskQueryVo> tidyTask(List<YxSystemUserTaskQueryVo> task,int uid);

    int getTaskComplete(int levelId,int uid);

    TaskDTO getTaskList(int levelId, int uid, UserLevelInfoDTO level);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemUserTaskQueryVo getYxSystemUserTaskById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxSystemUserTaskQueryParam
     * @return
     */
    Paging<YxSystemUserTaskQueryVo> getYxSystemUserTaskPageList(YxSystemUserTaskQueryParam yxSystemUserTaskQueryParam) throws Exception;

}
