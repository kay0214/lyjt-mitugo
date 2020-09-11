/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxFundsAccount;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxFundsAccountService;
import co.yixiang.modules.shop.service.dto.YxFundsAccountDto;
import co.yixiang.modules.shop.service.dto.YxFundsAccountQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxFundsAccountMapper;
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
* @date 2020-09-11
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxFundsAccount")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxFundsAccountServiceImpl extends BaseServiceImpl<YxFundsAccountMapper, YxFundsAccount> implements YxFundsAccountService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxFundsAccountQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxFundsAccount> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxFundsAccountDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxFundsAccount> queryAll(YxFundsAccountQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxFundsAccount.class, criteria));
    }


    @Override
    public void download(List<YxFundsAccountDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxFundsAccountDto yxFundsAccount : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("平台分佣总额", yxFundsAccount.getPrice());
            map.put("分红总积分", yxFundsAccount.getBonusPoint());
            map.put("拉新总积分", yxFundsAccount.getReferencePoint());
            map.put("是否删除（0：未删除，1：已删除）", yxFundsAccount.getDelFlag());
            map.put("创建人", yxFundsAccount.getCreateUserId());
            map.put("修改人", yxFundsAccount.getUpdateUserId());
            map.put("创建时间", yxFundsAccount.getCreateTime());
            map.put("更新时间", yxFundsAccount.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
