package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/setmeal")
public class SetmealMobileController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping(path = "/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> list = setMealService.getSetmeal();
        if (list != null && list.size() > 0) {
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);

        /*try {
            String setmeal = setMealService.getSetmeal2();
            System.out.println(setmeal);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }*/

    }


    @RequestMapping(path = "/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setMealService.findCheckGroupListById(id);
        if (setmeal != null) {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,setmeal);
        }

        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);

    }

}
