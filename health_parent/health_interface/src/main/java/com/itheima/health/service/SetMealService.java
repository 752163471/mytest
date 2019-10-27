package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add(Setmeal setmeal,Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupBySetmealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findCheckGroupListById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
