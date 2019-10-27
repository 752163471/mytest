package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //使用mybatis分页插件
        //1.初始化参数
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //2.查询数据
        List<CheckItem> list = checkItemDao.findPage(queryPageBean.getQueryString());
        //3.封装PageHelp对应的结果集
        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
        //封装数据
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void delete(Integer id) throws RuntimeException{
        //这里做判断(检查项和检查组是多对多关系，有中间关系表)
        //一个检查项可以对应多个检查组，一个检查组可以有多个检查项
        //如果有中间关系表，两种解决方案
        //方案一：再执行删除之前，先删除中间关系表的数据，然后再删除检查项的值
        //方案二：做判断，如果有值，就不能直接删除
        long count = checkItemDao.queryCheckgroupCheckitemByCheckitemId(id);
        if (count > 0){
            throw new RuntimeException("中间表和检查组中有关联数据，不能删除");
        }
        checkItemDao.delete(id);
    }

    @Override
    public CheckItem findById(Integer id) {


        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
