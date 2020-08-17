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
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.dto.UserDto;
import co.yixiang.modules.shop.service.dto.UserQueryCriteria;
import co.yixiang.modules.shop.service.mapper.UserSysMapper;
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
* @author zhangyk
* @date 2020-08-15
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "user")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserSysMapper, User> implements UserService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<User> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<User> queryAll(UserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(User.class, criteria));
    }


    @Override
    public void download(List<UserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto user : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("头像", user.getAvatarId());
            map.put("邮箱", user.getEmail());
            map.put("状态：1启用、0禁用", user.getEnabled());
            map.put("密码", user.getPassword());
            map.put("用户名", user.getUsername());
            map.put("部门名称", user.getDeptId());
            map.put("手机号码", user.getPhone());
            map.put("岗位名称", user.getJobId());
            map.put("创建日期", user.getCreateTime());
            map.put("最后修改密码的日期", user.getLastPasswordResetTime());
            map.put(" nickName",  user.getNickName());
            map.put(" sex",  user.getSex());
            map.put("用户角色：0->平台运营,1->合伙人,2->商户", user.getUserRole());
            map.put("商户联系人", user.getMerchantsContact());
            map.put("联系电话", user.getContactPhone());
            map.put("商户状态：0->启用,1->禁用", user.getMerchantsStatus());
            map.put("总积分", user.getTotalScore());
            map.put("推荐用二维码地址", user.getQrCodeUrl());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}