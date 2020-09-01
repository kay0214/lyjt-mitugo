/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shop.domain.YxSystemAttachment;
import co.yixiang.modules.shop.service.YxSystemAttachmentService;
import co.yixiang.modules.shop.service.dto.YxSystemAttachmentDto;
import co.yixiang.modules.shop.service.dto.YxSystemAttachmentQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxSystemAttachmentMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author nxl
* @date 2020-09-01
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxSystemAttachment")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxSystemAttachmentServiceImpl extends BaseServiceImpl<YxSystemAttachmentMapper, YxSystemAttachment> implements YxSystemAttachmentService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxSystemAttachmentQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxSystemAttachment> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxSystemAttachmentDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxSystemAttachment> queryAll(YxSystemAttachmentQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxSystemAttachment.class, criteria));
    }


    @Override
    public void download(List<YxSystemAttachmentDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxSystemAttachmentDto yxSystemAttachment : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("附件名称", yxSystemAttachment.getName());
            map.put("附件路径", yxSystemAttachment.getAttDir());
            map.put("压缩图片路径", yxSystemAttachment.getSattDir());
            map.put("附件大小", yxSystemAttachment.getAttSize());
            map.put("附件类型", yxSystemAttachment.getAttType());
            map.put("分类ID0编辑器,1产品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图", yxSystemAttachment.getPid());
            map.put("上传时间", yxSystemAttachment.getTime());
            map.put("图片上传类型 1本地 2七牛云 3OSS 4COS ", yxSystemAttachment.getImageType());
            map.put("图片上传模块类型 1 后台上传 2 用户生成", yxSystemAttachment.getModuleType());
            map.put("用户id", yxSystemAttachment.getUid());
            map.put("邀请码", yxSystemAttachment.getInviteCode());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
