package com.itheima.health.dao;

import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {
    void add(CheckItem checkItem);


    List<CheckItem> findPage(String queryString);

    void delete(Integer id);

    long queryCheckgroupCheckitemByCheckitemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemListById(Integer id);

}
