package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    void add(Setmeal setmeal);

    void addSetmealAndCheckgroupBySetmealId(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupBySetmealId(Integer id);

    void edit(Setmeal setmeal);

    void deleteSetmealAndCheckgroupBySetmealId(Integer id);

    void updateSetmealAndCheckgroupBySetmealId(@Param("setmeal_id") Integer id,@Param("checkgroup_id") Integer checkgroupId);

    void delete(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findCheckGroupListById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
