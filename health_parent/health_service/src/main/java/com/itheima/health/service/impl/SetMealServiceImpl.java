package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.SetMealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

//    @Autowired
//    private RedisTemplate<String,Setmeal> redisTemplate;

//    @Autowired(required = false)
//    public void setRedisTemplate(RedisTemplate<String, Setmeal> redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        this.redisTemplate = redisTemplate;
//    }


    @Override
    public void add(Setmeal setmeal,Integer[] checkgroupIds) {

        setMealDao.add(setmeal);

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //操作中间关系表
        if (checkgroupIds != null && checkgroupIds.length > 0) {
          addSetmealAndCheckgroupBySetmealId(setmeal.getId(),checkgroupIds);
        }

    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用mybatis的分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<Setmeal> page = setMealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    //用于操作中间关系表
    private void addSetmealAndCheckgroupBySetmealId(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",id);
            map.put("checkgroupId",checkgroupId);
            setMealDao.addSetmealAndCheckgroupBySetmealId(map);
        }
    }

    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupBySetmealId(Integer id) {
        return setMealDao.findCheckGroupBySetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //修改redis中的数据
        Setmeal st = setMealDao.findById(setmeal.getId());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,st.getImg());

        setMealDao.edit(setmeal);
        //给中间关系表添加数据

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //1.需要先删除中间关系表的数据
        setMealDao.deleteSetmealAndCheckgroupBySetmealId(setmeal.getId());

        //2.然后重新插入数据
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            updateSetmealAndCheckgroupBySetmealId(setmeal.getId(),checkgroupIds);
        }

    }

    @Override
    public void delete(Integer id) {
        //删除有两种方案：
        //1.先删除中间关系表的数据，然后再删除套餐表的数据
        setMealDao.deleteSetmealAndCheckgroupBySetmealId(id);
        //先根据id查询到套餐表的img数据
        Setmeal setmeal = setMealDao.findById(id);
        //然后清空redis中的数据
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        setMealDao.delete(id);

        //2.先查询中间关系表是否有数据，如果有数据，抛出异常给出提示，不能删除
    }




    private void updateSetmealAndCheckgroupBySetmealId(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            setMealDao.updateSetmealAndCheckgroupBySetmealId(id,checkgroupId);
        }
    }
    //移动端查询套餐列表
   /* @Override
    public List<Setmeal> getSetmeal() {

        return setMealDao.getSetmeal();
    }*/

    //使用redis做缓存，存储套餐列表和套餐详情
   /* @Override
    public List<Setmeal> getSetmeal() {
        //Jedis jedis = jedisPool.getResource();
        List<Setmeal> list = new ArrayList<>();
        //优先查redis
        Set<String> keys = redisTemplate.keys(RedisConstant.SETMEAL_LIST_RESOURCES +"*");
        //如果redis没有，就从数据库查
        if (keys == null) { //如果redis里没有，从数据库查出来放进去
            List<Setmeal> setmealList = setMealDao.getSetmeal();
            for (Setmeal setmealInDb : setmealList) {
                redisTemplate.opsForValue().set(RedisConstant.SETMEAL_LIST_RESOURCES + setmealInDb.getId(),setmealInDb);
            }
        }else { //如果有
            for (String key : keys) {
                Setmeal setmeal = redisTemplate.opsForValue().get(key);
                list.add(setmeal);
            }
        }
        return list;
    }*/

    @Override
    public List<Setmeal> getSetmeal() {
        //优先从redis里面查询
        String setmealListStr = jedisPool.getResource().get(RedisConstant.SETMEAL_LIST_RESOURCES);
        //将json字符串数据转换成json集合对象[]
        JSONArray setmeals = JSONObject.parseArray(setmealListStr);
        //将json集合对象[]转换成List<Setmeal>集合
         List<Setmeal> setmealList = (List<Setmeal>) JSON.toJSON(setmeals);
         //如果没有缓存数据
        if (setmealList == null) {
            //调用dao
            setmealList = setMealDao.getSetmeal();
            //把集合数据放进redis
            jedisPool.getResource().set(RedisConstant.SETMEAL_LIST_RESOURCES,JSON.toJSONString(setmealList));
        }
        return setmealList;
    }


    //移动端查询套餐所对应的检查组(详细信息)
    @Override
    public Setmeal findCheckGroupListById(Integer id) {
        //先从redis中获取
        String setmealStr = jedisPool.getResource().get("Setmeal");
        //先将json字符串转换成json对象{}
        JSONObject setmealObj = JSON.parseObject(setmealStr);
        //然后将json对象转换成setmeal对象
        Setmeal setmeal = (Setmeal) JSON.toJSON(setmealObj);
        //如果redis没有，就从数据库拿
        if (setmealStr == null) {
            //从数据库中拿
            setmeal = setMealDao.findCheckGroupListById(id);
            //然后放到redis中，提升性能
            jedisPool.getResource().set("Setmeal",JSON.toJSONString(setmeal)); //将对象转换为json字符串
        }
        //如果有直接返回
        return setmeal;
    }

     /*@Override
    public Setmeal findCheckGroupListById(Integer id) {
        return setMealDao.findCheckGroupListById(id);
    }*/


    //查询套餐的数量和名称
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        //男女性别还需做处理
        return setMealDao.findSetmealCount();
    }

}
