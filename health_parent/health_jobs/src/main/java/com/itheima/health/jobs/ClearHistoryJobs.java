package com.itheima.health.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务
 * 定时清理预约设置历史数据
 */
public class ClearHistoryJobs {

    @Reference
    private OrderSettingService orderSettingService;

    public void clearHistoryData(){
        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //当前时间
        String currentDate = dateFormat.format(date);

        System.out.println("当前时间" + currentDate);

        orderSettingService.deleteOrderSettingHistoryData(currentDate);

        System.out.println("删除成功");
    }
}
