/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.wechat.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.wechat.entity.YxWechatTemplate;
import co.yixiang.modules.wechat.mapper.YxWechatTemplateMapper;
import co.yixiang.modules.wechat.service.YxWechatTemplateService;
import co.yixiang.modules.wechat.web.param.YxWechatTemplateQueryParam;
import co.yixiang.modules.wechat.web.vo.YxWechatTemplateQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 微信模板 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-10
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WechatTemplateServiceImpl extends BaseServiceImpl<YxWechatTemplateMapper, YxWechatTemplate> implements YxWechatTemplateService {

    private final YxWechatTemplateMapper yxWechatTemplateMapper;

    @Override
    public YxWechatTemplateQueryVo getYxWechatTemplateById(Serializable id) throws Exception{
        return yxWechatTemplateMapper.getYxWechatTemplateById(id);
    }

    @Override
    public Paging<YxWechatTemplateQueryVo> getYxWechatTemplatePageList(YxWechatTemplateQueryParam yxWechatTemplateQueryParam) throws Exception{
        Page page = setPageParam(yxWechatTemplateQueryParam,OrderItem.desc("create_time"));
        IPage<YxWechatTemplateQueryVo> iPage = yxWechatTemplateMapper.getYxWechatTemplatePageList(page,yxWechatTemplateQueryParam);
        return new Paging(iPage);
    }

}
