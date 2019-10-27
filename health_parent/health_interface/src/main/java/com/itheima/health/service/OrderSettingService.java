package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;


import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void upload(List<OrderSetting> list);

    List<Map<String, Object>> findOrderSettingByOrderDate(String orderDate);

    void edit(OrderSetting orderSetting);


    void deleteOrderSettingHistoryData(String currentDate);

}
