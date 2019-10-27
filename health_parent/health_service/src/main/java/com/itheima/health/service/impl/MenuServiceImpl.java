package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用mybatis的分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<Menu> page = menuDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public void delete(Integer id) throws RuntimeException{
        //删除有两种解决方案
        //这里使用先查询中间关系表是否有数据
        int count = menuDao.findRoleMenuByMenuId(id);
        if (count > 0){
            throw new RuntimeException("中间关系表存在数据，不能删除");
        }
            menuDao.delete(id);
    }

    @Override
    public List<Role> findMenu(String username) {
        //先要查询到用户所对应的角色

        User user = userDao.findUserByUsername(username);

        List<Role> list = roleDao.findRoleByUsername(user.getId());
        //然后再通过角色查询到所对应的菜单

        for (Role role : list) {
            for (Menu menu : role.getMenus()) {
                Map<String,Object> map = new HashMap<>();

            }
        }

        return list;
    }

    //查询所有
    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }
}
