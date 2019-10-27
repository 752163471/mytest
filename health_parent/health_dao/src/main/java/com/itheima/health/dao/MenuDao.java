package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuDao {
    void add(Menu menu);

    Page<Menu> findPage(String queryString);

    Menu findById(Integer id);

    void edit(Menu menu);

    int findRoleMenuByMenuId(Integer id);

    void delete(Integer id);

    LinkedHashSet<Menu> findMenusByRoleId(Integer roleId);

    List<Menu> findChildrenByParentMenuId(Integer parentMenuId);

    List<Menu> findAll();

}
