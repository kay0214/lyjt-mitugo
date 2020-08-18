/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.dto.YxExamineLogDto;
import co.yixiang.modules.shop.service.dto.YxExamineLogQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxExamineLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author liusy
* @date 2020-08-17
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxExamineLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxExamineLogServiceImpl extends BaseServiceImpl<YxExamineLogMapper, YxExamineLog> implements YxExamineLogService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxExamineLogQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxExamineLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxExamineLogDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxExamineLog> queryAll(YxExamineLogQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxExamineLog.class, criteria));
    }


    @Override
    public void download(List<YxExamineLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxExamineLogDto yxExamineLog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("审批类型 1:提现 2:商户信息", yxExamineLog.getType());
            map.put("审核数据关联id", yxExamineLog.getTypeId());
            map.put("审批状态：0->待审核,1->通过,2->驳回", yxExamineLog.getStatus());
            map.put("审核说明", yxExamineLog.getRemark());
            map.put("是否删除（0：未删除，1：已删除）", yxExamineLog.getDelFlag());
            map.put("创建人(审核人)", yxExamineLog.getCreateUserId());
            map.put("修改人", yxExamineLog.getUpdateUserId());
            map.put("创建时间", yxExamineLog.getCreateTime());
            map.put("更新时间", yxExamineLog.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
