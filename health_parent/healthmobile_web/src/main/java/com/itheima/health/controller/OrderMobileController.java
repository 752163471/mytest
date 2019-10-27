package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping(path = "/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;


    @RequestMapping(path = "/submit")
    public Result submit(@RequestBody Map<String,Object> map){
        //获取用户信息
        //获取用户输入的验证码
        String code = (String) map.get("validateCode");
        //获取用户的手机号
        String telephone = (String) map.get("telephone");
        //获取存进去的验证码
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //校验验证码(redis里面存的和用户输入的比较)
        if (code == null || !code.equals(redisCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        try {
            //因为是用手机端预约，要给它一个预约类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //调用业务，进行预约
            return orderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    //显示预约成功
    @RequestMapping(path = "/findById")
    public Result findById(Integer id) throws Exception {
        Map<String,Object> map = orderService.findById(id);
        if (map != null) {
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }
        return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
    }
}
