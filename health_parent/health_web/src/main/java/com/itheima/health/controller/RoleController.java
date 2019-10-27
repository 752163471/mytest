package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @RequestMapping(path = "/findAll")
    public Result findAll(){

        List<Role> list = roleService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
    }

    //新增权限
    @RequestMapping(path = "/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        try{
            roleService.add(role,permissionIds,menuIds);
            return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_ROLE_FAIL);
        }
    }

    //分页查询
    @RequestMapping(path = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return roleService.findPage(queryPageBean);
    }
    //删除角色
    @RequestMapping(path = "/delete")
    public Result delete(@RequestParam("id") Integer id){
        try{
            roleService.delete(id);
            return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
    }
}
