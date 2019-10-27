package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckGroupDao {


    void add(CheckGroup checkGroup);

    void addCheckGroupCheckItemByCheckGroupId(Integer checkGroupId, Integer checkitemId);

    Page<CheckGroup> findPage(String queryString);

    CheckItem findById(Integer id);

    List<Integer> findCheckItemByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupCheckItemByCheckGroupId(Integer id);

    void delete(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckgroupListById(Integer id);

}
