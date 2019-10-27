package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberByRegTime(String regTime);


    List<Map<String, Object>> findMemberBySex();

    Integer findTodayNewMember(String today);

    Integer findTotalMember();

    Integer findAddNewMember(String thisWeekMonday);

    List<Map<String, Object>> findMemberByAge();

}
