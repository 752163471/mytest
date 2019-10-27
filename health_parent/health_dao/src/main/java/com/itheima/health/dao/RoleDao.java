package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    List<Role> findAll();

    Set<Role> findRolesByUserId(Integer id);

    List<Role> findRoleByUsername(Integer userId);

    void add(Role role);

    void addRolePermissionByRoleId(@Param("role_id") Integer id, @Param("permission_id")Integer permissionId);

    Page<Role> findPage(String queryString);

    void addRoleMenuByRoleId(@Param("role_id") Integer id,@Param("menu_id") Integer menuId);

    void deleteRolePermissionByRoleId(Integer id);

    void deleteRoleMenuByRoleId(Integer id);

    void deleteRoleByRoleId(Integer id);
}
