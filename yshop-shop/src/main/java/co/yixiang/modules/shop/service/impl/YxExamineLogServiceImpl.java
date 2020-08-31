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
import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.modules.shop.domain.YxMerchantsDetail;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.dto.YxExamineLogDto;
import co.yixiang.modules.shop.service.dto.YxExamineLogQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxExamineLogMapper;
import co.yixiang.modules.shop.service.mapper.YxMerchantsDetailMapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author liusy
 * @date 2020-08-19
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxExamineLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxExamineLogServiceImpl extends BaseServiceImpl<YxExamineLogMapper, YxExamineLog> implements YxExamineLogService {

    private final IGenerator generator;
    //    @Autowired
//    private YxMerchantsDetailService yxMerchantsDetailService;
    @Autowired
    private YxMerchantsDetailMapper yxMerchantsDetailMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxExamineLogQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxExamineLogDto> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxExamineLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxExamineLog::getCreateUserId, criteria.getChildUser()).eq(YxExamineLog::getDelFlag, 0);
        }
        if (null != criteria.getType()) {
            queryWrapper.lambda().eq(YxExamineLog::getType, criteria.getType());
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxExamineLog::getUsername, criteria.getUsername());
        }
        IPage<YxExamineLog> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(ipage.getRecords(), YxExamineLogDto.class));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxExamineLogDto> queryAll(YxExamineLogQueryCriteria criteria) {
        List<YxExamineLog> list = baseMapper.selectList(QueryHelpPlus.getPredicate(YxExamineLog.class, criteria));
        if (null == list || list.size() <= 0) {
            return new ArrayList<YxExamineLogDto>();
        }
        List<YxExamineLogDto> result = generator.convert(list, YxExamineLogDto.class);
        // 查询商户认证数据
        if (2 == criteria.getType()) {
            for (YxExamineLogDto dto : result) {
                YxMerchantsDetail yxMerchantsDetail = yxMerchantsDetailMapper.selectById(dto.getTypeId());
                dto.setContacts(yxMerchantsDetail.getContacts());
                dto.setContactMobile(yxMerchantsDetail.getContactMobile());
                dto.setMerchantsName(yxMerchantsDetail.getMerchantsName());
            }
        }
        return result;
    }


    @Override
    public void download(List<YxExamineLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxExamineLogDto yxExamineLog : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("审批类型 1:提现 2:商户信息", yxExamineLog.getType());
            map.put("审核数据关联id", yxExamineLog.getTypeId());
            map.put("审批状态：0->待审核,1->通过,2->驳回", yxExamineLog.getStatus());
            map.put("审核说明", yxExamineLog.getRemark());
            map.put("是否删除（0：未删除，1：已删除）", yxExamineLog.getDelFlag());
            map.put("创建人(审核人)", yxExamineLog.getCreateUserId());
            map.put("修改人", yxExamineLog.getUpdateUserId());
            map.put("创建时间", yxExamineLog.getCreateTime());
            map.put("更新时间", yxExamineLog.getUpdateTime());
            map.put("冗余字段：被审核人id", yxExamineLog.getUid());
            map.put("冗余字段：被审核人信息", yxExamineLog.getUsername());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
