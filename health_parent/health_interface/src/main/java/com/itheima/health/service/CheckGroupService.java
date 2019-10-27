package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;

import java.util.List;


public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckItem findById(Integer id);

    List<Integer> findCheckItemByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();


}
