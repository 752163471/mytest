package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/login")
public class LoginMobileController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping(path = "/check")
    public Result check(@RequestBody Map<String,Object> map, HttpServletResponse response){
        try {
            //先校验验证码
            String telephone = (String) map.get("telephone");
            //页面输入的验证码
            String validateCode = (String) map.get("validateCode");
            //从redis里拿验证码
            String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //校验验证码
            if (validateCode == null || !validateCode.equals(codeInRedis)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //查看是否是会员
            Member member = memberService.findByTelephone(telephone);
            //根据手机号码去查询
            if (member == null) {  //如果不是会员，就自动注册会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //如果是会员,向下执行，
            // 保存用户的登录状态，把验证码放到cookie里，
            Cookie cookie = new Cookie("Login_member_telephone",telephone);
            //设置cookie的有效范围
            cookie.setPath("/");
            //设置cookie的有效期限(一个月)
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }
    }
}
