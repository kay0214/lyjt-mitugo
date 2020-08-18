/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserTaskFinish;
import co.yixiang.modules.user.web.param.YxUserTaskFinishQueryParam;
import co.yixiang.modules.user.web.vo.YxUserTaskFinishQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 用户任务完成记录表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-07
 */
public interface YxUserTaskFinishService extends BaseService<YxUserTaskFinish> {

    void setFinish(int uid,int taskId);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserTaskFinishQueryVo getYxUserTaskFinishById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUserTaskFinishQueryParam
     * @return
     */
    Paging<YxUserTaskFinishQueryVo> getYxUserTaskFinishPageList(YxUserTaskFinishQueryParam yxUserTaskFinishQueryParam) throws Exception;

}
