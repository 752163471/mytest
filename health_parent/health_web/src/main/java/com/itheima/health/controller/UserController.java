package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Reference
    private UserService userService;

    //新增用户
    @RequestMapping(path = "/add")
    public Result add(@RequestBody User user,Integer[] roleIds){
        try {
            userService.add(user,roleIds);
            return new Result(true,MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }
    //分页查询
    @RequestMapping(path = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return userService.findPage(queryPageBean);
    }

    //编辑用户，根据id数据回显
    @RequestMapping(path = "/findById")
    public Result findById(@RequestParam("id") Integer id){
        User user = userService.findById(id);
        if (user != null) {
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        }
        return new Result(false,MessageConstant.QUERY_USER_FAIL);
    }
    //编辑用户，查询出用户所对应的角色
    @RequestMapping(path = "/findUserRoleByUserId")
    public List<Integer> findUserRoleByUserId(@RequestParam("id") Integer id){

        return userService.findUserRoleByUserId(id);
    }

    //编辑用户
    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody User user,Integer[] roleIds){
        try {
            userService.edit(user,roleIds);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }

    //删除用户
    @RequestMapping(path = "/delete")
    public Result delete(@RequestParam("id") Integer id){
        try {
            userService.delete(id);
            return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
    }

    //获取用户名
    @RequestMapping(path = "/getUsername")
    public Result getUsername(){
        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true,MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
