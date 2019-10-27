package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    void add(Role role, Integer[] permissionIds,Integer[] menuIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);
}
