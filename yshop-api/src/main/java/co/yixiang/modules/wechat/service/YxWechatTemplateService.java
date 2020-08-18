/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.wechat.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.wechat.entity.YxWechatTemplate;
import co.yixiang.modules.wechat.web.param.YxWechatTemplateQueryParam;
import co.yixiang.modules.wechat.web.vo.YxWechatTemplateQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 微信模板 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-10
 */
public interface YxWechatTemplateService extends BaseService<YxWechatTemplate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWechatTemplateQueryVo getYxWechatTemplateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxWechatTemplateQueryParam
     * @return
     */
    Paging<YxWechatTemplateQueryVo> getYxWechatTemplatePageList(YxWechatTemplateQueryParam yxWechatTemplateQueryParam) throws Exception;

}
