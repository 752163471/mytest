package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;


    //新增权限
    @RequestMapping(path = "/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

    //权限分页查询
    @RequestMapping(path = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return permissionService.findPage(queryPageBean);
    }

    //编辑权限，用于数据回显
    @RequestMapping(path = "/findById")
    public Result findById(Integer id){
        Permission permission = permissionService.findById(id);
        if (permission != null) {
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }
        return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
    }

    //编辑权限
    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
            return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }


    //删除
    @RequestMapping(path = "/delete")
    public Result delete(@RequestParam("id") Integer id){
        try {
            permissionService.delete(id);
            return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }
    //查询所有的权限
    @RequestMapping(path = "/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
    }

}
