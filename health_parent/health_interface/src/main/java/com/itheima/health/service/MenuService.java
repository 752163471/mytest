package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface MenuService {
    void add(Menu menu);

    PageResult findPage(QueryPageBean queryPageBean);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);

    List<Role> findMenu(String username);

    List<Menu> findAll();

}
