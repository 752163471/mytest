package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.util.SMSUtils;
import com.itheima.health.util.ValidateCodeUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(path = "/validateCode")
public class ValidateCodeMobileController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(path = "/send4Order")
    public Result send4Order(String telephone){
        //发送验证码
        try {
            //生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            //使用阿里云服务发送验证码
            //SMSUtils.sendShortMessage(telephone,code.toString());
            //模拟发送验证码
            System.out.println("验证码：" + code);
            //把验证码存在redis里面(存5分钟)
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,5 * 60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS,code);
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }


    }
    //手机登录
    @RequestMapping(path = "/send4Login")
    public Result send4Login(String telephone) throws ClientException {
        try {
            //生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            //发送短信
            //SMSUtils.sendShortMessage(telephone,String.valueOf(code));
            System.out.println("验证码：" + code);

            //把验证码放进redis，只能存5分钟
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
