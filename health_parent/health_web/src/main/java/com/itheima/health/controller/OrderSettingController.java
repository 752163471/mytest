package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping(path = "/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //使用POI解析文件，得到List<String[]> list
            List<String[]> strings = POIUtils.readExcel(excelFile);

            //把List<String[]> list 转成 List<OrderSetting> list
            List<OrderSetting> list = new ArrayList<>();

            for (String[] string : strings) {
                OrderSetting orderSetting = new OrderSetting(new Date(string[0]),Integer.parseInt(string[1]));

                list.add(orderSetting);
            }

            orderSettingService.upload(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }
    //展示日历预约设置信息
    @RequestMapping(path = "/findOrderSettingByOrderDate")
    public Result findOrderSettingByOrderDate(@RequestParam("orderDate") String orderDate){

        List<Map<String,Object>> mapList = orderSettingService.findOrderSettingByOrderDate(orderDate);

        if (mapList != null && mapList.size() > 0) {
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_FAIL);
    }

    //修改预约设置
    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.edit(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
