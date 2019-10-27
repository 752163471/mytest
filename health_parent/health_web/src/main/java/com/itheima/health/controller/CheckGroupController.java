package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //增加
    @RequestMapping(path = "/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    //分页查询
    @RequestMapping(path = "/pageQuery")
    public PageResult pageQuery(@RequestBody QueryPageBean queryPageBean){

        return checkGroupService.findPage(queryPageBean);
    }

    //编辑检查组(先根据id回显数据)
    @RequestMapping(path = "/findById")
    public Result findById(@RequestParam("id") Integer id){

            CheckItem checkItem =  checkGroupService.findById(id);
        if (checkItem != null) {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItem);
        }

        return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);

    }

    //查询检查组所对应的检查项
    @RequestMapping(path = "/findCheckItemByCheckGroupId")
    public List<Integer> findCheckItemByCheckGroupId(@RequestParam("id") Integer id){
        return checkGroupService.findCheckItemByCheckGroupId(id);
    }

    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //删除
    @RequestMapping(path = "/delete")
    public Result delete(@RequestParam("id") Integer id){
        try {
            checkGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }


    //查询所有检查组，套餐需要用
    @RequestMapping(path = "/findAll")
    public Result findAll(){
        List<CheckGroup> list = checkGroupService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }else {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


}
