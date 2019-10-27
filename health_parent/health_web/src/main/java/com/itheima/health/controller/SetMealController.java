package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    //上传
    @RequestMapping(path = "/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){

        try {
            //获取图片的名字
            String filename = imgFile.getOriginalFilename();
            //使得名字唯一
            filename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
            //上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            //也上传到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    //添加
    @RequestMapping(path = "/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    //分页查询
    @RequestMapping(path = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return setMealService.findPage(queryPageBean);
    }

    //编辑套餐，先回显套餐的基本信息
    @RequestMapping(path = "/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setMealService.findById(id);
        if (setmeal != null) {
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }
        return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }

    //用于套餐的检查组id回显
    @RequestMapping(path = "/findCheckGroupBySetmealId")
    public List<Integer> findCheckGroupBySetmealId(Integer id){
        return setMealService.findCheckGroupBySetmealId(id);
    }

    //编辑页面
    @RequestMapping(path = "/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }

    //删除套餐
    @RequestMapping(path = "/delete")
    public Result delete(@RequestParam("id") Integer id){
        try {
            setMealService.delete(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
