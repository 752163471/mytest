package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import com.itheima.health.util.MD5Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    //新增用户
    @Override
    public void add(User user,Integer[] roleIds) {
        //对密码进行加密
        String password = MD5Utils.md5(user.getPassword());
        user.setPassword(password);
        userDao.add(user);

        if (roleIds != null && roleIds.length > 0) {
            addUserRoleByUserId(user.getId(),roleIds);
        }

    }

    //新增用户需要操作中间关系表
    private void addUserRoleByUserId(Integer id, Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            userDao.addUserRoleByUserId(id,roleId);
        }
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用mybatis的分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        List<User> list = userDao.findPage(queryPageBean.getQueryString());

        PageInfo<User> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    //编辑用户，根据id查询用户数据回显
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findUserRoleByUserId(Integer id) {
        return userDao.findUserRoleByUserId(id);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        //进行加密
        String password = MD5Utils.md5(user.getPassword());
        user.setPassword(password);
        userDao.edit(user);
        //还需操作中间关系表
        //先删除中间关系表的数据
        deleteUserRoleById(user.getId());
        //然后再重新插入数据
        addUserRoleByUserId(user.getId(),roleIds);
    }

    private void deleteUserRoleById(Integer id) {
        userDao.deleteUserRoleById(id);
    }


    //删除，有两种解决方案
    @Override
    public void delete(Integer id) throws RuntimeException{
        //方案一：先删除中间关系表的数据，然后再删除用户数据
        //方案二：先查询中间表的数据，如果中间关系表有数据就不让删除，抛异常出去，给出提示信息
        int count = userDao.queryUserRoleByUserId(id);
        if (count > 0) {
            throw new RuntimeException("用户表的中间关系表存在数据，不能删除!");
        }
        userDao.delete(id);
    }

    @Override
    public User findUserByUsername(String s) {
        return userDao.findUserByUsername(s);
    }

}

