/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserSign;
import co.yixiang.modules.user.web.dto.SignDTO;
import co.yixiang.modules.user.web.param.YxUserSignQueryParam;
import co.yixiang.modules.user.web.vo.YxUserSignQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 签到记录表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-05
 */
public interface YxUserSignService extends BaseService<YxUserSign> {

    int sign(int uid);

    List<SignDTO>  getSignList(int uid,int page,int limit);

    boolean getYesterDayIsSign(int uid);

    boolean getToDayIsSign(int uid);

    int getSignSumDay(int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserSignQueryVo getYxUserSignById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUserSignQueryParam
     * @return
     */
    Paging<YxUserSignQueryVo> getYxUserSignPageList(YxUserSignQueryParam yxUserSignQueryParam) throws Exception;

}
