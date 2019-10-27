package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {
    void add(Permission permission);

    PageResult findPage(QueryPageBean queryPageBean);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);

    List<Permission> findAll();

}
