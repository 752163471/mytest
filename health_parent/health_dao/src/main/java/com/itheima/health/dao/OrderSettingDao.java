package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    void upload(OrderSetting orderSetting);

    long findOrderSettingByOrderDate(Date orderDate);

    void update(OrderSetting orderSetting);

    List<OrderSetting> QueryByOrderDate(@Param("dateBegin") String dateBegin, @Param("dateEnd") String dateEnd);

    void deleteOrderSettingHistoryData(String currentDate);

    OrderSetting findOrderSettingByCondition(Date orderDate);

    void updateReservationsByOrderDate(OrderSetting orderSetting);
}
