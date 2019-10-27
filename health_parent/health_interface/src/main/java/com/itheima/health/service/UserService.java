package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserService {
    void add(User user,Integer[] roleIds);

    PageResult findPage(QueryPageBean queryPageBean);

    User findById(Integer id);

    List<Integer> findUserRoleByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    void delete(Integer id);

    User findUserByUsername(String s);
}
