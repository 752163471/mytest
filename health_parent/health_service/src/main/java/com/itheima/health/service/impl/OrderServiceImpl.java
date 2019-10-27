package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Result submit(Map<String, Object> map) throws Exception {

       //1.使用预约时间作为查询条件，查询预约设置表，判断当前时间是否可预约
        //获取预约时间
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByCondition(date);

            //不可以预约：提示"当前时间不能进行体检预约"
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //可以预约，往下执行
        //2.获取预约设置表的最多可预约人数和实际预约人数，如果2个值相等，此时提示："预约已满，不能预约"
        int number = orderSetting.getNumber();//最多可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (number <= reservations){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3.使用手机号作为查询条件，查询会员表，判断当前手机号是否已经注册过会员
        //获取手机号
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member == null) {
            //如果没有查询到结果，此时当前手机号没有注册过会员，自动注册会员(向会员表中新增一条数据)
            String name = (String) map.get("name");
            String sex = (String) map.get("sex");
            String idCard = (String) map.get("idCard");
            String phoneNumber = (String) map.get("telephone");
            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(phoneNumber);
            member.setRegTime(new Date());
            //向数据库保存
            memberDao.add(member);
        }else { //如果查询到结果，此时当前手机号是会员，判断当前会员是否在同一时间预约了同一套餐，保证不能重复预约
            //使用会员id，预约时间，套餐id，查询预约表(t_order)
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId")); //获取套餐id

            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            //如果存在数据，就说明是重复预约，提示"同一套餐，同一时间不能重复预约"
            if (list != null && list.size() > 0) {
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }
        //4.如果可以预约，根据预约时间更新预约设置表，让可预约人数的字段+1
        //orderSetting.setReservations(orderSetting.getReservations() + 1);会产生线程安全问题
        orderSettingDao.updateReservationsByOrderDate(orderSetting);
        //5.如果可以预约，向预约表中插入1条数据(t_order)，表示完成预约
        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        Order order = new Order(member.getId(),date,(String) map.get("orderType"),Order.ORDERSTATUS_NO,setmealId);
        orderDao.save(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
       Map<String,Object> map =  orderDao.findById(id);
       //这里需要处理时间格式
        Date orderDate = (Date) map.get("orderDate");
        String orderDateStr = DateUtils.parseDate2String(orderDate);
        map.put("orderDate",orderDateStr);
        return map;
    }
}
