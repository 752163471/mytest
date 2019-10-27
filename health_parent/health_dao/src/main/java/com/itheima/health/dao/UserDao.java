package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    void add(User user);

    void addUserRoleByUserId(@Param("uid") Integer id, @Param("roleId") Integer roleId);

    List<User> findPage(String queryString);

    User findById(Integer id);

    List<Integer> findUserRoleByUserId(Integer id);

    void edit(User user);

    void deleteUserRoleById(Integer id);

    int queryUserRoleByUserId(Integer id);

    void delete(Integer id);

    User findUserByUsername(String s);
}
