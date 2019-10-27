package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    //增加菜单
    @RequestMapping(path = "/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }

    }
    //分页查询
    @RequestMapping(path = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean);
    }

    //编辑页面，用于数据回显
    @RequestMapping(path = "/findById")
    public Result findById(@RequestParam("id") Integer id){
        Menu menu = menuService.findById(id);
        if (menu != null) {
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
        }else {
            return new Result(false,MessageConstant.QUERY_MENU_FAIL);
        }
    }
    //编辑页面
    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
    }

    @RequestMapping(path = "/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
            return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @RequestMapping(path = "/findMenu")
    public Result findMenu(@RequestParam("username") String username){
        try{
            List<Role> list = menuService.findMenu(username);
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_MENU_FAIL);
        }
    }
    //查询所有的菜单
    @RequestMapping(path = "/findAll")
    public Result findAll(){
        try{
            List<Menu> list = menuService.findAll();
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_MENU_FAIL);
        }
    }
}
