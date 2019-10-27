package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /*
        reportDate:null,
        todayNewMember :0,
        totalMember :0,
        thisWeekNewMember :0,
        thisMonthNewMember :0,
        todayOrderNumber :0,
        todayVisitsNumber :0,
        thisWeekOrderNumber :0,
        thisWeekVisitsNumber :0,
        thisMonthOrderNumber :0,
        thisMonthVisitsNumber :0,
        hotSetmeal :[
            {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
            {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
                    ]
     */

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> map = new HashMap<>();
        //先计算时间

        //当前时间(今天)
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //本周的第一天
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //本周的最有一天
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        //本月的第一天
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //本月的最后一天
        String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
        //报表时间
        map.put("reportDate",today);
        //今日新增会员数
        Integer todayNewMember = memberDao.findTodayNewMember(today);
        map.put("todayNewMember",todayNewMember);

        //总会员数
        Integer totalMember = memberDao.findTotalMember();
        map.put("totalMember",totalMember);

        //当前周 and 月 新增的会员数
        Integer thisWeekNewMember = memberDao.findAddNewMember(thisWeekMonday);
        Integer thisMonthNewMember = memberDao.findAddNewMember(firstDay4ThisMonth);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);


        //今天预约数
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
        map.put("todayOrderNumber",todayOrderNumber);
        //今天预约数，已到诊
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
        map.put("todayVisitsNumber",todayVisitsNumber);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderByDate(thisWeekMonday,thisWeekSunday);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        //本周已预约数已到诊的
        Integer thisWeekVisitsNumber = orderDao.findVisitsByDate(thisWeekMonday,thisWeekSunday);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        //本月已预约数
        Integer thisMonthOrderNumber = orderDao.findOrderByDate(firstDay4ThisMonth,lastDay4ThisMonth);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        //本月已预约数，已到诊的
        Integer thisMonthVisitsNumber=orderDao.findVisitsByDate(firstDay4ThisMonth,lastDay4ThisMonth);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        //热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
