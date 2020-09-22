/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.web.param.YxSystemAttachmentQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemAttachmentQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 附件管理表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
public interface YxSystemAttachmentService extends BaseService<YxSystemAttachment> {

    YxSystemAttachment getInfo(String name);

    YxSystemAttachment getByCode(String code);

    void attachmentAdd(String name,String attSize,String attDir,String sattDir);

    void attachmentAdd(String name, String attSize, String attDir,String sattDir,Integer imageType);

    void newAttachmentAdd(String name,String attSize,String attDir,String sattDir,int uid,String code);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemAttachmentQueryVo getYxSystemAttachmentById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxSystemAttachmentQueryParam
     * @return
     */
    Paging<YxSystemAttachmentQueryVo> getYxSystemAttachmentPageList(YxSystemAttachmentQueryParam yxSystemAttachmentQueryParam) throws Exception;

}
