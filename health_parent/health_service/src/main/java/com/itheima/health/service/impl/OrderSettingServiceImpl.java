package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            //遍历集合
            for (OrderSetting orderSetting : list) {

                //判断当前的日期之前是否设置过
                long count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());

                if (count > 0){ //如果设置过，更新数量
                    orderSettingDao.update(orderSetting);
                }else {     //没有设置过，保存
                    orderSettingDao.upload(orderSetting);
                }

            }
        }
    }

    //展示日历预约设置
    @Override
    public List<Map<String, Object>> findOrderSettingByOrderDate(String orderDate) {

        //组织查询map
        //dateBegin表示月份开始时间
        String dateBegin = orderDate + "-01";
        //dateEnd月份结束时间
        String dateEnd = orderDate + "-31";
        //查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.QueryByOrderDate(dateBegin,dateEnd);

        //将List<OrderSetting>组织成List<Map>
        List<Map<String,Object>> mapList = new ArrayList<>();

        for (OrderSetting orderSetting : list) {
                Map<String,Object> map = new HashMap<>();

                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());

                mapList.add(map);
        }

        return mapList;
    }

    @Override
    public void edit(OrderSetting orderSetting) {
        //这里的逻辑跟上面上传的一样

        //判断当前的日期之前是否设置过
        long count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());

        if (count > 0){ //如果设置过，更新数量
            orderSettingDao.update(orderSetting);
        }else {     //没有设置过，保存
            orderSettingDao.upload(orderSetting);
        }
    }

    @Override
    public void deleteOrderSettingHistoryData(String currentDate) {
        orderSettingDao.deleteOrderSettingHistoryData(currentDate);
    }

}
