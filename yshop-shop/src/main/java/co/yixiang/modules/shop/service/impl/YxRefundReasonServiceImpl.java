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
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxRefundReason;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxRefundReasonService;
import co.yixiang.modules.shop.service.dto.YxRefundReasonDto;
import co.yixiang.modules.shop.service.dto.YxRefundReasonQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxRefundReasonMapper;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
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
 * @author nxl
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxRefundReason")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxRefundReasonServiceImpl extends BaseServiceImpl<YxRefundReasonMapper, YxRefundReason> implements YxRefundReasonService {

    private final IGenerator generator;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxRefundReasonQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxRefundReason> page = new PageInfo<>(queryAll(criteria));
        List<YxRefundReasonDto> list = new ArrayList<>();
        if (page.getTotal() > 0) {
            for (YxRefundReason item : page.getList()) {
                YxRefundReasonDto dto = generator.convert(item, YxRefundReasonDto.class);
                User user = this.userService.getById(dto.getUpdateUserId());
                if (null != user) {
                    dto.setUpdateUsername(user.getNickName());
                }
                list.add(dto);
            }
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxRefundReasonDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxRefundReason> queryAll(YxRefundReasonQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxRefundReason.class, criteria));
    }


    @Override
    public void download(List<YxRefundReasonDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxRefundReasonDto yxRefundReason : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("理由", yxRefundReason.getReason());
            map.put("类型：0：本地生活，1：商城", yxRefundReason.getReasonType());
            map.put("是否删除（0：未删除，1：已删除）", yxRefundReason.getDelFlag());
            map.put("创建人", yxRefundReason.getCreateUserId());
            map.put("修改人", yxRefundReason.getUpdateUserId());
            map.put("创建时间", yxRefundReason.getCreateTime());
            map.put("更新时间", yxRefundReason.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据主键查询未删除的数据
     *
     * @param id
     * @return
     */
    @Override
    public YxRefundReason selectById(Integer id) {
        YxRefundReason yxRefundReason = this.getOne(new QueryWrapper<YxRefundReason>().lambda().eq(YxRefundReason::getId, id).eq(YxRefundReason::getDelFlag, 0));
        if (null == yxRefundReason) {
            return null;
        }
        return yxRefundReason;
    }
}
