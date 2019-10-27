package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        com.itheima.health.pojo.User user = userService.findUserByUsername(s);
        if (user == null) { //如果不存在，抛出异常
            return null;
        }
        //如果存在，获取角色和权限

        List<GrantedAuthority> list = new ArrayList<>();

        Set<Role> roles = user.getRoles();
        if (roles != null) {
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null) {
                    for (Permission permission : permissions) { //拿到权限
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        //获取密码
        String passwordInDb = user.getPassword();

        return new User(s,passwordInDb,list);
    }
}
