package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    //增加权限
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用mybatis的分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<Permission> page = permissionDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    //编辑，数据回显
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    //变价权限
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void delete(Integer id) throws RuntimeException{
        //在删除之前，先查询中间关系表是否有数据
        int count = permissionDao.findRolePermissionByPermissionId(id);
        if (count > 0){
            throw new RuntimeException("中间关系表有数据，不能删除");
        }
        permissionDao.delete(id);
    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }


}
