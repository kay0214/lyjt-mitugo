/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.security.service;


import co.yixiang.modules.user.entity.YxUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtPermissionService {


    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user
     * @return
     */

    public Collection<GrantedAuthority> mapToGrantedAuthorities(YxUser user) {

        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");

        //Set<Role> roles = roleRepository.findByUsers_Id(user.getId());
        List<String> list = new ArrayList<>();
        list.add("yshop");

        return list.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
