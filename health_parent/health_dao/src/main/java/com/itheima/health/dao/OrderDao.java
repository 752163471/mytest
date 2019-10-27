package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void save(Order order);

    Map<String, Object> findById(Integer id);

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findOrderByDate(@Param("begin") String thisWeekMonday, @Param("end") String thisWeekSunday);

    Integer findVisitsByDate(@Param("begin") String thisWeekMonday, @Param("end") String thisWeekSunday);

    List<Map<String, Object>> findHotSetmeal();
}
