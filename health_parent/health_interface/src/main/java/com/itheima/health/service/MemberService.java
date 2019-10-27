package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberByRegTime(List<String> monthList);


    List<Map<String, Object>> findMemberBySex();

    List<Map<String, Object>> findMemberByAge();

}
