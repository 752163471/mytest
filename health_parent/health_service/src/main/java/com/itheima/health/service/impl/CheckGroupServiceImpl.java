package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

        checkGroupDao.add(checkGroup);
        //还需操作中间关系表
        addCheckGroupCheckItemByCheckGroupId(checkGroup.getId(),checkitemIds);
    }



    private void addCheckGroupCheckItemByCheckGroupId(Integer checkGroupId, Integer[] checkitemIds) {
        //遍历集合，拿到其中的id值
        for (Integer checkitemId : checkitemIds) {
            checkGroupDao.addCheckGroupCheckItemByCheckGroupId(checkGroupId,checkitemId);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用mybatis的pageHelper分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.edit(checkGroup);
        //还需操作中间关系表
        //先删除中间关系表的关联数据，再重新插入
        deleteCheckGroupCheckItemByCheckGroupId(checkGroup.getId());

        //再重新插入新的数据
        addCheckGroupCheckItemByCheckGroupId(checkGroup.getId(),checkitemIds);
    }


    private void deleteCheckGroupCheckItemByCheckGroupId(Integer id) {
        checkGroupDao.deleteCheckGroupCheckItemByCheckGroupId(id);
    }

    @Override
    public void delete(Integer id) {
        //中间表有关联数据的时候，两种方案：
        //方案一：把中间关系表的数据一并删除掉
        //方案二：再执行删除之前，先查询中间关系表的数据，如果有数据，就不让删除

        //这里使用方案二
        //先删除中间关系表的数据
        deleteCheckGroupCheckItemByCheckGroupId(id);
        checkGroupDao.delete(id);


    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }



}
