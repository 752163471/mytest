package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    void add(Permission permission);

    Page<Permission> findPage(String queryString);

    Permission findById(Integer id);

    void edit(Permission permission);

    int findRolePermissionByPermissionId(Integer id);

    void delete(Integer id);

    Set<Permission> findPermissionsByRoleId(Integer id);

    List<Permission> findAll();


}
