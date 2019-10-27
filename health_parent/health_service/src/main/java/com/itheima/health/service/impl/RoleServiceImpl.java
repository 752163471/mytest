package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public void add(Role role, Integer[] permissionIds,Integer[] menuIds) {
        roleDao.add(role);
        //在role和permission的中间关系表添加数据
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermissionByRoleId(role.getId(),permissionId);
            }
        }
        //在role和menu的中间关系表添加数据
        if (menuIds != null && menuIds.length > 0) {
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenuByRoleId(role.getId(),menuId);
            }
        }

    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //使用mybatis的分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //删除两种方案
        //要么先删除中间关系表的数据，然后再删除表的数据
        //要么先查询种间关系表是否有数据，如果有数据，就抛出异常给出提示信息，不能删除

        //先删除角色和权限中间关系表的数据
        roleDao.deleteRolePermissionByRoleId(id);

        //再删除角色和菜单中间关系表的数据
        roleDao.deleteRoleMenuByRoleId(id);

        //最后再删除角色表的数据
        roleDao.deleteRoleByRoleId(id);
    }
}
