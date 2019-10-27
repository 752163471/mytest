package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberByRegTime(List<String> monthList) {
        List<Integer> list = new ArrayList<>();
        for (String month : monthList) {
            String RegTime = month + "-31"; //可以根据月份计算当前月有没有31号
            Integer count = memberDao.findMemberByRegTime(RegTime);
            list.add(count);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findMemberBySex() {
        List<Map<String, Object>> mapList = memberDao.findMemberBySex();
        for (Map<String, Object> map : mapList) {
            String sex = (String) map.get("name");
            sex = "1".equals(sex)?"男":"女";
            map.put("name",sex);
        }
        return memberDao.findMemberBySex();
    }

    @Override
    public List<Map<String, Object>> findMemberByAge() {
        return memberDao.findMemberByAge();
    }

}
