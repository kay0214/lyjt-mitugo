/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.dto.WithdrawReviewQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxUserBillDto;
import co.yixiang.modules.shop.service.dto.YxUserBillQueryCriteria;
import co.yixiang.modules.shop.service.mapper.UserBillMapper;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
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

/**
* @author hupeng
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxUserBill")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxUserBillServiceImpl extends BaseServiceImpl<UserBillMapper, YxUserBill> implements YxUserBillService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxUserBillQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxUserBillDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }

    @Override
//    @Cacheable
    public List<YxUserBillDto> queryAll(YxUserBillQueryCriteria criteria){
        Integer startTime = null;
        Integer endTime = null;
        if (StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            startTime = DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00");
            endTime = DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59");
        }
        return baseMapper.findAllByQueryCriteria(criteria.getUsername(), criteria.getTitle(), criteria.getPm(), startTime, endTime);
    }

    @Override
    public Map<String, Object> queryAll(WithdrawReviewQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxUserBillDto> page = new PageInfo<>(queryAll2(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }

    private List<YxUserBillDto> queryAll2(WithdrawReviewQueryCriteria criteria){
        return baseMapper.withdrawReviewLog(criteria.getLinkId());
    }

    @Override
    public void download(List<YxUserBillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxUserBillDto yxUserBill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户uid", yxUserBill.getUid());
            map.put("关联id", yxUserBill.getLinkId());
            map.put("0 = 支出 1 = 获得", yxUserBill.getPm());
            map.put("账单标题", yxUserBill.getTitle());
            map.put("明细种类", yxUserBill.getCategory());
            map.put("明细类型", yxUserBill.getType());
            map.put("明细数字", yxUserBill.getNumber());
            map.put("剩余", yxUserBill.getBalance());
            map.put("备注", yxUserBill.getMark());
            map.put("添加时间", yxUserBill.getAddTime());
            map.put("0 = 带确定 1 = 有效 -1 = 无效", yxUserBill.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
